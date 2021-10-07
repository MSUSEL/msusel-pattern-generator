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

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
abstract class AbstractTreeGenerator implements TreeGenerator {

    String root
    List<String> nonterms = []
    List<String> terms = []
    List<Integer> arities = []
    Random rand = new Random()
    int maxDepth
    int size

    void init(String root, List<String> nonterms, List<String> terms, List<Integer> arities, int maxDepth, int size) {
        this.root = root
        this.nonterms = nonterms
        this.terms = terms
        this.arities = arities
        this.maxDepth = maxDepth
        this.size = size
    }

    def randomSelect(List<String> list) {
        if (!list)
            return null

        int ndx = rand.nextInt(list.size())
        list[ndx]
    }

    def selectRandomArity() {
        int ndx = rand.nextInt(arities.size())
        arities[ndx]
    }

    def selectArityLessThan(int size) {
        int arity = -1
        arities.each {
            if (it > arity && it < size)
                arity = it
        }
        arity
    }

    protected Node createInterfaceRoot() {
        boolean interfaceRoot = rand.nextBoolean()
        Node inter = null
        if (interfaceRoot) {
            inter = new Node(parent: null, value: root)
        }
        inter
    }
}
