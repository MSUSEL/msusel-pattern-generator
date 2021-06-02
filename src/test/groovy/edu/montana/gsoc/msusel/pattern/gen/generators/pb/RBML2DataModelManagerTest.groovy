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


import edu.isu.isuese.datamodel.Field
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.rbml.model.Classifier
import edu.montana.gsoc.msusel.rbml.model.Role
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class RBML2DataModelManagerTest extends DBSpec {

    RBML2DataModelManager fixture
    Role role
    Type type

    @Before
    void setUp() throws Exception {
        fixture = new RBML2DataModelManager()

        role = Classifier.builder()
                .name("Test")
                .create()

        type = Type.builder().type(Type.CLASS)
                .name("Class")
                .compKey("Class")
                .create()

        Map<Type, Role> roleMapping = [:]
        roleMapping[type] = role
        Map<Role, List<Type>> typeMapping = [:]
        typeMapping[role] = new LinkedList<Type>()
        typeMapping[role] << type

        fixture.setRoleMapping(roleMapping)
        fixture.setTypeMapping(typeMapping)
    }

    @After
    void tearDown() throws Exception {
    }

    @Test
    void "test getType"() {
        // given
        role

        // when
        Type t = fixture.getType(role)

        // then
        the(t).shouldEqual(type)
    }

    @Test
    void "getType with unknown"() {
        // given
        Role r = Classifier.builder()
                .name("Other")
                .create()

        // when
        Type t = fixture.getType(r)

        // then
        the(t).shouldBeNull()
    }

    @Test
    void "getType with null"() {
        // given
        role = null

        // when
        Type t = fixture.getType(role)

        // then
        the(t).shouldBeNull()
    }

    @Test
    void getTypes() {
        // given
        role

        // when
        List<Type> list = fixture.getTypes(role)

        // then
        the(list).shouldContain(type)
        the(list.size()).shouldEqual(1)
    }

    @Test
    void "getTypes with unknown input"() {
        // given
        Role other = Classifier.builder()
                .name("Other")
                .create()

        // when
        List<Type> list = fixture.getTypes(other)

        // then
        the(list).shouldNotBeNull()
        the(list.size()).shouldEqual(0)
    }

    @Test(expected = IllegalArgumentException.class)
    void "getTypes with null"() {
        // given
        role = null

        // when
        fixture.getTypes(role)
    }

    @Test
    void "test getRole"() {
        // given
        type

        // when
        Role r = fixture.getRole(type)

        // then
        the(r).shouldEqual(role)
        the(r).shouldBeA(Classifier)
    }

    @Test
    void "getRole with null"() {
        // when
        def result = fixture.getRole((Field) null)
        the(result).shouldBeNull()
    }

    @Test
    void addMapping() {
        // given
        Role otherRole = Classifier.builder().name("Other").create()
        Type otherType = Type.builder().type(Type.CLASS).name("Other").compKey("Other").create()

        // when
        fixture.addMapping(otherRole, otherType)

        // then
        the(fixture.roleMapping.size()).shouldEqual(2)
        the(fixture.typeMapping.size()).shouldEqual(2)
    }

    @Test
    void "addMapping same role other type"() {
        // given
        Type otherType = Type.builder().type(Type.CLASS).name("Other").compKey("Other").create()

        // when
        fixture.addMapping(role, otherType)

        // then
        the(fixture.roleMapping.size()).shouldEqual(2)
        the(fixture.typeMapping.size()).shouldEqual(1)
    }

    @Test
    void "addMapping same type different role"() {
        // given
        Role otherRole = Classifier.builder().name("Other").create()

        // when
        fixture.addMapping(otherRole, type)

        // then
        the(fixture.roleMapping.size()).shouldEqual(1)
        the(fixture.typeMapping.size()).shouldEqual(2)
        the(fixture.typeMapping[role].size()).shouldEqual(0)
    }

    @Test
    void "addMapping with null role"() {
        // given
        Role otherRole = null

        // when
        fixture.addMapping(otherRole, type)

        // then
        the(fixture.roleMapping.size()).shouldEqual(1)
        the(fixture.typeMapping.size()).shouldEqual(1)
        the(fixture.typeMapping[role].size()).shouldEqual(1)
    }

    @Test
    void "addMapping with null type"() {
        // given
        Type otherType = null

        // when
        fixture.addMapping(role, otherType)

        // then
        the(fixture.roleMapping.size()).shouldEqual(1)
        the(fixture.typeMapping.size()).shouldEqual(1)
        the(fixture.typeMapping[role].size()).shouldEqual(1)
    }

    @Test
    void removeMapping() {
        // given
        role
        type

        // when
        fixture.removeMapping(role, type)

        // then
        the(fixture.typeMapping[role].isEmpty()).shouldBeTrue()
        the(fixture.roleMapping.isEmpty()).shouldBeTrue()
    }

    @Test
    void "removeMapping with null role"() {
        // given
        role = null
        type

        // when
        fixture.removeMapping(role, type)

        // then
        the(fixture.typeMapping.isEmpty()).shouldBeFalse()
        the(fixture.roleMapping.isEmpty()).shouldBeFalse()
    }

    @Test
    void "removeMapping with null type"() {
        // given
        role
        type = null

        // when
        fixture.removeMapping(role, type)

        // then
        the(fixture.typeMapping.isEmpty()).shouldBeFalse()
        the(fixture.roleMapping.isEmpty()).shouldBeFalse()
    }

    @Test
    void remove() {
        // given
        type

        // when
        fixture.remove(type)

        // then
        the(fixture.roleMapping.isEmpty()).shouldBeTrue()
        the(fixture.typeMapping[role].isEmpty()).shouldBeTrue()
    }

    @Test
    void "remove with null type"() {
        // given
        type = null

        // when
        fixture.remove(type)

        // then
        the(fixture.roleMapping.isEmpty()).shouldBeFalse()
        the(fixture.typeMapping[role].isEmpty()).shouldBeFalse()
    }

    @Test
    void testRemove() {
        // given
        role

        // when
        fixture.remove(role)

        // then
        the(fixture.roleMapping.isEmpty()).shouldBeTrue()
        the(fixture.typeMapping.isEmpty()).shouldBeTrue()
    }

    @Test
    void "remove with null role"() {
        // given
        role = null

        // when
        fixture.remove(role)

        // then
        the(fixture.roleMapping.isEmpty()).shouldBeFalse()
        the(fixture.typeMapping.isEmpty()).shouldBeFalse()
    }
}