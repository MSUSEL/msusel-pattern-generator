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

class JavaBuildFileGeneratorTest extends DBSpec {

    GeneratorContext ctx
    FileTreeBuilder builder
    Project project
    final File testDir = new File('testdir')

    @Before
    void setup() {
        ctx = GeneratorContext.instance
        ctx.plugin = new JavaLanguageProvider()
        ctx.resetComponentGenerators()
        ctx.build = [
                "project": "Test",
                "artifact": "test",
                "description": "A simple test project"
        ]
        testDir.mkdirs()
        builder = new FileTreeBuilder(testDir)

        project = Project.builder()
                .name("Test")
                .version("1.0")
                .projKey("Test")
                .create()
    }

    @After
    void cleanup() {
        if (testDir.exists())
            testDir.deleteDir()
    }

    @Test
    void "test generate default type"() {
        ctx.buildGen.init(tree: builder, project: project)
        ctx.buildGen.generate()
        File build = new File(testDir, "build.gradle")
        File settings = new File(testDir, "settings.gradle")

        build.exists()
        settings.exists()
    }

    @Test
    void "test generate maven type"() {
        ctx.buildGen.init(tree: builder, project: project)
        ctx.buildGen.generate()
        File pom = new File(testDir, "pom.xml")

        pom.exists()
    }

    @Test
    void "test generate gradle type"() {
        ctx.buildGen.init(tree: builder, project: project)
        ctx.buildGen.generate()
        File build = new File(testDir, "build.gradle")
        File settings = new File(testDir, "settings.gradle")

        build.exists()
        settings.exists()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test generate with null builder"() {
        builder = null

        ctx.buildGen.init(tree: builder, project: project)
        ctx.buildGen.generate()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test generate with null type"() {
        def type = null

        ctx.buildGen.init(tree: builder, project: type)
        ctx.buildGen.generate()
    }

    @Test
    void "test generateGradleFile"() {
        ctx.buildGen.generateBuild(builder)
        File f = new File(testDir, 'build.gradle')

        f.exists()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test generateBuild with null builder"() {
        builder = null

        ctx.buildGen.generateBuild(builder)
    }

    @Test
    void "test generateSettings"() {
        ctx.buildGen.generateSettings(builder, project)
        File f = new File(testDir, 'settings.gradle')

        f.exists()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test generateSettings with null builder"() {
        builder = null

        ctx.buildGen.generateSettings(builder, project)
    }

    @Test(expected = IllegalArgumentException.class)
    void "test generateGradleSettingFile with null project"() {
        project = null

        ctx.buildGen.generateSettings(builder, project)
    }

    @Test
    void "test generateBuild"() {
        ctx.buildGen = JavaBuildFileGenerator.getInstance("maven", ctx)

        ctx.buildGen.generateBuild(builder)
        File f = new File(testDir, 'pom.xml')

        f.exists()
    }

    @Test
    void "test generate gradle subproject"() {
        // given
        File build = new File(testDir, "build.gradle")

        // when
        ctx.buildGen.generateSubproject(builder)

        // then
        the(build.exists()).shouldBeTrue()
        the(build.text).shouldContain("""\
        dependencies {
        
        }""".stripIndent())
    }

    @Test
    void "test generate gradle rootproject"() {
        // given
        Module mod01 = Module.builder().name("mod01").moduleKey("mod01").create()
        Module mod02 = Module.builder().name("mod02").moduleKey("mod02").create()
        Module mod03 = Module.builder().name("mod03").moduleKey("mod03").create()
        project.addModule(mod01)
        project.addModule(mod02)
        project.addModule(mod03)
        File build = new File(testDir, "build.gradle")
        File settings = new File(testDir, "settings.gradle")

        // when
        ctx.buildGen.generateRootProject(builder, project)

        // then
        the(build.exists()).shouldBeTrue()
        the(build.text).shouldContain("""\
        subprojects {
            version = '1.0'
        }""".stripIndent())
        the(settings.exists()).shouldBeTrue()
        the(settings.text).shouldContain("rootProject.name = 'Test'")
        the(settings.text).shouldContain("include 'mod01', 'mod02', 'mod03'")
    }
}
