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

import edu.isu.isuese.datamodel.Module
import edu.isu.isuese.datamodel.Namespace
import edu.isu.isuese.datamodel.PatternInstance
import edu.isu.isuese.datamodel.Project

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class NamespaceBuilder extends AbstractBuilder {

    def create() {
        if (!params.parent)
            throw new IllegalArgumentException("createNamespace: parent cannot be null")
        if (!params.name)
            throw new IllegalArgumentException("createNamespace: name cannot be null or empty")
        if (!params.pattern)
            throw new IllegalArgumentException("createNamespace: pattern cannot be null or empty")

        println("NSBuilder: ${params.name}")
        if (((String) params.name).contains(".")) {
            int ndx = ((String) params.name).indexOf(".")
            String nsName = ((String) params.name).substring(0, ndx)
            String next = ((String) params.name).substring(ndx + 1)
            String key = "${((Module) params.parent).getParentProject().projectKey}:${nsName}"

            Namespace first = Namespace.builder()
                .name(nsName)
                .nsKey(key)
                .create()

            ((Module) params.parent).getParentProject().addNamespace(first)
            ((Module) params.parent).addNamespace(first)
            createNamespaceRec(((Module) params.parent), first, next, (String) params.pattern)
        } else {
            String key = "${((Module) params.parent).getParentProject().projectKey}:${params.name}"

            Namespace first = Namespace.builder()
                    .name((String) params.name)
                    .nsKey(key)
                    .create()

            ((Module) params.parent).getParentProject().addNamespace(first)
            ((Module) params.parent).addNamespace(first)
            ctx.patternBuilder.init(parent: first, pattern: params.pattern)
            PatternInstance inst = ctx.patternBuilder?.create()
            ctx.patternKeys << inst.getInstKey()

            first
        }
    }

    def createNamespaceRec(Module owner, Namespace parent, String name, String pattern) {
        println("Namespace Rec: $name")
        if (!name.contains(".")) {
            Namespace holder = createChildNamespace(parent, name)
            owner.getParentProject().addNamespace(holder)
            parent.addNamespace(holder)

            ctx.patternBuilder.init(parent: holder, pattern: pattern)
            println("Pattern Builder = ${ctx.patternBuilder}")
            PatternInstance inst = ctx.patternBuilder?.create()
            ctx.patternKeys << inst.getInstKey()

            holder
        }
        else {
            int ndx = name.indexOf(".")
            String nsName = name.substring(0, ndx)
            String next = name.substring(ndx + 1)
            Namespace newParent = createChildNamespace(parent, nsName)

            owner.addNamespace(newParent)
            owner.getParentProject().addNamespace(newParent)

            createNamespaceRec(owner, newParent, next, pattern)
        }
    }

    private Namespace createChildNamespace(Namespace parent, String name) {
        Namespace.builder()
                .name(parent.getFullName() + "." + name)
                .nsKey("${parent.nsKey}.$name")
                .create()
    }

    private String getPackageName() {
        def names = []
        Random rand = new Random()
        int sections = rand.nextInt(3) + 1
        int num = rand.nextInt(3) + 1
        sections.each{ int entry ->
            String[] lines = NamespaceBuilder.class.getResourceAsStream("/edu/montana/gsoc/msusel/pattern/gen/regionnames${num}.txt").readLines()
            int ndx = rand.nextInt(lines.length)
            names << lines[ndx]
        }

        "edu.isu.xrese.${names.join('.').toLowerCase()}"
    }
}
