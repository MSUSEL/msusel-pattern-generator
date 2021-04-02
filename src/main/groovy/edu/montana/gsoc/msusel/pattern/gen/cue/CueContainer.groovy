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


import groovy.transform.TupleConstructor

@TupleConstructor(includeSuperProperties = true, includeSuperFields = true)
abstract class CueContainer extends Cue {

    protected Map<String, Cue> children = [:]

    void addChildCue(Cue child) {
        if (!children)
            children = [: ]
        if (child && !children.containsKey(child.name))
            children[child.name] = child
    }

    void removeChildCue(Cue child) {
        if (child && children.containsKey(child.name))
            children.remove(child.name, child)
    }

    static void main(String[] args) {
        def string = """\
            private int[][] transitions;
            start_field: currentState
            private [[State.root.name]] currentState = 0;
            end_field: currentState
        [[fields]]
        
            public [[InstName]]() {
                transitions = new int[[[ConcreteState.count]]][[[ConcreteState.count]]];
            }
        
            public void changeCurrentState([[State.root.name]] state) {
                currentState = state;
                currentState.run()
            }
        
            start_method: Request
            public void [[name]]() {
                currentState.[[Handle]]()
            }
            end_method: Request
        """

        def pattern = ~/(?ms)\[\[(?<content>[\w\.\d\s]*?)\]\]/
        def results = (string =~ pattern)
        assert results.hasGroup()
        assert results.size() == 8
        for (int i = 0; i < results.size(); i++)
            println(results[i][1])
    }
}