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
import edu.montana.gsoc.msusel.datamodel.member.Field
import edu.montana.gsoc.msusel.datamodel.member.Literal
import edu.montana.gsoc.msusel.datamodel.member.Method
import edu.montana.gsoc.msusel.datamodel.type.Enum
import edu.montana.gsoc.msusel.datamodel.type.Type

/**
 * @author Isaac Griffith
 * @vesion 1.3.0
 */
class TypeBuilder extends AbstractNodeBuilder {

    TypeBuilder(Component node) {
        super(node)
    }

    @Override
    def build(BuilderData data) {
        StringBuilder builder = new StringBuilder()

        data.evtMgr.fireTypeCreationStarted(node.key, lookupTypeRole(data, node), builder, node.key)
        builder << declareComment(data)
        builder << declareTypeHeader(data)
        builder << declareTypeBody(data)
        data.evtMgr.fireTypeCreationComplete(node.key, lookupTypeRole(data, node), builder, node.key)

        builder.toString()
    }

    def declareComment(BuilderData data) {
        StringBuilder builder = new StringBuilder()

        builder << "/**\n"
        builder << " * \n"
        builder << " */\n"

        builder.toString()
    }

    def declareTypeHeader(BuilderData data) {
        StringBuilder builder = new StringBuilder()

        Type typ = (Type) node
        data.evtMgr.fireTypeHeader(typ.key, lookupTypeRole(data, typ), builder, typ.key)

        if (typ.getAccessibility() && typ.getAccessibility() != Accessibility.DEFAULT) {
            builder << typ.getAccessibility().toString().toLowerCase()
            builder << " "
        }

        typ.modifiers.each {
            builder << it.toString().toLowerCase()
            builder << " "
        }

        builder << "${designator}"
        builder << " "
        builder << typ.name()
        builder << " "

        def extnds = data.tree.getGeneralizedFrom(typ)
        if (extnds) {
            builder << "extends "
            builder << ((Type) extnds[0]).name()
            builder << " "
        }

        List impls = data.tree.getRealizedFrom(typ)
        if (impls) {
            builder << "implements "
            impls.each { Type type ->
                builder << type.name()
                if (impls.last() != type)
                    builder << ", "
            }
            builder << " "
        }

        builder << "\n"

        builder.toString()
    }

    def declareTypeBody(BuilderData data) {
        StringBuilder builder = new StringBuilder()

        if (node instanceof Type) {
            data.evtMgr.fireTypeBody(node.key, lookupTypeRole(data, node), builder, node.key)

            builder << "{"
            builder << "\n"

            if (node instanceof Enum) {
                builder << createEnumLiterals(data)
                builder << "\n"
            }

            builder << createFields(data)
            builder << "\n"
            builder << createMethods(data)
            builder << "\n"
            builder << createContainedTypes(data)

            builder << "\n"
            builder << "}"
        }

        builder.toString()
    }

    def createEnumLiterals(BuilderData data) {
        StringBuilder builder = new StringBuilder()

        if (node instanceof Enum) {
            data.evtMgr.fireLiteralsStarted(node.key, null, builder, node.key)

            builder << "    "
            node.getLiterals().each { Literal literal ->
                builder << NodeBuilderFactory.instance.createBuilder(literal).build(getClonedData(data))
            }
            builder << ";\n"
            data.evtMgr.fireLiteralsCompleted(node.key, null, builder, node.key)
        }

        builder.toString()
    }

    def createMethods(BuilderData data) {
        StringBuilder builder = new StringBuilder()

        if (node instanceof Type) {
            data.evtMgr.fireMethodsStarted(node.key, null, builder, node.key)
            node.methods().each { Method method ->
                builder << NodeBuilderFactory.instance.createBuilder(method).build(getClonedData(data))
            }
            data.evtMgr.fireMethodsComplete(node.key, null, builder, node.key)
        }

        builder.toString()
    }

    def createFields(BuilderData data) {
        StringBuilder builder = new StringBuilder()

        if (node instanceof Type) {
            data.evtMgr.fireFieldsStarted(node.key, null, builder, node.key)
            node.fields().each { Field field ->
                builder << NodeBuilderFactory.instance.createBuilder(field).build(getClonedData())
            }
            data.evtMgr.fireFieldsComplete(node.key, lookupTypeRole(data, node), builder, node.key)
        }

        builder.toString()
    }

    def createContainedTypes(BuilderData data) {

    }
}
