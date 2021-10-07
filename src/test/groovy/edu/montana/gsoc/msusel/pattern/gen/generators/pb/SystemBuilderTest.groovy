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
package edu.montana.gsoc.msusel.pattern.gen.generators.pb

import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.System
import edu.montana.gsoc.msusel.pattern.gen.GeneratorContext
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class SystemBuilderTest extends DBSpec {

    GeneratorContext ctx

    @Before
    void setup() {
        ctx = GeneratorContext.instance
        ctx.resetPatternBuilderComponents()
        ctx.srcPath = "src/main/java"
        ctx.testPath = "src/test/java"
        ctx.binPath = "build/classes/java/main"
    }

    @After
    void teardown() {

    }

    @Test
    void "test createSystem ok and 1"() {
        // given:
        String pattern = "strategy"
        int num = 1

        // when:
        ctx.sysBuilder.init(id: "1", pattern: pattern, num: num)
        ctx.sysBuilder.create()
        List<System> sys = System.findAll()

        // then:
        the(sys.size()).shouldEqual(1)
        the(sys[0].name).shouldEqual(pattern)
        List<Project> proj = sys[0].projects
        the(proj.size()).shouldEqual(1)
        the("${proj[0].name}").shouldEqual("${pattern}-1")
    }

    @Test
    void "test createSystem ok and 2"() {
        // given:
        String pattern = "strategy"
        int num = 2

        // when:
        ctx.sysBuilder.init(id: "2", pattern: pattern, num: num)
        ctx.sysBuilder.create()
        List<System> sys = System.findAll()

        // then:
        the(sys.size()).shouldEqual(1)
        the(sys[0].name).shouldEqual(pattern)
        the(Project.findAll().size()).shouldEqual(1)
        List<Project> proj = sys[0].getProjects()
        the(proj.size()).shouldEqual(1)
        the("${proj[0].name}").shouldEqual("${pattern}-2")
    }

    @Test(expected = IllegalArgumentException.class)
    void "test createSystem null and 1"() {
        // given:
        String pattern = null
        int num = 1

        // when:
        ctx.sysBuilder.init(pattern: pattern, num: num)
        ctx.sysBuilder.create()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test createSystem empty and 1"() {
        // given:
        String pattern = ""
        int num = 1

        // when:
        ctx.sysBuilder.init(pattern: pattern, num: num)
        ctx.sysBuilder.create()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test createSystem ok and 0"() {
        // given:
        String pattern = "strategy"
        int num = 0

        // when:
        ctx.sysBuilder.init(pattern: pattern, num: num)
        ctx.sysBuilder.create()
        List<System> sys = System.findAll()
    }
}