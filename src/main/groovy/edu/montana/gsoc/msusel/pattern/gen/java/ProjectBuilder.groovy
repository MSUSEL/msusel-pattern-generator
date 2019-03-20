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
import edu.montana.gsoc.msusel.datamodel.Component
import edu.montana.gsoc.msusel.datamodel.structural.File

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

        List<File> files = Lists.newArrayList(data.tree.getUtils().getFiles())
        files.removeAll(defpkg)

        def map = [:]
        files.each {
            if (map[it.namespace.name()])
                map[it.namespace.name()] << it
            else
                map[it.namespace.name()] = [it]
        }

        def filetree = new FileTreeBuilder()
        filetree.dir("$data.base/$data.srcDir") {
            defpkg.each {
                "${it.name()}"(createUnitContents(it, StringBuilder.newInstance()))
            }
        }

        map.each {key, val ->
            File f = new File(base + "/src/main/java/" + key.toString().replaceAll(/\./, '/'))
            f.mkdirs()
            filetree = new FileTreeBuilder(f)
            filetree {
                val.each { File file ->
                    println "Creating file: ${file.name()}"
                    "${file.name()}"(createUnitContents(file, StringBuilder.newInstance()))
                }
            }
        }

        println "Done creating files"
        println ""
    }
}
