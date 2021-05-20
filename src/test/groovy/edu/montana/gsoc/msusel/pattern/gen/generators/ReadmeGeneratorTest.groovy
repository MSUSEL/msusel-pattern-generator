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
package edu.montana.gsoc.msusel.pattern.gen.generators


import spock.lang.Specification

class ReadmeGeneratorTest extends Specification {

    ReadmeGenerator fixture
    FileTreeBuilder builder
    File testDir = new File('testdir')

    def setup() {
        testDir.mkdirs()
        builder = new FileTreeBuilder(testDir)
        fixture = new ReadmeGenerator()
    }

    def cleanup() {
        if (testDir.exists())
            testDir.deleteDir()
    }

    def "test generate"() {
        given:
        fixture
        builder

        when:
        fixture.init(tree: builder, pattern: "Singleton", number: 10)
        fixture.generate()
        File f = new File(testDir, 'README.md')

        then:
        f.exists()
        f.text == """\
# Pattern Singleton 10
"""
    }

    def "test generate with null file builder"() {
        given:
        fixture

        when:
        fixture.init(tree: null, pattern: "Singleton", number: 10)
        fixture.generate()

        then:
        thrown IllegalArgumentException
    }

    def "test generate with empty pattern"() {
        given:
        fixture

        when:
        fixture.init(tree: builder, pattern: "", number: 10)
        fixture.generate()

        then:
        thrown IllegalArgumentException
    }

    def "test generate with null pattern"() {
        given:
        fixture

        when:
        fixture.init(tree: builder, pattern: null, number: 10)
        fixture.generate()

        then:
        thrown IllegalArgumentException
    }

    def "test generate with number less than 0"() {
        given:
        fixture

        when:
        fixture.init(tree: builder, pattern: "Singleton", number: "-1")
        fixture.generate()

        then:
        thrown IllegalArgumentException
    }

    def "test generate with missing number"() {
        given:
        fixture

        when:
        fixture.init(tree: builder, pattern: "Singleton")
        fixture.generate()

        then:
        thrown IllegalArgumentException
    }
}
