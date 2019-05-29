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
package edu.montana.gsoc.msusel.pattern.gen.java

import com.google.common.collect.Lists
import edu.isu.isuese.datamodel.Component
import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Project

/**
 * @author Isaac Griffith
 * @vesion 1.3.0
 */
class ProjectBuilder extends AbstractNodeBuilder {

    ProjectBuilder(Component node) {
        super(node)
    }

    @Override
    def build(BuilderData data) {
        declareDirStructure(data)
        declareFiles(data)
    }

    def declareFiles(BuilderData data) {
        println ""
        println "Constructing Source Files from DataModelMediator"
        println ""

        selectCue()

        List<File> defpkg = []
        defpkg += tree.getFiles().findAll { File file ->
            file.namespace == null
        }

        if (node instanceof Project) {
            Project proj = (Project) node
            List<File> files = Lists.newArrayList(proj.getFiles())
            files.removeAll(defpkg)

            def map = [:]
            files.each {
                if (map[it.getParentNamespaces().first().getName()])
                    map[it.getParentNamespaces().first().getName()] << it
                else
                    map[it.getParentNamespaces().first().getName()] = [it]
            }

            def filetree = new FileTreeBuilder()
            filetree.dir("$data.base/$data.srcDir") {
                defpkg.each {
                    "${it.getName()}"(createUnitContents(it, StringBuilder.newInstance()))
                }
            }

            map.each { key, val ->
                java.io.File f = new java.io.File(base + "/src/main/java/" + key.toString().replaceAll(/\./, '/'))
                f.mkdirs()
                filetree = new FileTreeBuilder(f)
                filetree {
                    val.each { File file ->
                        println "Creating file: ${file.getName()}"
                        "${file.getName()}"(createUnitContents(file, StringBuilder.newInstance())) // TODO Fix this
                    }
                }
            }
        }

        println "Done creating files"
        println ""
    }
}
