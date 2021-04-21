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
package edu.montana.gsoc.msusel.pattern.gen.generators.pb

import edu.isu.isuese.datamodel.Accessibility
import edu.isu.isuese.datamodel.Parameter
import edu.isu.isuese.datamodel.RefType
import edu.isu.isuese.datamodel.Reference
import edu.isu.isuese.datamodel.Type
import edu.isu.isuese.datamodel.TypeRef
import edu.isu.isuese.datamodel.TypeRefType
import edu.isu.isuese.datamodel.UnknownType

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class ParamBuilder extends AbstractComponentBuilder {

    Parameter create() {
        if (!params.param)
            throw new IllegalArgumentException("createParam: param cannot be null")

        Type type = ctx.rbmlManager.getType(((edu.montana.gsoc.msusel.rbml.model.Parameter) params.param).type)
        TypeRef tr = createTypeRef(type)
        String name = ((edu.montana.gsoc.msusel.rbml.model.Parameter) params.param).variable

        Parameter.builder()
                .name(name)
                .type(tr)
                .create()
    }

    private String getParamName() {
        String name = ""
        Random rand = new Random()
        nums = rand.nextInt(3) + 1
        nums.each{ int entry ->
            String[] lines = MethodBuilder.class.getResourceAsStream("/edu/montana/gsoc/msusel/pattern/gen/classnames${entry}.txt").readLines()
            int ndx = rand.nextInt(lines.length)
            name += lines[ndx]
        }

        name
    }

    @Override
    TypeRef createDefaultTypeRef() {
        UnknownType type = UnknownType.builder()
                .name("String")
                .compKey("UT:String")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Reference ref = new Reference(type.getRefKey(), RefType.TYPE)

        TypeRef.builder()
                .type(TypeRefType.Type)
                .typeName(type.name)
                .typeFullName(type.getFullName())
                .ref(ref)
                .create()
    }
}
