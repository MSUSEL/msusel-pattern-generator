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
package edu.montana.gsoc.msusel.pattern.cue

import org.codehaus.groovy.control.CompilerConfiguration

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class CueScriptLoader {

    def scripts = [
            "abstract factory":        "/pgcl/gof/abstract_factory.groovy",
            "adapter":                 "/pgcl/gof/adapter.groovy",
            "bridge":                  "/pgcl/gof/bridge.groovy",
            "builder":                 "/pgcl/gof/builder.groovy",
            "chain of responsibility": "/pgcl/gof/chain_of_responsibility.groovy",
            "command":                 "/pgcl/gof/command.groovy",
            "composite":               "/pgcl/gof/composite.groovy",
            "decorator":               "/pgcl/gof/decorator.groovy",
            "facade":                  "/pgcl/gof/facade.groovy",
            "factory method":          "/pgcl/gof/factory_method.groovy",
            "flyweight":               "/pgcl/gof/flyweight.groovy",
            "interpreter":             "/pgcl/gof/interpreter.groovy",
            "iterator":                "/pgcl/gof/iterator.groovy",
            "mediator":                "/pgcl/gof/mediator.groovy",
            "memento":                 "/pgcl/gof/memento.groovy",
            "observer":                "/pgcl/gof/observer.groovy",
            "prototype":               "/pgcl/gof/prototype.groovy",
            "proxy":                   "/pgcl/gof/proxy.groovy",
            "singleton":               "/pgcl/gof/singleton-java.groovy",
            "state":                   "/pgcl/gof/state.groovy",
            "strategy":                "/pgcl/gof/strategy.groovy",
            "template method":         "/pgcl/gof/template_method.groovy",
            "visitor":                 "/pgcl/gof/visitor.groovy"
    ]

    def loadPatternCues(String pattern) {
        if (!pattern)
            throw new IllegalArgumentException("load: pattern cannot be null or empty")

        if (scripts[pattern.toLowerCase()]) {
            def conf = new CompilerConfiguration()
            conf.setScriptBaseClass("edu.montana.gsoc.msusel.pattern.cue.CueScript")
            def shell = new GroovyShell(this.class.classLoader, new Binding(), conf)
            shell.evaluate(CueScriptLoader.class.getResourceAsStream(scripts[pattern.toLowerCase()]).text)
        }

        null
    }

    static def load(String file) {
        def conf = new CompilerConfiguration()
        conf.setScriptBaseClass("edu.montana.gsoc.msusel.pattern.cue.CueScript")
        def shell = new GroovyShell(this.class.classLoader, new Binding(), conf)
        shell.evaluate(new File(file))
    }
}
