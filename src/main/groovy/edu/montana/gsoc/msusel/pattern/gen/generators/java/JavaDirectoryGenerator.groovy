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
package edu.montana.gsoc.msusel.pattern.gen.generators.java

import edu.isu.isuese.datamodel.Module
import edu.isu.isuese.datamodel.Project
import edu.montana.gsoc.msusel.pattern.gen.generators.DirectoryGenerator
import groovy.util.logging.Log4j2
import org.apache.logging.log4j.Level

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class JavaDirectoryGenerator extends DirectoryGenerator {

    JavaDirectoryGenerator() {
    }

    /**
     * Constructs the following directory structure for java projects
     *
     * base/ProjectName
     *   |--src/
     *   |   |--main/
     *   |   |   |--java/
     *   |   |   |--resources/
     *   |   |--test/
     *   |       |--java/
     *   |       |--resources/
     *   |--target (or build/)
     *   |--pom.xml (or build.gradle)
     *   |--LICENSE
     *   |--README.md
     */
    @Override
    def generate() {
        log.log(Level.INFO,"generating the source directory")
        FileTreeBuilder builder = (FileTreeBuilder) params.tree
        Module mod = (Module) params.module
        Project proj = (Project) params.project
        boolean subproject = (boolean) params.subproject

        builder {
            src {
                main {
                    java {
                        if (proj) {
                            proj.getNamespaces().each {
                                ctx.nsGen.init(ns: it, builder: builder)
                                ctx.nsGen?.generate()
                            }
                        }
                    }
                    resources {}
                }
                test {
                    java {}
                    resources {}
                }
            }
            proj.getFiles().each {
                ctx.fileGen.init(file: it, builder: builder)
                ctx.fileGen?.generate()
            }
            ctx.licGen?.init(tree: builder)
            ctx.readmeGen?.init(tree: builder, number: params.num, pattern: params.pattern)
            ctx.ignoreGen?.init(tree: builder)

            ctx.licGen?.generate()
            ctx.readmeGen?.generate()
            ctx.ignoreGen?.generate()
            if (subproject) {
                ctx.buildGen?.init(tree: builder)
                ctx.buildGen?.generateSubproject(builder)
            } else {
                ctx.buildGen?.init(tree: builder, project: proj)
                ctx.buildGen?.generate()
            }
        }

        log.log(Level.INFO,"Done generating the source directory")
    }
}
