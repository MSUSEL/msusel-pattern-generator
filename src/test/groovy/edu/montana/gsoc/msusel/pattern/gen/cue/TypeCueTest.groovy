/*
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
package edu.montana.gsoc.msusel.pattern.gen.cue


import edu.isu.isuese.datamodel.Field
import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.Type
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.javalite.activejdbc.test.DBSpec
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.fail

@RunWith(JUnitParamsRunner.class)
class TypeCueTest extends DBSpec {

    TypeCue fixture
    FieldCue field
    MethodCue method

    @Before
    void setUp() {
        fixture = new TypeCue(name: "Test")
        method = new MethodCue(name: "aMethod")
        field = new FieldCue(name: "aField")
        fixture.addChildCue(field)
        fixture.addChildCue(method)
    }

    @Test
    void "GetCueForRole Type"() {
        // Given
        Type type = Type.builder().type(Type.CLASS).name("Test").compKey("Test").create()

        // When
        Cue actual = fixture.getCueForRole("Test", type)

        // Then
        the(actual).shouldBeEqual(fixture)
    }

    @Test
    void "GetCueForRole child Field"() {
        // Given
        Field f = Field.builder().name("Test").compKey("Test").create()

        // When
        Cue actual = fixture.getCueForRole("aField", f)

        // Then
        the(actual).shouldBeEqual(field)
    }

    @Test
    void "GetCueForRole child given parent type"() {
        // Given
        Type type = Type.builder().type(Type.CLASS).name("Test").compKey("Test").create()

        // When
        Cue actual = fixture.getCueForRole("aField", type)

        // Then
        the(actual).shouldBeNull()
    }

    @Test
    void "GetCueForRole child Method"() {
        // Given
        Method m = Method.builder().name("Test").compKey("Test").create()

        // When
        Cue actual = fixture.getCueForRole("aMethod", m)

        // Then
        the(actual).shouldBeEqual(method)
    }

    @Test
    void "GetCueForRole unknown rolename"() {
        // Given
        Type type = Type.builder().type(Type.CLASS).name("Test").compKey("Test").create()
        String roleName = "Unknown"

        // When
        Cue actual = fixture.getCueForRole(roleName, type)

        // Then
        the(actual).shouldBeNull()
    }

    @Test
    void "GetCueForRole null rolename"() {
        // Given
        Type type = Type.builder().type(Type.CLASS).name("Test").compKey("Test").create()
        String roleName = null

        // When
        Cue actual = fixture.getCueForRole(roleName, type)

        // Then
        the(actual).shouldBeNull()
    }

    @Test
    void "GetCueForRole null component"() {
        // Given
        Type type = null
        String roleName = "Test"

        // When
        Cue actual = fixture.getCueForRole(roleName, type)

        // Then
        the(actual).shouldBeNull()
    }

    @Test
    @Parameters([
            "Test, true",
            "aMethod, false",
            "aField, false",
            "Unknown, false",
            ", false"
    ])
    void "HasCueForRole"(String roleName, boolean expected) {
        // Given
        Type type = Type.builder().type(Type.CLASS).name("Test").compKey("Test").create()

        // When
        boolean actual = fixture.getCueForRole(roleName, type)

        // Then
        the(actual).shouldBeEqual(expected)
    }

    @Test
    void "Content"() {
        fail()
    }
}