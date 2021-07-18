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
package edu.montana.gsoc.msusel.pattern.gen.generators.pb

import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.System

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class SystemBuilder extends AbstractBuilder {

    def create() {
        if (!params.pattern)
            throw new IllegalArgumentException("createSystem: pattern cannot be null or empty")
        if (!params.id)
            throw new IllegalArgumentException("createSystem: identifier cannot be null or empty")

        String name = params.pattern
        String key = params.pattern
        System sys
        if (!params.system)
            sys = System.builder().key(key).name(name).basePath(ctx.getOutput() + File.separator + name).create()
        else
            sys = (System) params.system

        ctx.projBuilder.init(parent: sys, pattern: params.pattern, name: "${params.pattern}-${params.id}", version: "1.0.0")
        Project proj = ctx.projBuilder.create()

        ctx.results.put((String) params.id, "Key1", proj.getProjectKey())
        ctx.results.put((String) params.id, "Path1", proj.getFullPath())

        sys
    }
}
