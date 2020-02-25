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
import edu.isu.isuese.datamodel.Namespace
import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.System
import edu.montana.gsoc.msusel.pattern.gen.generators.CodeGenerator
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*

class JavaCodeGeneratorTest extends DBSpec {

    CodeGenerator fixture
    FileTreeBuilder builder
    System sys
    final File testDir = new File('testdir')

    @Before
    void setup() {
        testDir.mkdirs()
        builder = new FileTreeBuilder(testDir)

        sys = System.builder()
                .name("Bob")
                .key("System:01")
                .create()
        sys.saveIt()

        Module mod = Module.builder()
                .name("module")
                .moduleKey("module")
                .create()

        Project proj = Project.builder()
                .version("1.0")
                .name("project")
                .projKey("test")
                .create()
        proj.saveIt()
        sys.addProject(proj)

        proj.addModule(mod)

        Namespace ns1 = Namespace.builder()
                .name("ns1")
                .nsKey("ns1")
                .create()

        Namespace ns2 = Namespace.builder()
                .name("ns2")
                .nsKey("ns2")
                .create()
        ns1.addNamespace(ns2)

        Namespace ns3 = Namespace.builder()
                .name("ns3")
                .nsKey("ns3")
                .create()
        ns1.addNamespace(ns3)

        Namespace ns4 = Namespace.builder()
                .name("ns4")
                .nsKey("ns4")
                .create()
        ns2.addNamespace(ns4)
        mod.addNamespace(ns1)

        fixture = new JavaCodeGenerator(system: sys)
    }

    @After
    void teardown() {
        if (testDir.exists())
            testDir.deleteDir()
    }

    @Test
    void "test generate"() {
        fixture.generate(builder)

        File ns1 = new File(testDir, "ns1")
        File ns2 = new File(ns1, "ns2")
        File ns3 = new File(ns1, "ns3")
        File ns4 = new File(ns2, "ns4")

        assertTrue("base dir exists", testDir.exists())
        assertTrue("ns1 exists", ns1.exists())
        assertTrue("ns2 exists", ns2.exists())
        assertTrue("ns3 exists", ns3.exists())
        assertTrue("ns4 exists", ns4.exists())

        assertTrue("ns1 is directory", ns1.isDirectory())
        assertTrue("ns2 is directory", ns2.isDirectory())
        assertTrue("ns3 is directory", ns3.isDirectory())
        assertTrue("ns4 is directory", ns4.isDirectory())
    }

    @Test(expected = IllegalArgumentException.class)
    void "test generate with null file tree"() {
        fixture.generate(null)
    }
}
