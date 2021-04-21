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

import edu.isu.isuese.datamodel.*
import edu.montana.gsoc.msusel.pattern.gen.GeneratorContext
import edu.montana.gsoc.msusel.rbml.model.StructuralFeature
import org.javalite.activejdbc.test.DBSpec
import org.junit.Before
import org.junit.Test

class JavaFieldGeneratorTest extends DBSpec {

    GeneratorContext ctx
    TypeRef typeRef
    Type type

    @Before
    void setUp() {
        ctx = GeneratorContext.getInstance()
        ctx.plugin = new JavaLanguageProvider()
        ctx.resetComponentGenerators()
        ctx.resetPatternBuilderComponents()

        type = Class.builder()
                .name("Type")
                .accessibility(Accessibility.PUBLIC)
                .compKey("type")
                .create()

        Reference ref = Reference.builder()
                .refType(RefType.FIELD)
                .refKey(type.getRefKey())
                .create()
        typeRef = TypeRef.builder()
                .type(TypeRefType.Type)
                .typeName(type.name)
                .typeFullName(type.getFullName())
                .ref(ref)
                .create()
    }

    @Test
    void testGenerate_public() {
        Field publicAccess = Field.builder()
                .accessibility(Accessibility.PUBLIC)
                .compKey("test-public")
                .name("test")
                .create()
        publicAccess.setType(typeRef)
        publicAccess.saveIt()

        ctx.fieldGen.init(field: publicAccess)
        String obsData = ctx.fieldGen.generate()
        String expData = "public Type test;"

        the(expData).shouldEqual(obsData)
    }

    @Test
    void testGenerate_default() {
        Field defaultAccess = Field.builder()
                .accessibility(Accessibility.DEFAULT)
                .compKey("test-default")
                .name("test")
                .create()
        defaultAccess.setType(typeRef)
        defaultAccess.saveIt()

        ctx.fieldGen.init(field: defaultAccess)
        String obsData = ctx.fieldGen.generate()
        String expData = "Type test;"

        the(obsData).shouldEqual(expData)
    }

    @Test
    void testGenerate_protected() {
        Field protectedAccess = Field.builder()
                .accessibility(Accessibility.PROTECTED)
                .compKey("test-protected")
                .name("test")
                .create()
        protectedAccess.setType(typeRef)
        protectedAccess.saveIt()

        ctx.fieldGen.init(field: protectedAccess)
        String obsData = ctx.fieldGen.generate()
        String expData = "protected Type test;"

        the(obsData).shouldEqual(expData)
    }

    @Test
    void testGenerate_private() {
        Field privateAccess = Field.builder()
                .accessibility(Accessibility.PRIVATE)
                .compKey("test-private")
                .name("test")
                .create()
        privateAccess.setType(typeRef)
        privateAccess.saveIt()

        ctx.fieldGen.init(field: privateAccess)
        String obsData = ctx.fieldGen.generate()
        String expData = "private Type test;"

        the(expData).shouldEqual(obsData)
    }

    @Test
    void testGenerate_volatile() {
        Field volatileModifier = Field.builder()
                .accessibility(Accessibility.PUBLIC)
                .compKey("test-volatile")
                .name("test")
                .create()
        volatileModifier.addModifier(Modifier.forName("volatile"))
        volatileModifier.setType(typeRef)
        volatileModifier.saveIt()

        ctx.fieldGen.init(field: volatileModifier)
        String obsData = ctx.fieldGen.generate()
        String expData = "public volatile Type test;"

        the(expData).shouldEqual(obsData)
    }

    @Test
    void testGenerate_transient() {
        Field transientModifier = Field.builder()
                .accessibility(Accessibility.PUBLIC)
                .compKey("test-transient")
                .name("test")
                .create()
        transientModifier.addModifier(Modifier.forName("transient"))
        transientModifier.setType(typeRef)
        transientModifier.saveIt()

        ctx.fieldGen.init(field: transientModifier)
        String obsData = ctx.fieldGen.generate()
        String expData = "public transient Type test;"

        the(expData).shouldEqual(obsData)
    }

    @Test
    void testGenerate_final() {
        Field finalModifier = Field.builder()
                .accessibility(Accessibility.PUBLIC)
                .compKey("test-final")
                .name("test")
                .create()
        finalModifier.addModifier(Modifier.forName("final"))
        finalModifier.setType(typeRef)
        finalModifier.saveIt()

        ctx.fieldGen.init(field: finalModifier)
        String obsData = ctx.fieldGen.generate()
        String expData = "public final Type test;"

        the(expData).shouldEqual(obsData)
    }

    @Test
    void testGenerate_static() {
        Field staticModifier = Field.builder()
                .accessibility(Accessibility.PUBLIC)
                .compKey("test-static")
                .name("test")
                .create()
        staticModifier.addModifier(Modifier.forName("static"))
        staticModifier.setType(typeRef)
        staticModifier.saveIt()

        ctx.fieldGen.init(field: staticModifier)
        String obsData = ctx.fieldGen.generate()
        String expData = "public static Type test;"

        the(expData).shouldEqual(obsData)
    }

    @Test
    void testGenerate_modifiers() {
        Field modifiers = Field.builder()
                .accessibility(Accessibility.PUBLIC)
                .compKey("test-modifiers")
                .name("test")
                .create()
        modifiers.addModifier(Modifier.forName("static"))
        modifiers.addModifier(Modifier.forName("final"))
        modifiers.setType(typeRef)
        modifiers.saveIt()

        ctx.fieldGen.init(field: modifiers)
        String obsData = ctx.fieldGen.generate()
        String expData = "public static final Type test;"

        the(expData).shouldEqual(obsData)
    }
    
    @Test(expected = IllegalArgumentException.class)
    void testGenerate_null() {
        ctx.fieldGen.init(field: null)
        String obsData = ctx.fieldGen.generate()
    }

//    @Test
//    void "test generate with cuerole"() {
//        Field publicAccess = Field.builder()
//                .accessibility(Accessibility.PUBLIC)
//                .compKey("test-public")
//                .name("test")
//                .create()
//        publicAccess.setType(typeRef)
//        publicAccess.saveIt()
//
//        edu.montana.gsoc.msusel.rbml.model.Role role = StructuralFeature.builder()
//                .name("test")
//                .create()
//        ctx.rbmlManager.addMapping(role, publicAccess)
//
//        CueRole cueRole = new CueRole()
//        cueRole.disregard = false
//        cueRole.definition = "Testing"
//
//        Cue cue = new Cue()
//        cue.name = "Test"
//        cue.roles = ["test": cueRole]
//        ctx.cue = cue
//
//        ctx.fieldGen.init(field: publicAccess, type: type)
//        String obsData = ctx.fieldGen.generate()
//        String expData = "Testing"
//
//        the(expData).shouldEqual(obsData)
//    }

//    @Test
//    void "test generate with cuerole with disregard flag"() {
//        Field publicAccess = Field.builder()
//                .accessibility(Accessibility.PUBLIC)
//                .compKey("test-public")
//                .name("test")
//                .create()
//        publicAccess.setType(typeRef)
//        publicAccess.saveIt()
//
//        edu.montana.gsoc.msusel.rbml.model.Role role = StructuralFeature.builder()
//                .name("test")
//                .create()
//        ctx.rbmlManager.addMapping(role, publicAccess)
//
//        CueRole cueRole = new CueRole()
//        cueRole.disregard = true
//        cueRole.definition = "Testing"
//
//        Cue cue = new Cue()
//        cue.name = "Test"
//        cue.roles = ["test": cueRole]
//        ctx.cue = cue
//
//        ctx.fieldGen.init(field: publicAccess, type: type)
//        String obsData = ctx.fieldGen.generate()
//        String expData = ""
//
//        the(expData).shouldEqual(obsData)
//    }
}
