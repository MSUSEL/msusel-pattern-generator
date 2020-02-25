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
import edu.isu.isuese.datamodel.Namespace
import edu.isu.isuese.datamodel.Relation
import edu.isu.isuese.datamodel.RelationType
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.pattern.gen.GeneratorContext
import edu.montana.gsoc.msusel.rbml.io.SpecificationReader
import edu.montana.gsoc.msusel.rbml.model.ClassRole
import edu.montana.gsoc.msusel.rbml.model.Classifier
import edu.montana.gsoc.msusel.rbml.model.Generalization
import edu.montana.gsoc.msusel.rbml.model.GeneralizationHierarchy
import edu.montana.gsoc.msusel.rbml.model.Multiplicity
import edu.montana.gsoc.msusel.rbml.model.Relationship
import edu.montana.gsoc.msusel.rbml.model.Role
import edu.montana.gsoc.msusel.rbml.model.SPS
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.yaml.snakeyaml.Yaml

import static org.junit.Assert.fail

class RelationshipBuilderTest extends DBSpec {

    GeneratorContext ctx
    Namespace ns
    String yml = """\
SPS:
  name: State
  roles:
    - Class:
        name: A
        mult: '1..1'
    - Class:
        name: B
        mult: '1..1'
    - Class:
        name: C
        mult: '1..*'
    - Class:
        name: D
        mult: '1..1'
  relations:
    - Association:
        name: AB
        mult: '1..1'
        source:
          name: a
          type: A
          mult: '1..1'
        dest:
          name: b
          type: B
          mult: '1..1'
    - Association:
        name: BC
        mult: '1..1'
        source:
          name: b
          type: B
          mult: '1..1'
        dest:
          name: c
          type: C
          mult: '1..1'
    - Association:
        name: BD
        mult: '1..1'
        source:
          name: b
          type: B
          mult: '1..1'
        dest:
          name: d
          type: D
          mult: '1..1'
    - Association:
        name: CD
        mult: '1..1'
        source:
          name: c
          type: C
          mult: '1..1'
        dest:
          name: d
          type: D
          mult: '1..1'
"""
    SPS data

    @Before
    void setup() {
        ctx = GeneratorContext.getInstance()
        ctx.resetPatternBuilderComponents()
        ctx.setMaxBreadth(3)
        ctx.srcExt = ".java"
        ctx.srcPath = "src/main/java/"
        ns = Namespace.builder().name("test").nsKey("test").create()
        SpecificationReader reader = new SpecificationReader()
        Yaml yaml = new Yaml()
        def map = yaml.load(yml)
        reader.processSPS(map)
        data = reader.sps
    }

    @After
    void cleanup() {
    }

    @Test
    void "test processRole with gh root"() {
        Role gh = GeneralizationHierarchy.builder()
                .name("Test")
                .create()
        Role root = Classifier.builder().name("Test").create()
        Role a = Classifier.builder().name("A").create()
        Role b = ClassRole.builder().name("B").create()
        gh.root = root
        gh.children << a
        gh.children << b

        // when:
        ctx.relationBuilder.processRole(ns, gh, "Test")

        // then:
        the(ctx.relationBuilder.ghmap["Test"].roots.size()).shouldEqual(1)
    }

    @Test
    void "test processRole with gh leaf"() {
        Role gh = GeneralizationHierarchy.builder()
                .name("Test")
                .create()
        Role root = Classifier.builder().name("Test").create()
        Role a = Classifier.builder().name("A").create()
        Role b = ClassRole.builder().name("B").create()
        gh.root = root
        gh.children << a
        gh.children << b

        // when:
        ctx.relationBuilder.processRole(ns, gh, "B")

        // then:
        the(ctx.relationBuilder.ghmap["Test"].leaves.size()).shouldEqual(1)
    }

    @Test
    void "test processRole with classifier"() {
        // given:
        Role role = ClassRole.builder().name("A").mult(Multiplicity.fromString("1..1")).create()

        // when:
        ctx.relationBuilder.processRole(ns, role, null)

        // then:
        the(Class.findAll().size()).shouldEqual(1)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test processRole with null ok"() {
        // given:
        Role gh = null
        String port = "Test"

        // when:
        ctx.relationBuilder.processRole(ns, gh, port)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test processRole with gh null"() {
        // given:
        Role gh = GeneralizationHierarchy.builder()
                .name("Test")
                .create()
        String port = null

        // when:
        ctx.relationBuilder.processRole(ns, gh, port)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test processRole with gh empty"() {
        // given:
        Role gh = GeneralizationHierarchy.builder()
                .name("Test")
                .create()
        String port = ""

        // when:
        ctx.relationBuilder.processRole(ns, gh, port)
    }

    @Test
    void "test processRole with classifier null"() {
        // given:
        Role role = Classifier.builder()
                .name("Test")
                .create()
        String port = null

        // when:
        try {
            ctx.relationBuilder.processRole(ns, role, port)
        } catch (IllegalArgumentException ex) {
            println(ex.getMessage())
            fail()
        }
    }

    @Test
    void "test processRole with classifier empty"() {
        // given:
        Role role = Classifier.builder()
                .name("Test")
                .create()
        String port = ""

        // when:
        try {
            ctx.relationBuilder.processRole(ns, role, port)
        } catch (IllegalArgumentException ex) {
            fail()
        }
    }

    @Test
    void "test generateClassifier 1 to 1"() {
        // given:
        Classifier role = ClassRole.builder().name("Test").create()

        // when:
        ctx.relationBuilder.generateClassifier(ns, role)

        // then:
        the(Class.findAll().size()).shouldEqual(1)
    }

    @Test
    void "test generateClassifier 1 to many"() {
        // given:
        Classifier role = ClassRole.builder().mult(Multiplicity.fromString("1..*")).name("Test").create()

        // when:
        ctx.relationBuilder.generateClassifier(ns, role)

        // then:
        the(Class.findAll().size() <= 3).shouldBeTrue()
    }

    @Test
    void "test generateClassifier 1 to 2"() {
        // given:
        Classifier role = ClassRole.builder().mult(Multiplicity.fromString("1..2")).name("Test").create()

        // when:
        ctx.relationBuilder.generateClassifier(ns, role)

        // then:
        the(Class.findAll().size() <= 2).shouldBeTrue()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test generateClassifier null"() {
        // given:
        Classifier role = null

        // when:
        ctx.relationBuilder.generateClassifier(ns, role)
    }

    @Test
    void "test processGHRole roots"() {
        // given:
        Role gh = GeneralizationHierarchy.builder()
                .name("Test")
                .create()
        Role root = Classifier.builder().name("Test").create()
        Role a = Classifier.builder().name("A").create()
        Role b = ClassRole.builder().name("B").create()
        gh.root = root
        gh.children << a
        gh.children << b
        String port = "Test"

        // when:
        ctx.relationBuilder.processGHRole(ns, gh, port)

        // then:
        the(ctx.relationBuilder.ghmap["Test"].roots.size()).shouldEqual(1)
        println(ctx.relationBuilder.map[root][0])
        println(ctx.relationBuilder.ghmap["Test"].roots)
        the(ctx.relationBuilder.ghmap["Test"].roots).shouldContain(ctx.relationBuilder.map[root][0])
        the(ctx.relationBuilder.ghmap["Test"].leaves.isEmpty()).shouldBeTrue()
    }

    @Test
    void "test processGHRole leaves"() {
        // given:
        Role gh = GeneralizationHierarchy.builder()
                .name("Test")
                .create()
        Role root = Classifier.builder().name("Test").create()
        Role a = Classifier.builder().name("A").create()
        Role b = ClassRole.builder().name("B").create()
        gh.root = root
        gh.children << a
        gh.children << b
        String port = "B"

        // when:
        ctx.relationBuilder.processGHRole(ns, gh, port)

        // then:
        the(ctx.relationBuilder.ghmap["Test"].roots.isEmpty()).shouldBeTrue()
        the(ctx.relationBuilder.ghmap["Test"].leaves.size()).shouldEqual(1)
        println(ctx.relationBuilder.map[b][0])
        println(ctx.relationBuilder.ghmap["Test"].leaves)
        the(ctx.relationBuilder.ghmap["Test"].leaves).shouldContain(ctx.relationBuilder.map[b][0])
    }

    @Test(expected = IllegalArgumentException.class)
    void "test processGHRole null ok"() {
        // given:
        Role gh = null
        String port = "test"

        // when:
        ctx.relationBuilder.processGHRole(ns, gh, port)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test processGHRole ok null"() {
        // given:
        Role gh = GeneralizationHierarchy.builder()
                .name("Test")
                .create()
        String port = null

        // when:
        ctx.relationBuilder.processGHRole(ns, gh, port)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test processGHRole ok empty"() {
        // given:
        Role gh = GeneralizationHierarchy.builder()
                .name("Test")
                .create()
        String port = ""

        // when:
        ctx.relationBuilder.processGHRole(ns, gh, port)
    }

    @Test
    void "test selectAndCreateRelationship"() {
        // given:
        Role src = Classifier.builder().name("A").create()
        Type a = Class.builder().name("A").compKey("A").create()
        Role dest = Classifier.builder().name("B").create()
        Type b = Class.builder().name("B").compKey("B").create()
        ctx.relationBuilder.map[src] = [a]
        ctx.relationBuilder.map[dest] = [b]
        Relationship rel = new Generalization("test", Multiplicity.fromString("1..1"), dest, src)

        // when:
        ctx.relationBuilder.selectAndCreateRelationship(rel)

        // then:
        the(a.getGeneralizedBy().size()).shouldEqual(1)
        the(a.getGeneralizedBy().first()).shouldEqual(b)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test selectAndCreateRelationship null"() {
        // given:
        Relationship rel = null

        // when:
        ctx.relationBuilder.selectAndCreateRelationship(rel)
    }

    @Test
    void "test findGenHierarchyComponents leaf port"() {
        // given:
        GeneralizationHierarchy gh = GeneralizationHierarchy.builder().name("Test").create()
        Role root = Classifier.builder().name("Test").create()
        Role a = Classifier.builder().name("A").create()
        Role b = ClassRole.builder().name("B").create()
        gh.root = root
        gh.children << a
        gh.children << b
        String port = "A"

        // when:
        List<Role> roles = ctx.relationBuilder.findGenHierarchyComponents(gh, port)

        // then:
        the(roles).shouldContain(a)
        the(roles.size()).shouldEqual(1)
    }

    @Test
    void "test findGenHierarchyComponents unknown port"() {
        // given:
        GeneralizationHierarchy gh = GeneralizationHierarchy.builder().name("Test").create()
        Role root = Classifier.builder().name("Test").create()
        Role a = Classifier.builder().name("A").create()
        Role b = ClassRole.builder().name("B").create()
        gh.root = root
        gh.children << a
        gh.children << b
        String port = "Other"

        // when:
        List<Role> roles = ctx.relationBuilder.findGenHierarchyComponents(gh, port)

        // then:
        the(roles.isEmpty()).shouldBeTrue()
    }

    @Test
    void "test findGenHierarchyComponents root port"() {
        // given:
        GeneralizationHierarchy gh = GeneralizationHierarchy.builder().name("Test").create()
        Role root = Classifier.builder().name("Test").create()
        Role a = Classifier.builder().name("A").create()
        Role b = ClassRole.builder().name("B").create()
        gh.root = root
        gh.children << a
        gh.children << b
        String port = "Test"

        // when:
        List<Role> roles = ctx.relationBuilder.findGenHierarchyComponents(gh, port)

        // then:
        the(roles).shouldContain(root)
        the(roles.size()).shouldEqual(1)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test findGenHierarchyComponents null ok"() {
        // given:
        GeneralizationHierarchy gh = null
        String port = "port"

        // when:
        ctx.relationBuilder.findGenHierarchyComponents(gh, port)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test findGenHierarchyComponents ok null"() {
        // given:
        GeneralizationHierarchy gh = GeneralizationHierarchy.builder().name("Test").create()
        String port = null

        // when:
        ctx.relationBuilder.findGenHierarchyComponents(gh, port)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test findGenHierarchyComponents ok empty"() {
        // given:
        GeneralizationHierarchy gh = GeneralizationHierarchy.builder().name("Test").create()
        String port = ""

        // when:
        ctx.relationBuilder.findGenHierarchyComponents(gh, port)
    }

    @Test
    void "test prepareAndCreateRelationship"() {
        Role src = Classifier.builder().name("A").create()
        Type a = Class.builder().name("A").compKey("A").create()
        Role dest = Classifier.builder().name("B").create()
        Type b = Class.builder().name("B").compKey("B").create()
        ctx.relationBuilder.map[src] = [a]
        ctx.relationBuilder.map[dest] = [b]
        String srcPort = "source"
        String destPort = "dest"
        RelationType type = RelationType.ASSOCIATION

        // when:
        ctx.relationBuilder.prepareAndCreateRelationship(src, dest, srcPort, destPort, type)

        the(Relation.findAll().size()).shouldEqual(1)
        Relation rel = (Relation) Relation.findAll().first()
        the(rel.getType()).shouldEqual(RelationType.ASSOCIATION)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test prepareAndCreateRelationship null ok ok ok ok"() {
        // given:
        Role src = null
        Role dest = Classifier.builder().name("B").create()
        String srcPort = "source"
        String destPort = "dest"
        RelationType type = RelationType.ASSOCIATION

        // when:
        ctx.relationBuilder.prepareAndCreateRelationship(src, dest, srcPort, destPort, type)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test prepareAndCreateRelationship ok null ok ok ok"() {
        // given:
        Role src = Classifier.builder().name("A").create()
        Role dest = null
        String srcPort = "source"
        String destPort = "dest"
        RelationType type = RelationType.ASSOCIATION

        // when:
        ctx.relationBuilder.prepareAndCreateRelationship(src, dest, srcPort, destPort, type)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test prepareAndCreateRelationship ok ok null ok ok"() {
        // given:
        Role src = GeneralizationHierarchy.builder().name("A").create()
        Role dest = Classifier.builder().name("B").create()
        String srcPort = null
        String destPort = "dest"
        RelationType type = RelationType.ASSOCIATION

        // when:
        ctx.relationBuilder.prepareAndCreateRelationship(src, dest, srcPort, destPort, type)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test prepareAndCreateRelationship ok ok empty ok ok"() {
        // given:
        Role src = GeneralizationHierarchy.builder().name("A").create()
        Role dest = Classifier.builder().name("B").create()
        String srcPort = ""
        String destPort = "dest"
        RelationType type = RelationType.ASSOCIATION

        // when:
        ctx.relationBuilder.prepareAndCreateRelationship(src, dest, srcPort, destPort, type)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test prepareAndCreateRelationship ok ok ok null ok"() {
        // given:
        Role src = Classifier.builder().name("A").create()
        Role dest = GeneralizationHierarchy.builder().name("B").create()
        String srcPort = "source"
        String destPort = null
        RelationType type = RelationType.ASSOCIATION

        // when:
        ctx.relationBuilder.prepareAndCreateRelationship(src, dest, srcPort, destPort, type)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test prepareAndCreateRelationship ok ok ok empty ok"() {
        // given:
        Role src = Classifier.builder().name("A").create()
        Role dest = GeneralizationHierarchy.builder().name("B").create()
        String srcPort = "source"
        String destPort = ""
        RelationType type = RelationType.ASSOCIATION

        // when:
        ctx.relationBuilder.prepareAndCreateRelationship(src, dest, srcPort, destPort, type)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test prepareAndCreateRelationship ok ok ok ok null"() {
        // given:
        Role src = Classifier.builder().name("A").create()
        Role dest = Classifier.builder().name("B").create()
        String srcPort = "source"
        String destPort = "dest"
        RelationType type = null

        // when:
        ctx.relationBuilder.prepareAndCreateRelationship(src, dest, srcPort, destPort, type)
    }

    @Test
    void "test createRelationship generalization"() {
        // given:
        Type a = Class.builder()
                .name("A")
                .compKey("A")
                .create()
        Type b = Class.builder()
                .name("B")
                .compKey("B")
                .create()
        RelationType type = RelationType.GENERALIZATION

        // when:
        ctx.relationBuilder.createRelationship(a, b, type)

        // then:
        the(a.getGeneralizedBy().size()).shouldEqual(1)
        the(b.getGeneralizes().size()).shouldEqual(1)
    }

    @Test
    void "test createRelationship association"() {
        // given:
        Type a = Class.builder()
                .name("A")
                .compKey("A")
                .create()
        Type b = Class.builder()
                .name("B")
                .compKey("B")
                .create()
        RelationType type = RelationType.ASSOCIATION

        // when:
        ctx.relationBuilder.createRelationship(a, b, type)

        // then:
        the(a.getAssociatedTo().size()).shouldEqual(1)
        the(b.getAssociatedFrom().size()).shouldEqual(1)
    }

    @Test
    void "test createRelationship realization"() {
        // given:
        Type a = Class.builder()
                .name("A")
                .compKey("A")
                .create()
        Type b = Class.builder()
                .name("B")
                .compKey("B")
                .create()
        RelationType type = RelationType.REALIZATION

        // when:
        ctx.relationBuilder.createRelationship(a, b, type)

        // then:
        the(a.getRealizes().size()).shouldEqual(1)
        the(b.getRealizedBy().size()).shouldEqual(1)
    }

    @Test
    void "test createRelationship use"() {
        // given:
        Type a = Class.builder()
                .name("A")
                .compKey("A")
                .create()
        Type b = Class.builder()
                .name("B")
                .compKey("B")
                .create()
        RelationType type = RelationType.USE

        // when:
        ctx.relationBuilder.createRelationship(a, b, type)

        // then:
        the(a.getUseTo().size()).shouldEqual(1)
        the(b.getUseFrom().size()).shouldEqual(1)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test createRelationship null ok ok"() {
        // given:
        Type a = null
        Type b = Class.builder()
                .name("B")
                .compKey("B")
                .create()
        RelationType type = RelationType.ASSOCIATION

        // when:
        ctx.relationBuilder.createRelationship(a, b, type)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test createRelationship ok null ok"() {
        // given:
        Type a = Class.builder()
                .name("A")
                .compKey("A")
                .create()
        Type b = null
        RelationType type = RelationType.ASSOCIATION

        // when:
        ctx.relationBuilder.createRelationship(a, b, type)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test createRelationship ok ok null"() {
        // given:
        Type a = Class.builder()
                .name("A")
                .compKey("A")
                .create()
        Type b = Class.builder()
                .name("B")
                .compKey("B")
                .create()
        RelationType type = null

        // when:
        ctx.relationBuilder.createRelationship(a, b, type)
    }
}
