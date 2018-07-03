/**
 * MIT License
 *
 * MSUSEL Design Pattern Generator
 * Copyright (c) 2017-2018 Montana State University, Gianforte School of Computing
 * Software Engineering Laboratory
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

import edu.montana.gsoc.msusel.datamodel.Accessibility
import edu.montana.gsoc.msusel.datamodel.Component
import edu.montana.gsoc.msusel.datamodel.member.Constructor
import edu.montana.gsoc.msusel.datamodel.member.Method

/**
 * @author Isaac Griffith
 * @vesion 1.3.0
 */
class MethodBuilder extends AbstractNodeBuilder {

    MethodBuilder(Component node) {
        super(node)
    }

    @Override
    def build(BuilderData data) {
        StringBuilder builder = new StringBuilder()

        data.evtMgr.fireMethodCreationStarted(node.key, lookupFeatureRole(data, node), builder, data.parent.key)
        builder << declareComment(data)
        builder << "\n"
        builder << declareMethodHeader(data)
        builder << "\n"
        builder << declareMethodBody(data)
        data.evtMgr.fireMethodCreationComplete(node.key, lookupFeatureRole(data, node), builder, data.parent.key)

        builder.toString()
    }

    def declareComment(BuilderData data) {
        StringBuilder builder = new StringBuilder()

        if (node instanceof Method) {
            //        if (method.isOverriding()) {
            //            builder.comment(type: "block", format: "javadoc") {
            //                mkp.yield("    /**\n")
            //                mkp.yield("     * {@inheritDoc}\n")
            //                mkp.yield("     */\n")
            //            }
            //            builder.annotation {
            //                mkp.yield "    @"
            //                name "Override"
            //                mkp.yield "\n"
            //            }
            //        } else {
            builder << "    /**\n"
            node.getParams().each { param ->
                builder << "     * @param ${param.getKey()} \n"
            }
            if (node.getType().name() != "void")
                builder << "     * @return \n"
            builder << "     */"
            //        }
        }

        builder.toString()
    }

    def declareMethodHeader(BuilderData data) {
        StringBuilder builder = new StringBuilder()

        if (node instanceof Method) {
            data.evtMgr.fireMethodHeader(node.key, lookupFeatureRole(data, node), builder, data.parent.key)
            if (node.getAccessibility() && node.getAccessibility() != Accessibility.DEFAULT) {
                builder << "${node.getAccessibility().toString().toLowerCase()}"
                builder << " "
            }
            if (node.modifiers) {
                node.modifiers.each {
                    builder << it.toString().toLowerCase()
                    builder << " "
                }
            }

            if (node instanceof Constructor) {
                builder << node.getType().name()
                builder " "
                builder << node.name()
            } else {
                if (node.getType())
                    builder << node.getType().name()
                else
                    builder << "void"
                builder << " "
            }

            builder << node.name()
            builder << "("
            node.params.each {
                builder << it.type.name()
                builder << " "
                builder << it.name()
                if (it != node.params.last())
                    builder << ", "
            }
            builder << ")"
        }

        builder.toString()
    }

    def declareMethodBody(BuilderData data) {
        StringBuilder builder = new StringBuilder()

        if (node instanceof Method) {
            if (node.isAbstract())
                builder << ";"
            else {
                data.evtMgr.fireMethodBody(node.key, lookupFeatureRole(data, node), builder, data.parent.key)
                builder << " {"
                builder << "\n"
                builder << "    }"
            }
        }

        builder.toString()
    }
}
