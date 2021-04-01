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
import edu.montana.gsoc.msusel.pattern.gen.cue.Cue
import edu.montana.gsoc.msusel.pattern.gen.cue.CueManager
import edu.montana.gsoc.msusel.pattern.gen.generators.FieldGenerator
import edu.montana.gsoc.msusel.pattern.gen.logging.LoggerInit
import groovy.util.logging.Log

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log
class JavaFieldGenerator extends FieldGenerator {

    JavaFieldGenerator() {
        LoggerInit.init(log)
    }

    @Override
    String generate() {
        if (!params.field)
            throw new IllegalArgumentException("Field cannot be null")

        Field field = (Field) params.field

//        String roleName = findRole(field)?.name
//        Cue cue = CueManager.getInstance().getCurrent()
//        if (roleName && cue.hasCueForRole(roleName, field))
//            return ""

        String retVal = ""

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

        retVal
    }

    private boolean defaultAccessiblity(Field field) {
        field.getAccessibility().toString().toLowerCase() == ""
    }

    private boolean parentIsNotNullOrInterface(Field field) {
        !field.getParentTypes() || !(field.getParentTypes().first() instanceof Interface)
    }
}
