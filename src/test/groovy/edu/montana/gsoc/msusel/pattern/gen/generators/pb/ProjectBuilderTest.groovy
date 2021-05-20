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

import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.System
import edu.montana.gsoc.msusel.pattern.gen.GeneratorContext
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class ProjectBuilderTest extends DBSpec {

    GeneratorContext ctx
    System sys

    @Before
    void setUp() throws Exception {
        ctx = GeneratorContext.getInstance()

        sys = System.builder()
                .name("Test")
                .key("Test")
                .basePath(new File(".").absolutePath)
                .create()

        ctx.srcPath = "src/main/java"
        ctx.testPath = "src/test/java"
        ctx.binPath = "build/classes/java/main"
    }

    @After
    void tearDown() throws Exception {
    }

    @Test
    void createProject() {
        // given:
        sys
        String name = "Test"
        String version = "1.0"
        String pattern = "strategy"

        // when:
        ctx.projBuilder.init(parent: sys, name: name, version: version, pattern: pattern)
        ctx.projBuilder.create()

        // then:
        List<Project> projects = Project.findAll()
        the(projects.size()).shouldEqual(1)
        the(projects[0].name).shouldEqual(name)
        the("${projects[0].projectKey}").shouldEqual("${sys.key}:$name:$version")
        the(projects[0].version).shouldEqual(version)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test create project null sys"() {
        // given:
        sys = null
        String name = "name"
        String version = "1.0"
        String pattern = "test"

        // when:
        ctx.projBuilder.init(parent: sys, name: name, version: version, pattern: pattern)
        ctx.projBuilder.create()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test create project null name"() {
        // given:
        sys
        String name = null
        String version = "1.0"
        String pattern = "test"

        // when:
        ctx.projBuilder.init(parent: sys, name: name, version: version, pattern: pattern)
        ctx.projBuilder.create()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test create project empty name"() {
        // given:
        sys
        String name = ""
        String version = "1.0"
        String pattern = "test"

        // when:
        ctx.projBuilder.init(parent: sys, name: name, version: version, pattern: pattern)
        ctx.projBuilder.create()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test create project null version"() {
        // given:
        sys
        String name = "name"
        String version = null
        String pattern = "test"

        // when:
        ctx.projBuilder.init(parent: sys, name: name, version: version, pattern: pattern)
        ctx.projBuilder.create()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test create project empty version"() {
        // given:
        sys
        String name = "name"
        String version = ""
        String pattern = "test"

        // when:
        ctx.projBuilder.init(parent: sys, name: name, version: version, pattern: pattern)
        ctx.projBuilder.create()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test create project null pattern"() {
        // given:
        sys
        String name = "name"
        String version = "1.0"
        String pattern = null

        // when:
        ctx.projBuilder.init(parent: sys, name: name, version: version, pattern: pattern)
        ctx.projBuilder.create()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test create project empty pattern"() {
        // given:
        sys
        String name = "name"
        String version = "1.0"
        String pattern = ""

        // when:
        ctx.projBuilder.init(parent: sys, name: name, version: version, pattern: pattern)
        ctx.projBuilder.create()
    }
}