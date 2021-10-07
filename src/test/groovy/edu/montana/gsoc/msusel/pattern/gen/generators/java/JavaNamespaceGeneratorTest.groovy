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

import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.FileType
import edu.isu.isuese.datamodel.Namespace
import edu.isu.isuese.datamodel.Project
import edu.montana.gsoc.msusel.pattern.gen.GeneratorContext
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class JavaNamespaceGeneratorTest extends DBSpec {

    GeneratorContext ctx
    FileTreeBuilder builder
    final java.io.File testDir = new java.io.File('testdir')
    Namespace ns1
    Namespace ns2
    Namespace ns3
    Namespace ns4
    Project proj

    @Before
    void setup() {
        testDir.mkdirs()
        builder = new FileTreeBuilder(testDir)

        ns1 = Namespace.builder()
                .name("ns1")
                .nsKey("ns1")
                .create()

        ns2 = Namespace.builder()
                .name("ns1.ns2")
                .nsKey("ns1.ns2")
                .create()
        ns1.addNamespace(ns2)

        ns3 = Namespace.builder()
                .name("ns1.ns3")
                .nsKey("ns1.ns3")
                .create()
        ns1.addNamespace(ns3)

        ns4 = Namespace.builder()
                .name("ns1.ns2.ns4")
                .nsKey("ns1.ns2.ns4")
                .create()
        ns2.addNamespace(ns4)

        proj = Project.builder()
                .name("Test")
                .projKey("Test")
                .version("1.0")
                .create()
        proj.addNamespace(ns1)
        proj.addNamespace(ns2)
        proj.addNamespace(ns3)
        proj.addNamespace(ns4)

        ctx = GeneratorContext.instance
        ctx.resetPatternBuilderComponents()
        ctx.plugin = new JavaLanguageProvider()
        ctx.resetComponentGenerators()
    }

    @After
    void teardown() {
        if (testDir.exists())
            testDir.deleteDir()
    }

    @Test
    void "A single namespace"() {
        ctx.nsGen.init(ns: ns4, builder: builder)
        ctx.nsGen.generate()

        java.io.File ns4dir = new java.io.File(testDir, "ns1/ns2/ns4")

        a(ns4dir.exists()).shouldBeTrue()
        a(ns4dir.isDirectory()).shouldBeTrue()
    }

    @Test
    void "Nested namespaces"() {
        ctx.nsGen.init(ns: ns1, builder: builder)
        ctx.nsGen.generate()
        ctx.nsGen.init(ns: ns2, builder: builder)
        ctx.nsGen.generate()
        ctx.nsGen.init(ns: ns3, builder: builder)
        ctx.nsGen.generate()
        ctx.nsGen.init(ns: ns4, builder: builder)
        ctx.nsGen.generate()

        java.io.File ns1dir = new java.io.File(testDir, "ns1")
        java.io.File ns2dir = new java.io.File(ns1dir, "ns2")
        java.io.File ns3dir = new java.io.File(ns1dir, "ns3")
        java.io.File ns4dir = new java.io.File(ns2dir, "ns4")

        a(testDir.exists()).shouldBeTrue()
        a(ns1dir.exists()).shouldBeTrue()
        a(ns2dir.exists()).shouldBeTrue()
        a(ns3dir.exists()).shouldBeTrue()
        a(ns4dir.exists()).shouldBeTrue()

        a(ns1dir.isDirectory()).shouldBeTrue()
        a(ns2dir.isDirectory()).shouldBeTrue()
        a(ns3dir.isDirectory()).shouldBeTrue()
        a(ns4dir.isDirectory()).shouldBeTrue()
    }
}
