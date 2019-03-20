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
package edu.montana.gsoc.msusel.pattern.gen

import edu.montana.gsoc.msusel.codetree.CodeTree
import edu.montana.gsoc.msusel.codetree.DefaultCodeTree
import edu.montana.gsoc.msusel.codetree.node.Accessibility
import edu.montana.gsoc.msusel.codetree.node.Modifier
import edu.montana.gsoc.msusel.codetree.node.member.Field
import edu.montana.gsoc.msusel.codetree.node.member.Method
import edu.montana.gsoc.msusel.codetree.node.structural.File
import edu.montana.gsoc.msusel.codetree.node.structural.Import
import edu.montana.gsoc.msusel.codetree.node.structural.Namespace
import edu.montana.gsoc.msusel.codetree.node.structural.Project
import edu.montana.gsoc.msusel.codetree.node.type.Class
import edu.montana.gsoc.msusel.codetree.node.type.Interface
import edu.montana.gsoc.msusel.codetree.node.type.Type
import edu.montana.gsoc.msusel.codetree.typeref.PrimitiveTypeRef
import edu.montana.gsoc.msusel.pattern.cue.CueManager
import edu.montana.gsoc.msusel.pattern.gen.java.JavaSourceBuilder
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

class JavaSourceBuilderSpec extends Specification {

    def test() {
        given:
        File f = new File("testdata")
        f.deleteDir()
        CueManager.loadCues("/home/git/patterngen/gof")
        CodeTree tree = new DefaultCodeTree()

        Project pn = Project.builder().key("TestProject").create()

        File file1 = File.builder().key("testdata/src/main/java/com/sparqline/srcml/TestInterface.java").create()

        Namespace ns = Namespace.builder().key("com.sparqline.srcml").create()
        pn.children << ns


        Type type1 = Interface.builder().key("TestInterface").create()
        Method mn1 = Method.builder().key("test").accessibility(Accessibility.PUBLIC).create()
        mn1.addModifier(Modifier.ABSTRACT)
        mn1.type = PrimitiveTypeRef.getInstance("void")
        type1.children << mn1
        ns.children << type1

        file1.children << type1
        file1.namespace = ns

        File file2 = File.builder().key("testdata/src/main/java/com/sparqline/srcml/TestClass.java").create()
        file2.imports << Import.builder().key("java.util.*").create()

        Type type2 = Class.builder().key("TestClass").create()
        Method mn2 = Method.builder().key("test").accessibility(Accessibility.PUBLIC).create()
        Field fn2 = Field.builder().key("test").accessibility(Accessibility.PRIVATE).create()
        fn2.addModifier(Modifier.STATIC)
        fn2.addModifier(Modifier.FINAL)
        fn2.setType PrimitiveTypeRef.getInstance("int")
        mn2.type = PrimitiveTypeRef.getInstance("void")
        type2.children << mn2
        type2.children << fn2
        ns.children << type2

        file2.children << type2
        file2.namespace = ns

        tree.addRealizes(type2, type1)
        pn.children << file1
        pn.children << file2

        tree.setProject(pn)

        AbstractSourceBuilder builder = new JavaSourceBuilder(base: "testdata", pattern: "Singleton", tree: tree)

        when:
        def writer = builder.construct(pn, tree)
        println(writer.toString())

        then:
        Files.exists(Paths.get("testdata/src/main/java/com/sparqline/srcml/TestClass.java"))
        Files.exists(Paths.get("testdata/src/main/java/com/sparqline/srcml/TestInterface.java"))
        Files.size(Paths.get("testdata/src/main/java/com/sparqline/srcml/TestClass.java")) > 0l
        Files.size(Paths.get("testdata/src/main/java/com/sparqline/srcml/TestInterface.java")) > 0l
    }
}