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

import edu.isu.isuese.datamodel.Field
import edu.isu.isuese.datamodel.Interface
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.pattern.cue.CueRole
import edu.montana.gsoc.msusel.pattern.gen.generators.FieldGenerator
import edu.montana.gsoc.msusel.rbml.model.Role

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class JavaFieldGenerator extends FieldGenerator {

    @Override
    String generate() {
        if (!params.field)
            throw new IllegalArgumentException("Field cannot be null")

        Field field = (Field) params.field
        Type parent = (Type) params.type

        Role role = findRole(field)
        CueRole cueRole = null

        if (role)
            cueRole = (CueRole) ctx.cue?.roles[role.name]

        String retVal = ""

        if (!cueRole || !cueRole?.disregard) {
            if (cueRole?.definition) {
                retVal = cueRole.definition(parent.compKey)
            } else {
                if (!defaultAccessiblity(field)) {
                    if (parentIsNotNullOrInterface(field))
                        retVal = field.getAccessibility().toString().toLowerCase() + " "
                }

                if (parentIsNotNullOrInterface(field)) {
                    field.getModifiers().each {
                        retVal += it.getName().toLowerCase()
                        retVal += " "
                    }
                }

                retVal += "${field.getType().typeName} ${field.name};"
            }
        }

        retVal
    }

    private boolean defaultAccessiblity(Field field) {
        field.getAccessibility().toString().toLowerCase() == "default"
    }

    private boolean parentIsNotNullOrInterface(Field field) {
        !field.getParentTypes() || !(field.getParentTypes().first() instanceof Interface)
    }
}
