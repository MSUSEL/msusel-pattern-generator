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

import edu.isu.isuese.datamodel.Type

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class Tree {

    Node root

    boolean isEmpty() { !root }

    String toString() {
        StringBuilder builder = new StringBuilder()
        int indent = 1
        builder.append(root.value)
        builder.append("\n")
        builder.append(root.childrenToString(builder, indent))
    }
}

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class Node {
    def children = []
    String value
    Node parent
    Type type

    boolean terminal() {
        children.isEmpty()
    }

    Node addChild(String value) {
        Node n = null
        if (value) {
            n = new Node(parent: this, value: value)
            children << n
        }
        n
    }

    void remChild(String value) {
        Node n = children.find {
            it.value == value
        }
        if (n)
            children.remove(n)
    }

    String childrenToString(StringBuilder builder, int indent) {
        children.each {
            for (int i = 0; i < indent; i++)
                builder.append("    ")
            builder.append(it.value)
            builder.append("\n")
            builder.append(it.childrenToString(builder, indent + 1))
        }
    }
}
