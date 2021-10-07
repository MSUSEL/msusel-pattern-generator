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

class CueFactorySpec extends Specification {

    def "CreateCue"(type, name, Class clazz) {
        given:
          type
          name
          clazz

        when:
          Cue actual = CueFactory.instance.createCue(type, name)

        then:
          actual != null
          clazz.isInstance(actual)

        where:
          type            | name   | clazz
          CueType.Pattern | "Test" | PatternCue.class
          CueType.Type    | "Test" | TypeCue.class
          CueType.Method  | "Test" | MethodCue.class
          CueType.Field   | "Test" | FieldCue.class

    }

    def "CreateCue with null type"() {
        given:
          CueType type = null
          String name = "Test"

        when:
          CueFactory.instance.createCue(type, name)

        then:
          thrown IllegalArgumentException
    }

    def "CreateCue with empty name"() {
        given:
          CueType type = CueType.Pattern
          String name = ""

        when:
          CueFactory.instance.createCue(type, name)

        then:
          thrown IllegalArgumentException
    }

    def "CreateCue with null name"() {
        given:
          CueType type = CueType.Pattern
          String name = null

        when:
          CueFactory.instance.createCue(type, name)

        then:
          thrown IllegalArgumentException
    }

    def "GetInstance"() {
        expect:
            CueFactory.instance != null
    }
}
