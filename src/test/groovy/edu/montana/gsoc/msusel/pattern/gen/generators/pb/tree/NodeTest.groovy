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

class NodeTest extends Specification {

    Node fixture

    def setup() {
        fixture = new Node(value: "Test")
    }

    def cleanup() {
    }

    def "test terminal"() {
        given:
        fixture

        when:
        def value = fixture.terminal()

        then:
        value
    }

    def "test terminal has children"() {
        given:
        fixture.children = [new Node(value: "Child")]

        when:
        def value = fixture.terminal()

        then:
        !value
    }

    def "test addChild"() {
        given:
        fixture

        when:
        Node n = fixture.addChild("test")

        then:
        n.parent == fixture
        n.value == "test"
        n.terminal()
        fixture.children.size() == 1
    }

    def "test addChild null"() {
        given:
        fixture

        when:
        Node n = fixture.addChild(null)

        then:
        n == null
        fixture.children.isEmpty()
    }

    def "test addChild empty"() {
        given:
        fixture

        when:
        Node n = fixture.addChild("")

        then:
        n == null
        fixture.children.isEmpty()
    }

    def "test remChild"(String value, int expected) {
        given:
        fixture.children = [
            new Node(value: "Child1"),
            new Node(value: "Child2")
        ]

        when:
        fixture.remChild(value)

        then:
        fixture.children.size == expected

        where:
        value    | expected
        "Child1" | 1
        "Child2" | 1
        "Other"  | 2
    }
}
