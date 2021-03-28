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
import edu.montana.gsoc.msusel.pattern.gen.generators.ProjectGenerator
import edu.montana.gsoc.msusel.pattern.gen.logging.LoggerInit
import groovy.util.logging.Log

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log
class JavaProjectGenerator extends ProjectGenerator {

    JavaProjectGenerator() {
        LoggerInit.init(log)
    }

    @Override
    def generate() {
        log.info("Generating Project")
        Project proj = (Project) params.proj
        FileTreeBuilder builder = (FileTreeBuilder) params.builder

        if (proj.getModules().size() > 1) {
            ctx.dirGen.init(project: proj, module: null, tree: builder, num: params.num, pattern: params.pattern)
            ctx.dirGen.generate()
            proj.getModules().each { Module m ->
                builder."${m.getName()}" {
                    ctx.modGen.init(project: proj, mod: m, builder: builder, subproject: true, num: params.num, pattern: params.pattern)
                    ctx.modGen.generate()
                }
            }
        } else if (proj.getModules().size() == 1) {
            ctx.modGen.init(project: proj, mod: proj.getModules().first(), builder: builder, subproject: false, num: params.num, pattern: params.pattern)
            ctx.modGen.generate()
        }
        log.info("Done generating project")
    }
}
