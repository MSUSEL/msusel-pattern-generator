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

import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.Type
import edu.isu.isuese.datamodel.TypeRef
import edu.isu.isuese.datamodel.Accessibility
import edu.isu.isuese.datamodel.Modifier
import edu.isu.isuese.datamodel.Parameter
import edu.montana.gsoc.msusel.rbml.model.BehavioralFeature

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class MethodBuilder extends AbstractComponentBuilder {

    Method create() {
        if (!params.feature)
            throw new IllegalArgumentException("createMethod: feature cannot be null")
        if (!params.methodName)
            throw new IllegalArgumentException("createMethod: methodName cannot be null")
        if (!params.owner)
            throw new IllegalArgumentException("createMethod: owner cannot be null")

        BehavioralFeature feature = (BehavioralFeature) params.feature

        String name = params.methodName
        Type type = ctx.rbmlManager.getType(feature.type)
        TypeRef tr = createTypeRef(type)

        Method method = Method.builder()
                .name(name)
                .compKey(name)
                .accessibility(Accessibility.PUBLIC)
                .type(tr)
                .create()
        if (((BehavioralFeature) params.feature).isStatic) {
            method.addModifier(Modifier.forName("STATIC"))
        }
        if (((BehavioralFeature) params.feature).isAbstract) {
            method.addModifier(Modifier.forName("ABSTRACT"))
        }
        ((BehavioralFeature) params.feature).params.each {
            ctx.paramBuilder.init(param: it)
            method.addParameter((Parameter) ctx.paramBuilder.create())
        }
        ((Type) params.owner).addMember(method)
        method.updateKey()
        method.save()
        ctx.rbmlManager.addMapping(feature, method)
        method
    }

    @Override
    TypeRef createDefaultTypeRef() {
        return TypeRef.createPrimitiveTypeRef("void")
    }

    String getMethodName() {
        String name = ""
        Random rand = new Random()
        int nums = 2
        boolean isFirst = true
        (1..2).each { int entry ->
            String[] lines = MethodBuilder.class.getResourceAsStream("/edu/montana/gsoc/msusel/pattern/gen/methodnames${entry}.txt").readLines()
            int ndx = rand.nextInt(lines.length)
            if (isFirst) {
                name += lines[ndx].toLowerCase()
                isFirst = false
            } else {
                name += lines[ndx]
            }
        }

        name
    }
}
