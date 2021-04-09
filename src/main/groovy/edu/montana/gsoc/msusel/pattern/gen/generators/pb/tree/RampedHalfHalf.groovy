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
package edu.montana.gsoc.msusel.pattern.gen.generators.pb.tree

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class RampedHalfHalf extends AbstractTreeGenerator {

    enum Type { GROW, FULL }

    @Override
    Tree generate() {
        //Node inter = createInterfaceRoot()
        //Node root = grow(0, maxDepth, inter, Type.GROW)
        Node root = grow(0, maxDepth, null, Type.GROW)
        new Tree(root: root)
    }

    private Node grow(int depth, int maxDepth, Node parent, Type type) {
        Node node
        if (depth == maxDepth) {
            node = parent.addChild(randomSelect(terms))
        } else if (depth == 0) {
//            node.value = parent ? randomSelect(nonterms) : root
            node = new Node(parent: parent)
            node.value = root
            generateChildren(node, depth, maxDepth)
        }
        else {
            if (type == Type.GROW) {
                List<String> combo = []
                combo += terms
                combo += nonterms
                node = parent.addChild(randomSelect(combo))
            } else {
                node = parent.addChild(randomSelect(nonterms))
            }
            if (node.value in nonterms) {
                generateChildren(node, depth, maxDepth)
            }
        }

        node
    }

    private void generateChildren(Node node, int depth, int maxDepth) {
        int arity = selectRandomArity()
        arity.times {
            if (rand.nextBoolean())
                node.children << grow(depth + 1, maxDepth, node, Type.GROW)
            else
                node.children << grow(depth + 1, maxDepth, node, Type.FULL)
        }
    }
}
