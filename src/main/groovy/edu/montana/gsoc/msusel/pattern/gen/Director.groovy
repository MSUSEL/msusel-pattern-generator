/**
 * MIT License
 *
 * MSUSEL Design Pattern Generator
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package edu.montana.gsoc.msusel.pattern.gen

import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.System
import edu.isu.isuese.datamodel.util.DBCredentials
import edu.isu.isuese.datamodel.util.DBManager
import edu.montana.gsoc.msusel.pattern.gen.cue.CueManager

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class Director {

    GeneratorContext context
    DBManager manager

    void initialize() {
        context = GeneratorContext.getInstance()
        context.resetPatternBuilderComponents()
        context.resetComponentGenerators()
        manager = DBManager.instance
    }

    void execute() {
        DBCredentials creds = DBCredentials.builder()
                .driver(context.db.driver)
                .type(context.db.type)
                .url(context.db.url)
                .user(context.db.user)
                .pass(context.db.pass)
                .create()
        if (context.resetDb) {
            manager.createDatabase(creds)
        }

        List<System> systems = []

        if (!context.resetOnly) {
            // Open DB Connection
            manager.open(creds)

            println "Results\n${context.results}"
            Set<String> patterns = new HashSet<>()
            context.results.rowMap().each { String id, Map<String, String> map ->
                patterns << map.get("PatternType")
            }
            println "Patterns: $patterns"

            if (!context.generateOnly) {
                patterns.each { pattern ->
                    Map<String, String> map = context.results.column("PatternType").findAll { String id, String value -> value == pattern }
                    println "PatternType: $pattern"
                    context.loader.loadPatternCues(pattern, "java")

                    map.each { id, val ->
                        System sys = System.findFirst("name = ?", pattern)
                        if (!sys) {
                            context.sysBuilder.init(pattern: pattern, id: id)
                            sys = context.sysBuilder.create()
                            systems << sys
                        } else {
                            context.sysBuilder.init(pattern: pattern, system: sys, id: id)
                            context.sysBuilder.create()
                            if (!systems.contains(sys))
                                systems << sys
                        }
                        context.resetPatternBuilderComponents()
                    }
                }
            }

            if (!context.dataOnly) {
                systems.each {sys ->
                    println("System: ${sys.getKey()} with name: ${sys.name}")
                    context.sysGen.init(sys: sys, builder: new FileTreeBuilder(new File(context.getOutput())), pattern: sys.getName())
                    context.sysGen.generate()
                }
            }

            // Close DB Connection
            manager.close()
        }
    }
}
