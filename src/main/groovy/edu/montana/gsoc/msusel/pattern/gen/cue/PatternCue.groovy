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
package edu.montana.gsoc.msusel.pattern.gen.cue

import edu.isu.isuese.datamodel.Classifier
import edu.isu.isuese.datamodel.Component
import groovy.transform.TupleConstructor

@TupleConstructor(includeSuperFields = true, includeSuperProperties = true, excludes = ["typeCueMap"])
class PatternCue extends CueContainer {
    Map<String, TypeCue> typeCueMap

    @Override
    def getDelimString() {
        return (/begin_pattern: ${name}.*end_pattern: ${name}/)
    }

    @Override
    def getReplacement() {
        return "[[pattern: ${name}]]"
    }

    @Override
    def getCueForRole(String roleName, Classifier c) {
        return null
    }

    @Override
    def hasCueForRole(String roleName, Component t) {
        children.each { key, value ->
            if (key == roleName)
                return true
            else if (value.hasCueForRole(roleName, t))
                return true
        }
        return false
    }
}
