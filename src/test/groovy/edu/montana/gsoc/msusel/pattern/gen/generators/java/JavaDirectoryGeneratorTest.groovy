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
package edu.montana.gsoc.msusel.pattern.gen.generators.java

import edu.isu.isuese.datamodel.Module
import edu.isu.isuese.datamodel.Project
import edu.montana.gsoc.msusel.pattern.gen.GeneratorContext
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class JavaDirectoryGeneratorTest extends DBSpec {

    GeneratorContext ctx
    File testDir = new File("testdir")
    FileTreeBuilder builder
    Project project
    Module module

    @Before
    void setup() {
        ctx = GeneratorContext.instance
        ctx.plugin = new JavaLanguageProvider()
        ctx.resetComponentGenerators()
        ctx.license = [
                "name": "MIT",
                "year": "2020",
                "holder": "Developer",
                "project": "TestProj",
                "url": "https://testproj.com"
        ]

        project = Project.builder().name("Test").projKey("Test").version("1.0").create()
        module = Module.builder().name("Test").moduleKey("Test").create()
        project.addModule(module)

        //pbParams = PatternBuilderParams.builder().number(10).pattern("Singleton").create()

        testDir.mkdirs()
        builder = new FileTreeBuilder(testDir)
    }

    @After
    void teardown() {
        if (testDir.exists())
            testDir.deleteDir()
    }

    @Override
    boolean rollback() {
        return super.rollback()
    }

    @Test
    void "test generate"() {
        ctx.dirGen.init(project: project, mod: module, tree: builder, subproject: false, num: 10, pattern: "test")
        ctx.dirGen.generate()
        File dir = new File("testdir")
        File src = new File(dir, "src")
        File main = new File(src, "main")
        new File(main, "java").exists()
        new File(main, "resources").exists()
        File test = new File(src, "test")

        the(dir.exists()).shouldBeTrue()
        the(dir.isDirectory()).shouldBeTrue()
        a(new File(dir, "LICENSE").exists()).shouldBeTrue()
//        new File(dir, "pom.xml").exists()
        a(new File(dir, "README.md").exists()).shouldBeTrue()
        the(src.exists()).shouldBeTrue()
        the(src.isDirectory()).shouldBeTrue()
        the(main.exists()).shouldBeTrue()
        the(main.isDirectory()).shouldBeTrue()
        the(test.exists()).shouldBeTrue()
        the(test.isDirectory()).shouldBeTrue()
        the(new File(test, "java").exists()).shouldBeTrue()
        the(new File(test, "resources").exists()).shouldBeTrue()
    }
}
