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
package edu.montana.gsoc.msusel.pattern.gen.generators.pb.tree

import spock.lang.Specification

class RandomBranchTest extends Specification {

    RandomBranch fixture

    void setup() {
        fixture = new RandomBranch()
    }

    void cleanup() {
    }

    def "RandomSelect"() {
        given:
        fixture
        def list = ["Test", "AbsTest", "ConTest"]

        when:
        String val = fixture.randomSelect(list)

        then:
        val != null
        val in list
    }

    def "RandomSelect null"() {
        given:
        fixture
        def list = null

        when:
        String val = fixture.randomSelect(list)

        then:
        val == null
    }

    def "RandomSelect empty"() {
        given:
        fixture
        def list = []

        when:
        String val = fixture.randomSelect(list)

        then:
        val == null
    }

    def "SelectArityLessThan"(int size, expected) {
        given:
        fixture.arities = [1, 3, 5]

        when:
        def arity = fixture.selectArityLessThan(size)

        then:
        arity == expected

        where:
        size | expected
        0    | -1
        1    | -1
        2    | 1
        3    | 1
        4    | 3
        5    | 3
        6    | 5
        7    | 5
        10   | 5
    }

    def "Generate"() {
        given:
        fixture.nonterms = ["AbsTest"]
        fixture.terms = ["ConTest"]
        fixture.arities = [1, 2, 3, 4, 5]
        fixture.maxDepth = 3
        fixture.size = 20

        when:
        Tree tree = fixture.generate()

        then:
        tree != null
        tree.root != null
        if (tree.root.children)
            tree.root.value == "AbsTest"
        else
            tree.root.value == "ConTest"
    }
}
