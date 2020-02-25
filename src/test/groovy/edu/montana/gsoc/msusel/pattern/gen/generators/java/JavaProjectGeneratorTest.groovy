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

import edu.isu.isuese.datamodel.Module
import edu.isu.isuese.datamodel.Project
import edu.montana.gsoc.msusel.pattern.gen.GeneratorContext
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class JavaProjectGeneratorTest extends DBSpec {

    Project data
    GeneratorContext ctx
    FileTreeBuilder builder
    final File testDir = new File('testdir')

    @Before
    void setup() {
        ctx = GeneratorContext.instance
        ctx.resetPatternBuilderComponents()
        ctx.plugin = new JavaLanguageProvider()
        ctx.resetComponentGenerators()
        testDir.mkdirs()
        builder = new FileTreeBuilder(testDir)

        data = Project.builder()
                .name("Test")
                .version("1.0")
                .projKey("Test")
                .create()
    }

    @After
    void teardown() {
        if (testDir.exists())
            testDir.deleteDir()
    }

    @Test
    void "generate a simple project without subprojects"() {
        Module m = Module.builder()
                .name("module")
                .moduleKey("module")
                .create()

        data.addModule(m)
        data.save()

        ctx.projGen.init(proj: data, builder: builder, num: 10, pattern: "test")
        ctx.projGen.generate()

        File moddir = new File(testDir, "module")

        the(testDir.exists()).shouldBeTrue()
        the(moddir.exists()).shouldBeFalse()
    }

    @Test
    void "generate a project with subprojects"() {
        Module m = Module.builder()
                .name("module")
                .moduleKey("module")
                .create()

        data.addModule(m)
        Module m2 = Module.builder()
                .name("module2")
                .moduleKey("module2")
                .create()

        data.addModule(m2)
        data.save()

        ctx.projGen.init(proj: data, builder: builder, num: 10, pattern: "test")
        ctx.projGen.generate()

        File moddir = new File(testDir, "module")
        File moddir2 = new File(testDir, "module2")

        the(testDir.exists()).shouldBeTrue()
        the(moddir.exists()).shouldBeTrue()
        the(moddir2.exists()).shouldBeTrue()
    }
}
