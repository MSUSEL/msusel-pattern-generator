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
package edu.montana.gsoc.msusel.pattern.gen.cue

import spock.lang.Specification

class CueContainerSpec extends Specification {

    CueContainer fixture

    def setup() {
        fixture = new TypeCue("Test")
    }

    def "Add Child Cue"() {
        given:
        Cue child = new FieldCue(name: "Child")

        when:
        fixture.addChildCue(child)

        then:
        fixture.children.size() == 1
        fixture.children.containsValue(child)
        fixture.children["Child"] != null
        fixture.children["Child"] == child
    }

    def "Add null Child Cue"() {
        given:
        Cue child = null

        when:
        fixture.addChildCue(child)

        then:
        fixture.children.size() == 0
        !fixture.children.containsKey("Child")
    }

    def "Remove Child Cue"() {
        given:
        Cue child = new FieldCue(name: "Child")
        fixture.addChildCue(child)

        when:
        fixture.removeChildCue(child)

        then:
        fixture.children.size() == 0
        !fixture.children.containsValue(child)
        !fixture.children.containsKey("Child")
    }

    def "Remove null Child Cue"() {
        given:
        Cue child = new FieldCue(name: "Child")
        fixture.addChildCue(child)

        when:
        fixture.removeChildCue(null)

        then:
        fixture.children.size() == 1
        fixture.children.containsValue(child)
        fixture.children.containsKey("Child")
    }

    def "Remove unknown Child Cue"() {
        given:
        Cue child = new FieldCue(name: "Child")
        Cue child2 = new FieldCue(name: "Child")
        fixture.addChildCue(child)

        when:
        fixture.removeChildCue(child2)

        then:
        fixture.children.size() == 1
        fixture.children.containsValue(child)
        fixture.children.containsKey("Child")
    }
}
