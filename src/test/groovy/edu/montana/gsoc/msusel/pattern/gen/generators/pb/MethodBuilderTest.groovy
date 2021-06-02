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
import edu.montana.gsoc.msusel.pattern.gen.GeneratorContext
import edu.montana.gsoc.msusel.rbml.model.BehavioralFeature
import edu.montana.gsoc.msusel.rbml.model.Classifier
import edu.montana.gsoc.msusel.rbml.model.Multiplicity
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class MethodBuilderTest extends DBSpec {

    GeneratorContext ctx
    Classifier role
    Type type

    @Before
    void setUp() throws Exception {
        ctx = GeneratorContext.getInstance()
        ctx.resetPatternBuilderComponents()

        role = Classifier.builder()
                .name("Test")
                .create()

        type = Type.builder().type(Type.CLASS)
                .name("Class")
                .compKey("Class")
                .accessibility(Accessibility.PUBLIC)
                .create()

        ctx.rbmlManager.addMapping(role, type)
    }

    @After
    void tearDown() throws Exception {
    }

    @Test
    void "createTypeRef with known type"() {
        // given
        type

        // when
        TypeRef tr = ctx.methBuilder.createTypeRef(type)

        // then
        the(tr.getTypeName()).shouldEqual("Class")
    }

    @Test
    void "createTypeRef with null type"() {
        // given
        type = null

        // when
        TypeRef tr = ctx.methBuilder.createTypeRef(type)

        // then
        the(tr.getTypeName()).shouldEqual("void")
    }

    @Test
    void "test createMethod"() {
        // given:
        BehavioralFeature feature = BehavioralFeature.builder()
                .name("method")
                .type(role)
                .mult(Multiplicity.fromString("1..1"))
                .isStatic(false)
                .isAbstract(false)
                .create()

        // when:
        ctx.methBuilder.init(feature: feature, methodName: "test", owner: type)
        Method method = ctx.methBuilder.create()

        // then:
        the(method.type.typeName).shouldEqual("Class")
        the(method.name).shouldBeEqual("test")
    }

    @Test
    void "test create static method"() {
        // given:
        BehavioralFeature feature = BehavioralFeature.builder()
                .name("method")
                .type(role)
                .mult(Multiplicity.fromString("1..1"))
                .isStatic(true)
                .isAbstract(false)
                .create()

        // when:
        ctx.methBuilder.init(feature: feature, methodName: "test", owner: type)
        Method method = ctx.methBuilder.create()

        // then:
        the(method.hasModifier("STATIC")).shouldBeTrue()
        the(method.name).shouldBeEqual("test")
    }

    @Test
    void "test create abstract method"() {
        // given
        BehavioralFeature feature = BehavioralFeature.builder()
                .name("method")
                .type(role)
                .mult(Multiplicity.fromString("1..1"))
                .isStatic(false)
                .isAbstract(true)
                .create()

        // when:
        ctx.methBuilder.init(feature: feature, methodName: "test", owner: type)
        Method method = ctx.methBuilder.create()

        // then:
        the(method.isAbstract()).shouldBeTrue()
        the(method.name).shouldBeEqual("test")
    }

    @Test
    void "test create method with empty type"() {
        // given:
        BehavioralFeature feature = BehavioralFeature.builder()
                .name("method")
                .type(null)
                .mult(Multiplicity.fromString("1..1"))
                .isStatic(false)
                .isAbstract(false)
                .create()

        // when:
        ctx.methBuilder.init(feature: feature, methodName: "test", owner: type)
        Method method = ctx.methBuilder.create()

        // then:
        the(method.type.typeName).shouldEqual("void")
        the(method.name).shouldBeEqual("test")
    }

    @Test(expected = IllegalArgumentException.class)
    void "test create a null method"() {
        // given:
        BehavioralFeature feature = null

        // when:
        ctx.methBuilder.init(feature: feature)
        ctx.methBuilder.create()
    }

    @Test
    void createDefaultTypeRef() {
        // when
        TypeRef tr = ctx.methBuilder.createDefaultTypeRef()

        // then
        the(tr.getTypeName()).shouldEqual("void")
    }
}