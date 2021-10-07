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
package edu.montana.gsoc.msusel.pattern.gen.generators

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class ReadmeGenerator extends AbstractGenerator {

    def generate() {
        if (!params.tree)
            throw new IllegalArgumentException("generate: File Builder cannot be null")
        if (!params.number || new Integer(params.number).intValue() < 0)
            throw new IllegalArgumentException("generate: number must be available and greater than 0")
        if (!params.pattern)
            throw new IllegalArgumentException("generate: pattern cannot be null or empty")

        FileTreeBuilder tree = (FileTreeBuilder) params.tree
//        String pattern = params.pattern
//        int number = params.number

        tree.'README.md'("""\
        # Pattern ${params.pattern} ${params.number}
        """.stripIndent())
    }
}
