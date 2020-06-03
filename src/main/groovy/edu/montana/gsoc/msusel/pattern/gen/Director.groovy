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
import edu.isu.isuese.datamodel.util.DBManager

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

        if (context.resetDb) {
            manager.createDatabase(context.db.type, context.db.driver, context.db.url, context.db.user, context.db.pass)
        }

        if (!context.resetOnly) {
            // Open DB Connection
            manager.open(context.db.driver, context.db.url, context.db.user, context.db.pass)

            if (!context.generateOnly) {
                context.patterns.each {
                    context.resetPatternBuilderComponents()
                    context.sysBuilder.init(pattern: it, num: context.numInstances)
                    context.sysBuilder.create()
                }
            }

            if (!context.dataOnly) {
                context.projectKeys.each {
                    Project proj = Project.findFirst("projKey = ?", it)
                    if (proj) {
                        System sys = proj.getParentSystem()
                        context.loader.loadPatternCues(sys.name)
                        context.sysGen.init()
                        context.sysGen.generate()
                    }
                }
            }

            // Close DB Connection
            manager.close()
        }
    }
}
