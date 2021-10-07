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

import edu.isu.isuese.datamodel.Module
import edu.isu.isuese.datamodel.Namespace
import edu.isu.isuese.datamodel.Project
import edu.montana.gsoc.msusel.pattern.gen.GeneratorContext
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

class NamespaceBuilderTest extends DBSpec {

    GeneratorContext ctx
    Module data

    @Before
    void setUp() throws Exception {
        ctx = GeneratorContext.instance
        ctx.resetPatternBuilderComponents()

        Project project = Project.builder()
                .name("Test")
                .projKey("Test")
                .version("1.0")
                .create()
        data = Module.builder()
                .name("Test")
                .moduleKey("Test")
                .create()
        project.addModule(data)
    }

    @After
    void tearDown() throws Exception {
    }

    @Test
    void "test createNamespace with no dots"() {
        // given:
        data
        String name = "ns"
        String pattern = "strategy"

        // when:
        ctx.nsBuilder.init(parent: data, name: name, pattern: pattern)
        ctx.nsBuilder.create()
        List<Namespace> ns = Namespace.findAll()

        // then:
        the(data.namespaces.size()).shouldEqual(1)
        the(ns.size()).shouldEqual(1)
        the(ns[0].nsKey).shouldEqual("Test:ns")
        the(ns[0].name).shouldEqual(name)
    }

    @Test
    void "test createNamespace with single dot"() {
        // given:
        data
        String name = "ns.ns1"
        String pattern = "strategy"

        // when:
        ctx.nsBuilder.init(parent: data, name: name, pattern: pattern)
        ctx.nsBuilder.create()
        List<Namespace> ns = Namespace.findAll()

        // then:
        the(data.namespaces.size()).shouldEqual(1)
        the(ns.size()).shouldEqual(2)
        the(ns[0].nsKey).shouldEqual("Test:ns")
        the(ns[0].name).shouldEqual("ns")
        the(ns[0].namespaces.size()).shouldEqual(1)
        the(ns[1].nsKey).shouldEqual("Test:ns.ns1")
        the(ns[1].name).shouldEqual("ns.ns1")
    }

    @Test
    void "test createNamespace with multiple dots"() {
        // given:
        data
        String name = "ns.ns1.ns2"
        String pattern = "strategy"

        // when:
        ctx.nsBuilder.init(parent: data, name: name, pattern: pattern)
        ctx.nsBuilder.create()
        List<Namespace> ns = Namespace.findAll()

        // then:
        the(data.namespaces.size()).shouldEqual(2) // 1
        the(ns.size()).shouldEqual(3)
        the(ns[0].nsKey).shouldEqual("Test:ns")
        the(ns[0].name).shouldEqual("ns")
        the(ns[0].namespaces.size()).shouldEqual(0) // 1
        the(ns[1].nsKey).shouldEqual("Test:ns.ns1")
        the(ns[1].name).shouldEqual("ns.ns1")
        the(ns[1].namespaces.size()).shouldEqual(1)
        the(ns[2].nsKey).shouldEqual("Test:ns.ns1.ns2")
        the(ns[2].name).shouldEqual("ns.ns1.ns2")
    }

    @Test(expected = IllegalArgumentException.class)
    void "test createNamespace with null data"() {
        // given:
        data = null
        String name = "ns"
        String pattern = "strategy"

        // when:
        ctx.nsBuilder.init(parent: data, name: name, pattern: pattern)
        ctx.nsBuilder.create()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test createNamespace with null name"() {
        // given:
        data
        String name = null
        String pattern = "strategy"

        // when:
        ctx.nsBuilder.init(parent: data, name: name, pattern: pattern)
        ctx.nsBuilder.create()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test createNamespace with empty name"() {
        // given:
        data
        String name = ""
        String pattern = "strategy"

        // when:
        ctx.nsBuilder.init(parent: data, name: name, pattern: pattern)
        ctx.nsBuilder.create()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test createNamespace with null pattern"() {
        // given:
        data
        String name = "ns"
        String pattern = null

        // when:
        ctx.nsBuilder.init(parent: data, name: name, pattern: pattern)
        ctx.nsBuilder.create()
    }

    @Test(expected = IllegalArgumentException.class)
    void "test createNamespace with empty pattern"() {
        // given:
        data
        String name = "ns"
        String pattern = ""

        // when:
        ctx.nsBuilder.init(parent: data, name: name, pattern: pattern)
        ctx.nsBuilder.create()
    }
}