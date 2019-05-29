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

import edu.isu.isuese.datamodel.Component
import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Import
import edu.isu.isuese.datamodel.Type

/**
 * @author Isaac Griffith
 * @vesion 1.3.0
 */
class FileBuilder extends AbstractNodeBuilder {

    FileBuilder(Component node) {
        super(node)
    }

    @Override
    def build(BuilderData data) {
        declareComment(data)
        declareNamespace(data)
        declareImports(data)
        declareTypes(data)
    }

    def declareComment(BuilderData data) {

    }

    def declareNamespace(BuilderData data) {
        StringBuilder builder = new StringBuilder()

        if (node instanceof File) {
            File file = (File) node
            if (file.getNamespace()) {
                builder << "package "
                builder << file.getNamespace().getKey()
                builder << ";"
            }
        }

        builder.toString()
    }

    def declareImports(BuilderData data) {
        StringBuilder builder = new StringBuilder()

        if (node instanceof File) {
            File file = (File) node
            file.imports().each { Import imp ->
                builder << "import "
                builder << imp.getKey()
                builder << ";"
                builder << "\n"
            }
        }

        builder.toString()
    }

    def declareTypes(BuilderData data) {
        StringBuilder builder = new StringBuilder()

        if (node instanceof File) {
            data.evtMgr.fireTypesStarted(node.key, null, builder, null)
            node.types().each { Type typ ->
                BuilderData newData = data.clone()
                newData.parent = node
                NodeBuilderFactory.instance.createBuilder(typ).build(newData)
            }
            data.evtMgr.fireTypesComplete(node.key, null, builder, null)
        }

        builder.toString()
    }
}
