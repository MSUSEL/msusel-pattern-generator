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
import edu.isu.isuese.datamodel.Class
import edu.isu.isuese.datamodel.Namespace
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.pattern.gen.GeneratorContext
import edu.montana.gsoc.msusel.rbml.model.BehavioralFeature
import edu.montana.gsoc.msusel.rbml.model.Classifier
import edu.montana.gsoc.msusel.rbml.model.Multiplicity
import edu.montana.gsoc.msusel.rbml.model.StructuralFeature
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class ClassifierBuilderTest extends DBSpec {

    GeneratorContext ctx

    @Before
    void setup() {
        ctx = GeneratorContext.getInstance()
        ctx.resetPatternBuilderComponents()
    }

    @After
    void cleanup() {
    }

    @Test
    void "test createClassifier"() {
        // given:
        Classifier clazz = Classifier.builder()
                .name("Classifier")
                .mult(Multiplicity.fromString("1..1"))
                .create()
        Namespace ns = Namespace.builder().nsKey("ns").name("ns").create()

        // when:
        ctx.clsBuilder.init(classifier: clazz, ns: ns)
        ctx.clsBuilder.create()

        // then:
        the(ctx.rbmlManager.getTypes(clazz)).shouldNotBeNull()
        the(ctx.rbmlManager.getTypes(clazz).size()).shouldEqual(1)
        the(ctx.rbmlManager.getType(clazz)).shouldNotBeNull()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test createClassifier with null"() {
        // given:
        Classifier clazz = null
        Namespace ns = Namespace.builder().nsKey("ns").name("ns").create()

        // when:
        ctx.clsBuilder.init(classifier: clazz, ns: ns)
        ctx.clsBuilder.create()
    }

    @Test
    void "test createFeatures"() {
        // given:
        Classifier role = Classifier.builder()
                .name("Test")
                .create()

        Type type = Class.builder()
                .name("Class")
                .compKey("Class")
                .accessibility(Accessibility.PUBLIC)
                .create()

        Namespace ns = Namespace.builder().nsKey("ns").name("ns").create()

        ctx.rbmlManager.addMapping(role, type)

        Classifier clazz = Classifier.builder()
                .name("Classifier")
                .mult(Multiplicity.fromString("1..1"))
                .create()
        StructuralFeature feature = StructuralFeature.builder()
                .name("feature")
                .isStatic(false)
                .mult(Multiplicity.fromString("1..1"))
                .type(role)
                .create()
        BehavioralFeature feature2 = BehavioralFeature.builder()
                .name("method")
                .type(role)
                .mult(Multiplicity.fromString("1..1"))
                .isStatic(false)
                .isAbstract(false)
                .create()
        clazz.behFeats << feature2
        clazz.structFeats << feature

        ctx.clsBuilder.init(classifier: clazz, ns: ns)
        ctx.clsBuilder.create()

        // when:
        ctx.clsBuilder.createFeatures(clazz)

        // then:
        the(ctx.rbmlManager.getType(clazz).getFields().size()).shouldEqual(1)
        the(ctx.rbmlManager.getType(clazz).getMethods().size()).shouldEqual(1)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test createFeatures with null"() {
        // given:
        Classifier clazz = null

        // when:
        ctx.clsBuilder.createFeatures(clazz)
    }
}
