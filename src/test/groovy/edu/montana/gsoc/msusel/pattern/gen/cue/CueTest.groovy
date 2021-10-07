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

import edu.isu.isuese.datamodel.Component
import edu.montana.gsoc.msusel.pattern.gen.generators.pb.RBML2DataModelManager
import org.javalite.activejdbc.test.DBSpec
import org.junit.Before
import org.junit.Test

class CueTest extends DBSpec {

    Cue fixture
    Component comp
    CueParams params
    RBML2DataModelManager manager

    @Before
    void setUp() {
        fixture = new FieldCue(name: "Test")
        params = new CueParams()
        params.setParam("literals", "testLiterals")
        params.setParam("typedef", "testTypeDef")
        params.setParam("fields", "testFields")
        params.setParam("methods", "testMethods")
        params.setParam("imports", "testImports")
        params.setParam("package", "testPackage")
        params.setParam("copyright", "testCopyright")
        params.setParam("InstName", "testInstName")
        params.setParam("BaseName", "testBaseName")
        params.setParam("RootName", "testRootName")
        params.setParam("OtherRootName", "testOtherRootName")
    }

    @Test
    void "Compile"() {
        // given:
        String expected = ""
        fixture

        // when:
        String actual = fixture.compile(comp, params, manager)

        // then:
        the actual shouldBeEqual expected
    }

    @Test
    void "preContent"() {
        // given:
        String someText = """\
            [[copyright]]
            [[package]]
            [[imports]]
            [[typedef]]
            [[literals]]
            [[fields]]
            [[methods]]
            [[InstName]]
            [[BaseName]]
            [[RootName]]
            [[OtherRootName]]
            """.stripIndent()
        String expected = """\
            testCopyright
            testPackage
            testImports
            testTypeDef
            testLiterals
            testFields
            testMethods
            testInstName
            testBaseName
            testRootName
            testOtherRootName
            """.stripIndent()
        fixture

        // when:
        String actual = fixture.preContent(someText, params)

        // then:
        the actual shouldBeEqual expected
    }

    @Test
    void "content"() {
        // given:
        String someText
        String expected
        fixture

        // when:
        String actual = fixture.content(someText, comp, params, manager)

        // then:
        the actual shouldBeEqual expected
    }

    @Test
    void "postContent"() {
        // given:
        String someText
        String expected
        fixture

        // when:
        String actual = fixture.postContent(someText, comp, params, manager)

        // then:
        the actual shouldBeEqual expected
    }
}
