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

import edu.isu.isuese.datamodel.Accessibility
import edu.isu.isuese.datamodel.Component
import edu.isu.isuese.datamodel.Field

/**
 * @author Isaac Griffith
 * @vesion 1.3.0
 */
class FieldBuilder extends AbstractNodeBuilder {

    FieldBuilder(Component node) {
        super(node)
    }

    @Override
    def build(BuilderData data) {
        declareComment(data)
        declareField(data)
    }

    def declareComment(BuilderData data) {

    }

    def declareField(BuilderData data) {
        StringBuilder builder = new StringBuilder()

        if (node instanceof Field) {
            data.evtMgr.fireFieldCreationStarted(node.key, lookupFeatureRole(data, node), builder, data.parent.key)
//            if (!currentCue?.roles["${lookupTypeRole(t)}::${lookupFeatureRole(field)}"] || !currentCue?.roles["${lookupTypeRole(t)}::${lookupFeatureRole(field)}"].disregard) {
            builder << "    "

            if (node.getAccessibility() && node.getAccessibility() != Accessibility.DEFAULT) {
                builder << "${node.getAccessibility().toString().toLowerCase()}"
                builder << " "
            }

            node.modifiers.each {
                builder << it.toString().toLowerCase()
                builder << " "
            }

            builder << node.getType().name()
            builder << " "
            builder << node.getKey()

            builder << ";"

            builder << "\n"
//            }

            data.evtMgr.fireFieldCreationComplete(node.key, lookupFeatureRole(data, node), builder, data.parent.key)
        }

        return builder.toString()
    }
}
