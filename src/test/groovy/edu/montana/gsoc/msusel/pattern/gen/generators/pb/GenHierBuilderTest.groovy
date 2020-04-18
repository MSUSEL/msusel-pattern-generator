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

import edu.isu.isuese.datamodel.Class
import edu.isu.isuese.datamodel.Interface
import edu.isu.isuese.datamodel.Namespace
import edu.isu.isuese.datamodel.Relation
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.pattern.gen.GeneratorContext
import edu.montana.gsoc.msusel.pattern.gen.generators.pb.tree.Node
import edu.montana.gsoc.msusel.pattern.gen.generators.pb.tree.Tree
import edu.montana.gsoc.msusel.rbml.model.ClassRole
import edu.montana.gsoc.msusel.rbml.model.Classifier
import edu.montana.gsoc.msusel.rbml.model.GeneralizationHierarchy
import edu.montana.gsoc.msusel.rbml.model.Multiplicity
import edu.montana.gsoc.msusel.rbml.model.Role
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class GenHierBuilderTest extends DBSpec {

    GeneratorContext ctx

    @Before
    void setup() {
        ctx = GeneratorContext.instance
        ctx.resetPatternBuilderComponents()
    }

    @After
    void teardown() {
    }

    @Test
    void "test generalizes"() {
        // given:
        Type parent = Class.builder().name("Parent").compKey("Parent").create()
        Type child = Class.builder().name("Child").compKey("Child").create()

        // when:
        ctx.ghBuilder.generalizes(parent, child)

        // then
        the(parent.getGeneralizes().first()).shouldEqual(child)
        the(child.getGeneralizedBy().first()).shouldEqual(parent)
    }

    @Test
    void "test generalizes parent interface child class"() {
        // given:
        Type parent = Interface.builder().name("Parent").compKey("Parent").create()
        Type child = Class.builder().name("Child").compKey("Child").create()

        // when:
        ctx.ghBuilder.generalizes(parent, child)

        // then
        the(parent.getRealizedBy().first()).shouldEqual(child)
        the(child.getRealizes().first()).shouldEqual(parent)
    }

    @Test
    void "test generalizes parent interface child interface"() {
        // given:
        Type parent = Interface.builder().name("Parent").compKey("Parent").create()
        Type child = Interface.builder().name("Child").compKey("Child").create()

        // when:
        ctx.ghBuilder.generalizes(parent, child)

        // then
        the(parent.getGeneralizes().first()).shouldEqual(child)
        the(child.getGeneralizedBy().first()).shouldEqual(parent)
    }

    @Test
    void "test generalizes parent class child interface"() {
        // given:
        Type parent = Class.builder().name("Parent").compKey("Parent").create()
        Type child = Interface.builder().name("Child").compKey("Child").create()

        // when:
        ctx.ghBuilder.generalizes(parent, child)

        // then
        the(Relation.findAll().isEmpty()).shouldBeTrue()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test generalizes null ok"() {
        // given:
        Type parent = null
        Type child = Class.builder().name("Child").compKey("Child").create()

        // when:
        ctx.ghBuilder.generalizes(parent, child)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test generalizes ok null"() {
        // given:
        Type parent = Class.builder().name("Parent").compKey("Parent").create()
        Type child = null

        // when:
        ctx.ghBuilder.generalizes(parent, child)
    }

    @Test
    void "test createClassifier"() {
        // given:
        Namespace ns = Namespace.builder().name("Test").nsKey("Test").create()
        Classifier role = ClassRole.builder().name("Test").create()
        Node n = new Node(value: "Test")

        // when:
        ctx.ghBuilder.createClassifier(ns, role, n)

        // then:
        the(n.type).shouldNotBeNull()
        the(n.type).shouldBeType(Class)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test createClassifier null ok ok"() {
        // given:
        Namespace ns = null
        Classifier role = Classifier.builder().name("Test").create()
        Node n = new Node(value: "Test")

        // when:
        ctx.ghBuilder.createClassifier(ns, role, n)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test createClassifier ok null ok"() {
        // given:
        Namespace ns = Namespace.builder().name("Test").nsKey("Test").create()
        Classifier role = null
        Node n = new Node(value: "Test")

        // when:
        ctx.ghBuilder.createClassifier(ns, role, n)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test createClassifier ok ok null"() {
        // given:
        Namespace ns = Namespace.builder().name("Test").nsKey("Test").create()
        Classifier role = Classifier.builder().name("Test").create()
        Node n = null

        // when:
        ctx.ghBuilder.createClassifier(ns, role, n)
    }

    @Test
    void "test getClassifier"() {
        // given:
        GeneralizationHierarchy gh = GeneralizationHierarchy.builder().name("Test").create()
        Classifier root = Classifier.builder().name("Test").mult(Multiplicity.fromString("1..1")).create()
        gh.setRoot(root)
        List<Role> roles = [
                ClassRole.builder().name("ConTest").mult(Multiplicity.fromString("1..*")).create(),
                Classifier.builder().name("AbsTest").mult(Multiplicity.fromString("1..1")).create()
        ]
        gh.setChildren(roles)

        // when:
        def first = ctx.ghBuilder.getClassifier(gh, "Test")
        def second = ctx.ghBuilder.getClassifier(gh, "ConTest")
        def third = ctx.ghBuilder.getClassifier(gh, "AbsTest")
        def fourth = ctx.ghBuilder.getClassifier(gh, "Other")

        // then:
        the(first).shouldEqual(root)
        the(second).shouldEqual(roles[0])
        the(third).shouldEqual(roles[1])
        the(fourth).shouldBeNull()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test getClassifier null ok"() {
        // given:
        GeneralizationHierarchy gh = null
        String name = "Test"

        // when:
        ctx.ghBuilder.getClassifier(gh, name)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test getClassifier ok null"() {
        // given:
        GeneralizationHierarchy gh = GeneralizationHierarchy.builder().name("Test").create()
        String name = null

        // when:
        ctx.ghBuilder.getClassifier(gh, name)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test getClassifier ok empty"() {
        // given:
        GeneralizationHierarchy gh = GeneralizationHierarchy.builder().name("Test").create()
        String name = ""

        // when:
        ctx.ghBuilder.getClassifier(gh, name)
    }

    @Test
    void "test populateTree"() {
        // given:
        GeneralizationHierarchy gh = GeneralizationHierarchy.builder().name("Test").create()
        gh.setRoot(Classifier.builder().name("Test").mult(Multiplicity.fromString("1..1")).create())
        def abschild = Classifier.builder().name("AbsTest").mult(Multiplicity.fromString("1..1")).create()
        abschild.setAbstrct(true)
        List<Role> roles = [
                ClassRole.builder().name("ConTest").mult(Multiplicity.fromString("1..*")).create(),
                abschild
        ]
        gh.setChildren(roles)
        Namespace ns = Namespace.builder().name("Test").nsKey("Test").create()
        Tree tree = new Tree()
        Node root = new Node(value: "AbsTest")
        Node child1 = root.addChild("ConTest")
        Node child2 = root.addChild("AbsTest")
        Node child3 = child2.addChild("ConTest")
        Node child4 = child2.addChild("ConTest")
        tree.setRoot(root)

        // when:
        ctx.ghBuilder.populateTree(gh, ns, tree)

        // then:
        the(Interface.findAll().size()).shouldEqual(0)
        the(Class.findAll().size()).shouldEqual(5)
        the(Relation.findAll().size()).shouldEqual(4)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test populateTree null ok ok"() {
        // given:
        GeneralizationHierarchy gh = null
        Namespace ns = Namespace.builder().name("Test").nsKey("Test").create()
        Tree tree = new Tree()

        // when:
        ctx.ghBuilder.populateTree(gh, ns, tree)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test populateTree ok null ok"() {
        // given:
        GeneralizationHierarchy gh = GeneralizationHierarchy.builder().name("Test").create()
        Namespace ns = null
        Tree tree = new Tree()

        // when:
        ctx.ghBuilder.populateTree(gh, ns, tree)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test populateTree ok ok null"() {
        // given:
        GeneralizationHierarchy gh = GeneralizationHierarchy.builder().name("Test").create()
        Namespace ns = Namespace.builder().name("Test").nsKey("Test").create()
        Tree tree = null

        // when:
        ctx.ghBuilder.populateTree(gh, ns, tree)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test createGenHierarchy null ok ok"() {
        // given:
        GeneralizationHierarchy gh = GeneralizationHierarchy.builder().name("Test").create()
        Namespace ns = null
        Map<String, Map<String, List<Type>>> ghmap = ["Test": [:]]

        // when:
        ctx.ghBuilder.init(gh: gh, ns: ns)
        ctx.ghBuilder.ghmap = ghmap
        ctx.ghBuilder.create()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test createGenHierarchy ok null ok"() {
        // given:
        GeneralizationHierarchy gh = null
        Namespace ns = Namespace.builder().name("Test").nsKey("Test").create()
        Map<String, Map<String, List<Type>>> ghmap = ["Test": [:]]

        // when:
        ctx.ghBuilder.init(gh: gh, ns: ns)
        ctx.ghBuilder.ghmap = ghmap
        ctx.ghBuilder.create()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test createGenHierarchy ok ok null"() {
        // given:
        GeneralizationHierarchy gh = GeneralizationHierarchy.builder().name("Test").create()
        Namespace ns = Namespace.builder().name("Test").nsKey("Test").create()
        Map<String, Map<String, List<Type>>> ghmap = null

        // when:
        ctx.ghBuilder.init(gh: gh, ns: ns)
        ctx.ghBuilder.ghmap = ghmap
        ctx.ghBuilder.create()
    }
}
