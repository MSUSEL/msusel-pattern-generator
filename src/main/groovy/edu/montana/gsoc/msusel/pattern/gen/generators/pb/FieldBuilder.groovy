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

import edu.isu.isuese.datamodel.*
import edu.montana.gsoc.msusel.rbml.model.StructuralFeature

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class FieldBuilder extends AbstractComponentBuilder {

    /**
     * Constructs a new Field from the specification given by
     * the provided feature. If the provided feature has no provided
     * type a basic type such as String will be selected
     *
     * @return A new Field object based on the provided specification
     * @throws IllegalArgumentException when the provided feature specification is null
     */
    Field create() {
        if (!params.feature)
            throw new IllegalArgumentException("createField: feature cannot be null")
        if (!params.fieldName)
            throw new IllegalArgumentException("createField: fieldName cannot be null")
        if (!params.owner)
            throw new IllegalArgumentException("createField: owner cannot be null")

        StructuralFeature feature = (StructuralFeature) params.feature

        String name = params.fieldName
        Type type = ctx.rbmlManager.getType(feature.type)
        TypeRef tr = createTypeRef(type)
        Field field = Field.builder()
                .name(name)
                .accessibility(Accessibility.PRIVATE)
                .compKey(name)
                .type(tr)
                .create()
        if (((StructuralFeature) params.feature).isStatic)
            field.addModifier(Modifier.forName("STATIC"))

        params.owner.addMember(field)
        field.updateKey()
        field.save()
        ctx.rbmlManager.addMapping(feature, field)

        field
    }

    /**
     * @return A random field name
     */
    String getFieldName() {
        String name = ""
        Random rand = new Random()
        String[] lines = FieldBuilder.class.getResourceAsStream("/edu/montana/gsoc/msusel/pattern/gen/fieldnames.txt").readLines()
        int ndx = rand.nextInt(lines.length)
        name += lines[ndx].toLowerCase()

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
                .ref(ref)
                .create()
    }
}
