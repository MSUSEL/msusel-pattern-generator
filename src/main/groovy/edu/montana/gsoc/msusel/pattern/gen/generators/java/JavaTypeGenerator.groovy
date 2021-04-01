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
import edu.montana.gsoc.msusel.pattern.gen.cue.Cue
import edu.montana.gsoc.msusel.pattern.gen.cue.CueManager
import edu.montana.gsoc.msusel.pattern.gen.cue.CueParams
import edu.montana.gsoc.msusel.pattern.gen.generators.TypeGenerator
import edu.montana.gsoc.msusel.pattern.gen.logging.LoggerInit
import groovy.util.logging.Log

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log
class JavaTypeGenerator extends TypeGenerator {

    JavaTypeGenerator() {
        LoggerInit.init(log)
    }

    @Override
    String generate() {
        Type type = (Type) params.type

//        String roleName = findRole(type)?.name
//        Cue cue = CueManager.getInstance().getCurrent()

        String output = ""
//        if (roleName && cue.hasCueForRole(roleName, type)) {
//            output = fromCue(cue, type)
//        } else {
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
//        }

        log.info("Done generating type")
        output
    }

    private String fromCue(Cue cue, Type type) {
        String kind = type.getClass().getSimpleName().toLowerCase();

        CueParams params = new CueParams()
        params.setParam("literals", genLiterals(type))
        params.setParam("typedef", createTypeDef(kind, type, type.name))
        params.setParam("fields", genFields(type))
        params.setParam("methods", genMethods(type))
        params.setParam("InstName", type.name)
        params.setParam("ClassComment", typeComment())

        cue.compile(params, ctx.rbmlManager)
    }

    private String createTemplate(String kind, Type type) {
        String name = type.name

        """\
        /**
        ${typeComment()}
         */
        ${createTypeDef(kind, type, name)} {${getContent(type)}
        }
        """
    }

    private String createTypeDef(String kind, Type type, String name) {
        "${access(type)}${modifiers(type)}$kind ${name}${extendsList(type)}${implementsList(type)}"
    }

    private String getContent(Type type) {
        return """${
            genLiterals(type)
        }${genFields(type)}${genMethods(type)}"""
    }

    private String typeComment() {
        """ * Generated Class
         *
         * @author Isaac Griffith
         * @version 1.0"""
    }

    private String access(Type type) {
        String access = type.accessibility.toString().toLowerCase().replaceAll(/_/, " ") + " "
        if (access == "default " || access == " ")
            access = ""

        access
    }

    private String modifiers(Type type) {
        String content = ""

        if (type.modifiers) {
            content += type.modifiers.collect { it.name.toLowerCase() }.join(" ")
            content += " "
        }
        if (content.contains("abstract ") && type instanceof Interface)
            content = content.replace("abstract ", "")

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
        if (type.realizes) {
            content += " implements "
            Set<String> set = Sets.newTreeSet()
            set.addAll(type.getRealizes().collect { it.name })
            content += set.join(", ")
        }

        content
    }

    private String genLiterals(Type type) {
        String content = ""

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

        content
    }

    private String genFields(Type type) {
        String content = ""

        if (type.fields) {
            content += "\n"
            type.fields.each { Field f ->
                content += "\n            "
                ctx.fieldGen.init(field: f, type: type)
                content += ctx.fieldGen.generate()
            }
        }

        content
    }

    private String genMethods(Type type) {

        String content = ""

        if (!(type instanceof Interface)) {
            type.fields.each { Field f ->
                ctx.methodGen.init(field: f, parent: type)
                content += "\n    "
                content += ctx.methodGen.generate()
            }
        }
        println("Number of Methods to generate: ${type.methods.size()}")
        type.methods.each { Method m ->
            ctx.methodGen.init(method: m, parent: type)
            content += "\n    "
            content += ctx.methodGen.generate()
        }

        content
    }
}