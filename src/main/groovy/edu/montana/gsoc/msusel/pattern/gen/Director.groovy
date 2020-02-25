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

import edu.isu.isuese.datamodel.System

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
        manager = new DBManager(context: context)
    }

    void execute() {

        if (context.resetDb) {
            manager.createDatabase()
        }

        if (!context.resetOnly) {
            // Open DB Connection
            manager.open()

            if (!context.generateOnly) {
                context.patterns.each {
                    context.resetPatternBuilderComponents()
                    context.sysBuilder.init(pattern: it, num: context.numInstances)
                    context.sysBuilder.create()
                }
            }

            if (!context.dataOnly) {
                System.findAll().each {
                    if (it instanceof System) {
                        context.loader.loadPatternCues(it.name)

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
