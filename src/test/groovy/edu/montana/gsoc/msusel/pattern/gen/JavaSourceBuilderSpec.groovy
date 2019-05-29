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

import edu.isu.isuese.datamodel.Accessibility
import edu.isu.isuese.datamodel.Modifier
import edu.isu.isuese.datamodel.Field
import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Import
import edu.isu.isuese.datamodel.Namespace
import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.Class
import edu.isu.isuese.datamodel.Interface
import edu.isu.isuese.datamodel.Type
import edu.isu.isuese.datamodel.TypeRef
import edu.montana.gsoc.msusel.pattern.cue.CueManager
import edu.montana.gsoc.msusel.pattern.gen.java.JavaSourceBuilder
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths


class JavaSourceBuilderSpec extends Specification {

    def test() {
        given:
        java.io.File f = new java.io.File("testdata")
        f.deleteDir()
        CueManager.loadCues("/home/git/patterngen/gof")
        
        Project pn = Project.builder().projKey("TestProject").create()

        File file1 = File.builder().fileKey("testdata/src/main/java/com/sparqline/srcml/TestInterface.java").create()

        Namespace ns = Namespace.builder().nsKey("com.sparqline.srcml").create()
        pn.addns


        Type type1 = Interface.builder().compKey("TestInterface").create()
        Method mn1 = Method.builder().compKey("test").accessibility(Accessibility.PUBLIC).create()
        mn1.addModifier(Modifier.forName(Modifier.Values.ABSTRACT.toString()))
        mn1.type = TypeRef.createPrimitiveTypeRef("void")
        type1.addMember(mn1)

        ns.children << type1

        file1.addType(type1)

        File file2 = File.builder().fileKey("testdata/src/main/java/com/sparqline/srcml/TestClass.java").create()
        file2.addImport(Import.builder().name("java.util.*").create())

        Type type2 = Class.builder().compKey("TestClass").create()
        Method mn2 = Method.builder().compKey("test").accessibility(Accessibility.PUBLIC).create()
        Field fn2 = Field.builder().compKey("test").accessibility(Accessibility.PRIVATE).create()
        fn2.addModifier(Modifier.forName(Modifier.Values.STATIC.toString()))
        fn2.addModifier(Modifier.forName(Modifier.Values.FINAL.toString()))
        fn2.setType TypeRef.createPrimitiveTypeRef("int")
        mn2.setType TypeRef.createPrimitiveTypeRef("void")
        type2.addMember(mn2)
        type2.addMember(fn2)

        ns.addFile(file2)
        file2.addType(type2)

        type2.realizes(type1)

        AbstractSourceBuilder builder = new JavaSourceBuilder(base: "testdata", pattern: "Singleton")

        when:
        def writer = builder.construct(pn)
        println(writer.toString())

        then:
        Files.exists(Paths.get("testdata/src/main/java/com/sparqline/srcml/TestClass.java"))
        Files.exists(Paths.get("testdata/src/main/java/com/sparqline/srcml/TestInterface.java"))
        Files.size(Paths.get("testdata/src/main/java/com/sparqline/srcml/TestClass.java")) > 0l
        Files.size(Paths.get("testdata/src/main/java/com/sparqline/srcml/TestInterface.java")) > 0l
    }
}