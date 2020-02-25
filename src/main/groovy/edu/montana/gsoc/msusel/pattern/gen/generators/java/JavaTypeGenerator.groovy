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

import com.google.common.collect.Sets
import edu.isu.isuese.datamodel.*
import edu.montana.gsoc.msusel.pattern.cue.CueRole
import edu.montana.gsoc.msusel.pattern.gen.current.TypeGenerator
import edu.montana.gsoc.msusel.pattern.gen.event.EventType

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class JavaTypeGenerator extends TypeGenerator {

    CueRole cueRole

    @Override
    String generate() {

        File parent = (File) params.parent
        Type type = (Type) params.type

        edu.montana.gsoc.msusel.rbml.model.Role role = findRole(type)
        if (role)
            cueRole = (CueRole) ctx.cue.roles[role.name]

        // fire TypeCreationStarted event
//        events.fireTypeCreationStarted(type.compKey, null, this, parent.fileKey)

        String output = ""
        if (!cueRole || !cueRole?.disregard) {
            if (cueRole?.definition) {
                output = cueRole.definition(type.compKey)
            } else {
                switch (type) {
                    case Class:
                        output = createTemplate("class", type)
                        break
                    case Enum:
                        output = createTemplate("enum", type)
                        break
                    case Interface:
                        output = createTemplate("interface", type)
                        break
                }
            }
        }

        // fire TypeCreationCompleted event
//        events.fireTypeCreationComplete(type.compKey, null, this, parent.fileKey)

        output
    }

    private String createTemplate(String kind, Type type) {
        String name = type.name

        """\
        /**
        ${typeComment(type)}
         */
        ${access(type)}${modifiers(type)}$kind ${name}${extendsList(type)}${implementsList(type)} {${getContent(type)}
        }
        """
    }

    private String getContent(Type type) {
        if (cueRole?.content) {
            return """
            ${cueRole.content(type.name)}"""
        } else {
            return """${
                genLiterals(type)
            }${genFields(type)}${genMethods(type)}"""
        }
    }

    private String typeComment(Type type) {
        """ * Generated Class
         *
         * @author Isaac Griffith
         * @version 1.0"""
    }

    private String access(Type type) {
        String access = type.accessibility.toString().toLowerCase().replaceAll(/_/, " ") + " "
        if (access == "default ")
            access = ""

        access
    }

    private String modifiers(Type type) {
        String content = ""

        if (type.modifiers) {
            content += type.modifiers.collect { it.name.toLowerCase() }.join(" ")
            content += " "
        }

        content
    }

    private String extendsList(Type type) {
        String content = ""
        if (type instanceof Enum)
            return content
        if (type.getGeneralizedBy()) {
            content += " extends "
            Set<String> set = Sets.newTreeSet()
            set.addAll(type.getGeneralizedBy().collect { it.name })
            content += set.join(", ")
        }

        content
    }

    private String implementsList(Type type) {
        String content = ""
        if (type instanceof Interface)
            return content
        if (type.getRealizes()) {
            content += " implements "
            Set<String> set = Sets.newTreeSet()
            set.addAll(type.getRealizes().collect { it.name })
            content += set.join(", ")
        }

        content
    }

    private String genLiterals(Type type) {
        String content = ""

        if (cueRole?.content && cueRole.event == EventType.LiteralsStarted) {
            content += cueRole.content(type.compKey)
        }

        if (type instanceof Enum) {
            if (type.literals)
                content += "\n"
            type.literals.each { Literal l ->
                content += "\n        "
                ctx.literalGen.init(parent: type, literal: l)
                content += ctx.literalGen.generate()
                content += ","
            }
        }

        int index = content.lastIndexOf(",")
        if (index >= 0)
            content = content.substring(0, index) + ";"

        if (cueRole?.content && cueRole.event == EventType.LiteralsComplete) {
            content += cueRole.content(type.compKey)
        }

        content
    }

    private String genFields(Type type) {
        String content = ""

        if (cueRole?.content && cueRole.event == EventType.FieldsStarted) {
            content += cueRole.content(type.compKey)
        }

        if (type.fields) {
            content += "\n"
            type.fields.each { Field f ->
                content += "\n            "
                ctx.fieldGen.init(field: f, type: type)
                content += ctx.fieldGen.generate()
            }
        }

        if (cueRole?.content && cueRole.event == EventType.FieldsComplete) {
            content += cueRole.content(type.compKey)
        }

        content
    }

    private String genMethods(Type type) {

        String content = ""

        if (cueRole?.content && cueRole.event == EventType.MethodsStarted) {
            content += cueRole.content(type.compKey)
        }

        if (!(type instanceof Interface)) {
            type.fields.each { Field f ->
                ctx.methodGen.init(field: f, parent: type)
                content += "\n    "
                content += ctx.methodGen.generate()
            }
        }
        type.methods.each { Method m ->
            ctx.methodGen.init(method: m, parent: type)
            content += "\n    "
            content += ctx.methodGen.generate()
        }

        if (cueRole?.content && cueRole.event == EventType.MethodsComplete) {
            content += cueRole.content(type.compKey)
        }

        content
    }
}
