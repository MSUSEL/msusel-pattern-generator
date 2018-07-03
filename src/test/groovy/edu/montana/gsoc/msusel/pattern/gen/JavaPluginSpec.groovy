/**
 * MIT License
 *
 * MSUSEL Design Pattern Generator
 * Copyright (c) 2017-2018 Montana State University, Gianforte School of Computing
 * Software Engineering Laboratory
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
package edu.montana.gsoc.msusel.pattern.gen

import edu.montana.gsoc.msusel.codetree.CodeTree
import edu.montana.gsoc.msusel.codetree.DefaultCodeTree
import edu.montana.gsoc.msusel.codetree.node.structural.Project
import edu.montana.gsoc.msusel.pattern.cue.CuePattern
import edu.montana.gsoc.msusel.pattern.cue.CueScriptLoader
import edu.montana.gsoc.msusel.pattern.gen.java.JavaPlugin
import edu.montana.gsoc.msusel.rbml.RBMLScriptLoader
import edu.montana.gsoc.msusel.rbml.model.Pattern
import spock.lang.Specification

class JavaPluginSpec extends Specification {

    def test() {
        given:
        File f = new File("testdata")
        f.deleteDir()
        CodeTree tree = new DefaultCodeTree()
        tree.setProject(Project.builder().key("Test").create())

        Pattern rbml = RBMLScriptLoader.load("rbml/gof/singleton.rbml")
        CuePattern cue = CueScriptLoader.load("pgcl/gof/singleton-java.groovy")

        GeneratorConfig config = GeneratorConfig.builder()
            .plugin(new JavaPlugin(tree: tree))
            .base("testdata")
            .cue(cue)
            .rbml(rbml)
            .maxBreadth(1)
            .maxDepth(1)
            .numInstances(1)
            .create()

        when:
        config.plugin.initialize(config.base, rbml.name)
        config.plugin.execute(config)

        then:
        true
    }
}
