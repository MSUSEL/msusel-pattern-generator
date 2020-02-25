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

import edu.isu.isuese.datamodel.Constructor
import edu.isu.isuese.datamodel.Destructor
import edu.isu.isuese.datamodel.Field
import edu.isu.isuese.datamodel.Interface
import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.pattern.cue.CueRole
import edu.montana.gsoc.msusel.pattern.gen.current.MethodGenerator
import edu.montana.gsoc.msusel.rbml.model.Role

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class JavaMethodGenerator extends MethodGenerator {

    CueRole cueRole

    @Override
    String generate() {
        if (!params.method && !params.field)
            throw new IllegalArgumentException("Method and Field cannot be null")

        Method method = (Method) params.method
        Field field = (Field) params.field
        Type parent = (Type) params.parent

        Role role = findRole(method)
        if (role)
            cueRole = (CueRole) ctx.cue.roles[role.name]

        String output = ""

        if (method)
            output += generate(method, parent)
        else if (field)
            output += generate(field)

        output
    }

    String generate(Method method, Type parent) {
        String output

        if (!cueRole || !cueRole?.disregard) {
            if (cueRole?.definition) {
                output = cueRole.definition(parent.compKey)
            } else {
                switch (method) {
                    case Constructor:
                        output = createConstructor(method, cueRole)
                        break
                    case Destructor:
                        output = createDestructor(method, cueRole)
                        break
                    default:
                        output = createMethod(method, cueRole)
                        break
                }
            }
        }

        output
    }

    String generate(Field field) {
        if (!field)
            throw new IllegalArgumentException("Field cannot be null")

        String line = """\
            ${createAccessor(field)}"""
        if (!field.modifiers.find { it.name == "FINAL" })
        line += """
            ${createMutator(field)}"""
        line
    }

    private String createMethod(Method method, CueRole cueRole) {
        String name = method.name
        String body = method.isAbstract() ? ";" : getMethodBody(cueRole)
        String paramList = getParamList(method)
        String modifiers = getModifiers(method)
        modifiers = modifiers ? modifiers + " " : ""
        String type = method.type.typeName
        String comment = getComment(method)
        String access = method.accessibility.toString().toLowerCase()
        access = access != "default" ? access + " " : ""

        String excepts = ""
        if (method.exceptions) {
            excepts = " throws "
            def exceptList = []
            method.exceptions.each {
                exceptList << it.getTypeRef().typeName
            }
            excepts += exceptList.join(", ")
        }

        if (method.getParentTypes()) {
            if (method.getParentTypes().first() instanceof Interface) {
                """
            $comment
            $type $name($paramList)$excepts;"""
            } else {
                if (body != ";")
                    excepts += " "
                """
            $comment
            $access$modifiers$type $name($paramList)$excepts$body"""
            }
        }  else {
            if (body != ";")
                excepts += " "
            """
            $comment
            $access$modifiers$type $name($paramList)$excepts$body"""
        }
    }

    private String getComment(Method method) {
        String params = ""
        String ret = ""
        String excepts = ""
        method.params.each {
            params += """
             * @param ${it.name}"""
        }
        if (method.type.typeName != "void") {
            ret = """
             * @return"""
        }
        method.exceptions.each {
            excepts += """
             * @throws ${it.getTypeRef().typeName}"""
        }
        String comment = """/**
             * $params${ret ?: ""}$excepts
             */"""
        if (method.isOverriding()) {
            comment += """
            @Override"""
        }
        comment
    }

    private String getParamList(Method method) {
        def list = []
        if (method.params) {
            method.params.each { p ->
                def modList = []
                p.modifiers.each {
                    modList << it.name.toLowerCase()
                }
                String item = ""
                if (modList)
                    item = modList.join(" ") + " "
                list << "$item${p.type.typeName} ${p.name}"
            }
            list.join(", ")
        } else {
            ""
        }
    }

    private String getModifiers(Method method) {
        def list = []
        if (method.modifiers) {
            method.modifiers.each { m ->
                list << m.name.toLowerCase()
            }
            list.join(" ")
        } else {
            ""
        }
    }

    private String createConstructor(Method method, CueRole cueRole) {
        String name = method.name

        String body = getMethodBody(cueRole)
        String paramList = ""
        String comment = """/**
             * Construct a new $name instance
             */"""
        String access = method.accessibility.toString().toLowerCase()

        """
            $comment
            $access $name($paramList) $body"""
    }

    private String getMethodBody(CueRole cueRole) {
        cueRole?.content ? """{
                ${cueRole.content(((Type) params.parent)?.compKey)}
            }""" : """{
            }"""
    }

    private String createDestructor(Method method, CueRole cueRole) {
        """
            /**
             *
             */
            @Override
            protected void finalize() throws Throwable {
            }"""
    }

    private String createMutator(Field field) {
        String name = field.name
        String capName = name.capitalize()
        String type = field.type.typeName
        """
            /**
             * @param $name the new value for $name
             */
            public ${field.modifiers.find { it.name == 'STATIC' } ? 'static ' : ''}void set$capName($type $name) {
                this.$name = $name;
            }"""
    }

    private String createAccessor(Field field) {
        String name = field.name
        String capName = name.capitalize()
        String type = field.type.typeName
        if (type != "boolean") {
            """
            /**
             * @return the value of $name
             */
            public ${field.modifiers.find { it.name == 'STATIC' } ? 'static ' : ''}$type get$capName() {
                return $name;
            }"""
        } else {
            """
            /**
             * @return the value of $name
             */
            public ${field.modifiers.find { it.name == 'STATIC' } ? 'static ' : ''}$type is$capName() {
                return $name;
            }"""
        }
    }
}
