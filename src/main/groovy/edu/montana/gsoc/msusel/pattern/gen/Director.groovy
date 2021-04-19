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

import com.google.common.collect.Table
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

            if (!context.generateOnly) {
                context.patterns.each {
                    context.loader.loadPatternCues(it, "java")
                    context.sysBuilder.init(pattern: it, num: context.numInstances)
                    System sys = context.sysBuilder.create()
                    systems += sys

                    if (!context.dataOnly) {
                        context.sysGen.init(sys: sys, builder: new FileTreeBuilder(new File(context.getOutput())), num: context.getNumInstances(), pattern: it)
                        context.sysGen.generate()
                    }

                    updateResults(sys, it)
                }
            }

            // Close DB Connection
            manager.close()
        }
    }

    void updateResults(System sys, String pattern) {
        List<Table.Cell<String, String, String>> cells = context.results.cellSet().findAll {
            it.columnKey == "PatternType" && it.value == pattern
        }.toList()
        List<String> ids = cells.collect {it.rowKey}

        List<Project> projects = sys.getProjects()

        assert ids.size() == projects.size()

        for (int i = 0; i < ids.size(); i++) {
            context.results.put(ids.get(i), "Key1", projects.get(i).getProjectKey())
            context.results.put(ids.get(i), "Path1", projects.get(i).getFullPath())
        }
    }
}
