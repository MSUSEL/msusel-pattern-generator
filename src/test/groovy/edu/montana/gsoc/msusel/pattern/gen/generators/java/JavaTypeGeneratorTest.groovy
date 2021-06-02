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
package edu.montana.gsoc.msusel.pattern.gen.generators.java

import edu.isu.isuese.datamodel.*
import edu.montana.gsoc.msusel.pattern.gen.GeneratorContext
import edu.montana.gsoc.msusel.pattern.gen.cue.Cue
import edu.montana.gsoc.msusel.pattern.gen.cue.CueManager
import edu.montana.gsoc.msusel.rbml.model.SPS
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

import java.io.File

import static org.junit.Assert.assertEquals

class JavaTypeGeneratorTest extends DBSpec {

    GeneratorContext ctx
    FileTreeBuilder builder
    final File testDir = new File("testdir")
    Type data
    edu.isu.isuese.datamodel.File file
    Namespace ns

    @Before
    void setup() {
        ctx = GeneratorContext.instance
        ctx.resetPatternBuilderComponents()
        ctx.plugin = new JavaLanguageProvider()
        ctx.resetComponentGenerators()
        testDir.mkdirs()
        builder = new FileTreeBuilder(testDir)

        Project project = Project.builder()
                .name("Test")
                .projKey("Test")
                .version("1.0")
                .create()
        project.save()

        System sys = System.builder().name("test").key("test").create()

        ns = Namespace.builder().name("test").nsKey("test").relPath("test").create()

        file = edu.isu.isuese.datamodel.File.builder().type(FileType.SOURCE).name("Test.java").fileKey("Test.java").relPath("Test.java").create()

        ns.save()
        project.addNamespace(ns)
        project.addFile(file)
        sys.addProject(project)
        sys.updateKeys()
        ns.refresh()
        project.refresh()
    }

    @After
    void cleanup() {
        if (testDir.exists())
            testDir.deleteDir()
    }

    @Test
    void "generate an empty class"() {
        data = Type.builder().type(Type.CLASS).name("Test").accessibility(Accessibility.PUBLIC).create()

        ns.addType(data)
        file.addType(data)

        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public class Test {

}
"""
        ctx.typeGen.init(type: data)
        String observed = ctx.typeGen.generate().stripIndent()

        assertEquals(expected, observed)
    }

    @Test
    void "generate an empty enum"() {
        data = Type.builder().type(Type.ENUM).name("Test").accessibility(Accessibility.PUBLIC).create()

        ns.addType(data)
        file.addType(data)

        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public enum Test {

}
"""
        ctx.typeGen.init(type: data)
        String observed = ctx.typeGen.generate().stripIndent()

        assertEquals(expected, observed)
    }

    @Test
    void "generate an empty interface"() {
        data = Type.builder().type(Type.INTERFACE).name("Test").accessibility(Accessibility.PUBLIC).create()

        ns.addType(data)
        file.addType(data)

        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public interface Test {

}
"""
        ctx.typeGen.init(type: data)
        String observed = ctx.typeGen.generate().stripIndent()

        assertEquals(expected, observed)
    }

    @Test
    void "generate a public class"() {
        data = Type.builder().type(Type.CLASS)
                .name("Test")
                .accessibility(Accessibility.PUBLIC)
                .create()

        ns.addType(data)
        file.addType(data)

        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public class Test {

}
"""
        ctx.typeGen.init(type: data)
        String observed = ctx.typeGen.generate().stripIndent()

        assertEquals(expected, observed)
    }

    @Test
    void "generate a public static class"() {
        data = Type.builder().type(Type.CLASS)
                .name("Test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.addModifier(Modifier.forName("STATIC"))

        ns.addType(data)
        file.addType(data)

        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public static class Test {

}
"""
        ctx.typeGen.init(type: data)
        String observed = ctx.typeGen.generate().stripIndent()

        assertEquals(expected, observed)
    }

    @Test
    void "generate a private class"() {
        data = Type.builder().type(Type.CLASS)
                .name("Test")
                .accessibility(Accessibility.PRIVATE)
                .create()

        ns.addType(data)
        file.addType(data)

        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
private class Test {

}
"""
        ctx.typeGen.init(type: data)
        String observed = ctx.typeGen.generate().stripIndent()

        assertEquals(expected, observed)
    }

    @Test
    void "generate a default class"() {
        data = Type.builder().type(Type.CLASS)
                .name("Test")
                .accessibility(Accessibility.DEFAULT)
                .create()

        ns.addType(data)
        file.addType(data)

        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
class Test {

}
"""
        ctx.typeGen.init(type: data)
        String observed = ctx.typeGen.generate().stripIndent()

        assertEquals(expected, observed)
    }

    @Test
    void "generate a final class"() {
        data = Type.builder().type(Type.CLASS)
                .name("Test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.addModifier(Modifier.forName("FINAL"))

        ns.addType(data)
        file.addType(data)

        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public final class Test {

}
"""
        ctx.typeGen.init(type: data)
        String observed = ctx.typeGen.generate().stripIndent()

        assertEquals(expected, observed)
    }

    @Test
    void "generate a class with one method"() {
        data = Type.builder().type(Type.CLASS)
                .name("Test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Method m = Method.builder()
                .name("test")
                .accessibility(Accessibility.PUBLIC)
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .create()
        data.addMember(m)

        ns.addType(data)
        file.addType(data)

        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public class Test {

    /**
     *
     */
    public void test() {
    }

}
"""
        ctx.typeGen.init(type: data)
        String observed = ctx.typeGen.generate().stripIndent()

        assertEquals(expected, observed)
    }

    @Test
    void "generate a class with two methods"() {
        data = Type.builder().type(Type.CLASS)
                .name("Test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Method m = Method.builder()
                .name("test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .create()
        Method m2 = Method.builder()
                .name("test2")
                .compKey("test2")
                .accessibility(Accessibility.PUBLIC)
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .create()
        data.addMember(m)
        data.addMember(m2)

        ns.addType(data)
        file.addType(data)

        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public class Test {

    /**
     *
     */
    public void test() {
    }

    /**
     *
     */
    public void test2() {
    }

}
"""
        ctx.typeGen.init(type: data)
        String observed = ctx.typeGen.generate().stripIndent()

        assertEquals(expected, observed)
    }

    @Test
    void "generate a class with one field"() {
        data = Type.builder().type(Type.CLASS)
                .name("Test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Field f = Field.builder()
                .name("test")
                .accessibility(Accessibility.PRIVATE)
                .create()
        f.setType(TypeRef.createPrimitiveTypeRef("int"))
        data.addMember(f)

        ns.addType(data)
        file.addType(data)

        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public class Test {

    private int test;

    /**
     * @return the value of test
     */
    public int getTest() {
        return test;
    }

    /**
     * @param test the new value for test
     */
    public void setTest(int test) {
        this.test = test;
    }

}
"""
        ctx.typeGen.init(type: data)
        String observed = ctx.typeGen.generate().stripIndent()

        assertEquals(expected, observed)
    }

    @Test
    void "generate a class with two fields"() {
        data = Type.builder().type(Type.CLASS)
                .name("Test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Field f = Field.builder()
                .name("test")
                .compKey("test")
                .accessibility(Accessibility.PRIVATE)
                .create()
        f.setType(TypeRef.createPrimitiveTypeRef("int"))
        Field f2 = Field.builder()
                .name("test2")
                .compKey("test2")
                .accessibility(Accessibility.PRIVATE)
                .create()
        f2.setType(TypeRef.createPrimitiveTypeRef("int"))
        data.addMember(f)
        data.addMember(f2)

        ns.addType(data)
        file.addType(data)

        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public class Test {

    private int test;
    private int test2;

    /**
     * @return the value of test
     */
    public int getTest() {
        return test;
    }

    /**
     * @param test the new value for test
     */
    public void setTest(int test) {
        this.test = test;
    }

    /**
     * @return the value of test2
     */
    public int getTest2() {
        return test2;
    }

    /**
     * @param test2 the new value for test2
     */
    public void setTest2(int test2) {
        this.test2 = test2;
    }

}
"""
        ctx.typeGen.init(type: data)
        String observed = ctx.typeGen.generate().stripIndent()

        assertEquals(expected, observed)
    }

    @Test
    void "generate an interface with one method"() {
        data = Type.builder().type(Type.INTERFACE)
                .name("Test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Method m = Method.builder()
                .name("test")
                .accessibility(Accessibility.PUBLIC)
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .create()
        m.addModifier(Modifier.forName("ABSTRACT"))
        data.addMember(m)

        ns.addType(data)
        file.addType(data)

        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public interface Test {

    /**
     *
     */
    void test();

}
"""
        ctx.typeGen.init(type: data)
        String observed = ctx.typeGen.generate().stripIndent()

        assertEquals(expected, observed)
    }

    @Test
    void "generate an interface with two methods"() {
        data = Type.builder().type(Type.INTERFACE)
                .name("Test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Method m = Method.builder()
                .name("test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .create()
        m.addModifier(Modifier.forName("ABSTRACT"))
        Method m2 = Method.builder()
                .name("test2")
                .compKey("test2")
                .accessibility(Accessibility.PUBLIC)
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .create()
        m2.addModifier(Modifier.forName("ABSTRACT"))
        data.addMember(m)
        data.addMember(m2)

        ns.addType(data)
        file.addType(data)

        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public interface Test {

    /**
     *
     */
    void test();

    /**
     *
     */
    void test2();

}
"""
        ctx.typeGen.init(type: data)
        String observed = ctx.typeGen.generate().stripIndent()

        assertEquals(expected, observed)
    }

    @Test
    void "generate an interface with a field"() {
        data = Type.builder().type(Type.INTERFACE)
                .name("Test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Field f = Field.builder()
                .name("test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        f.setType(TypeRef.createPrimitiveTypeRef("int"))
        f.addModifier(Modifier.forName("STATIC"))
        f.addModifier(Modifier.forName("FINAL"))
        data.addMember(f)

        ns.addType(data)
        file.addType(data)

        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public interface Test {

    int test;

}
"""
        ctx.typeGen.init(type: data)
        String observed = ctx.typeGen.generate().stripIndent()

        assertEquals(expected, observed)
    }

    @Test
    void "generate an enum with one literal"() {
        data = Type.builder().type(Type.ENUM)
                .name("Test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Literal l = Literal.builder()
                .name("test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.addMember(l)

        ns.addType(data)
        file.addType(data)

        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public enum Test {

    test;

}
"""
        ctx.typeGen.init(type: data)
        String observed = ctx.typeGen.generate().stripIndent()

        assertEquals(expected, observed)
    }

    @Test
    void "generate an enum with two literals"() {
        data = Type.builder().type(Type.ENUM)
                .name("Test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Literal l = Literal.builder()
                .name("test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.addMember(l)
        Literal l2 = Literal.builder()
                .name("test2")
                .compKey("test2")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.addMember(l2)

        ns.addType(data)
        file.addType(data)

        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public enum Test {

    test,
    test2;

}
"""
        ctx.typeGen.init(type: data)
        String observed = ctx.typeGen.generate().stripIndent()

        assertEquals(expected, observed)
    }

    @Test
    void "generate a class that extends another"() {
        data = Type.builder().type(Type.CLASS)
                .name("Test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Type parent = Type.builder().type(Type.CLASS)
                .name("Parent")
                .compKey("parent")
                .accessibility(Accessibility.PUBLIC)
                .create()
        parent.addModifier(Modifier.forName("ABSTRACT"))
        data.generalizedBy(parent)

        ns.addType(data)
        ns.addType(parent)
        file.addType(data)
        file.addType(parent)

        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public class Test extends Parent {

}
"""
        ctx.typeGen.init(type: data)
        String observed = ctx.typeGen.generate().stripIndent()

        assertEquals(expected, observed)
    }

    @Test
    void "generate a class that implements an interface"() {
        data = Type.builder().type(Type.CLASS)
                .name("Test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Type parent = Type.builder().type(Type.INTERFACE)
                .name("Parent")
                .compKey("parent")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.realizes(parent)

        ns.addType(data)
        ns.addType(parent)
        file.addType(data)
        file.addType(parent)

        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public class Test implements Parent {

}
"""
        ctx.typeGen.init(type: data)
        String observed = ctx.typeGen.generate().stripIndent()

        assertEquals(expected, observed)
    }

    @Test
    void "generate a class that implements multiple interfaces"() {
        data = Type.builder().type(Type.CLASS)
                .name("Test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Type parent = Type.builder().type(Type.INTERFACE)
                .name("Parent")
                .compKey("parent")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.realizes(parent)
        Type parent2 = Type.builder().type(Type.INTERFACE)
                .name("Parent2")
                .compKey("parent2")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.realizes(parent2)

        ns.addType(data)
        ns.addType(parent)
        ns.addType(parent2)
        file.addType(data)
        file.addType(parent)
        file.addType(parent2)

        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public class Test implements Parent, Parent2 {

}
"""
        ctx.typeGen.init(type: data)
        String observed = ctx.typeGen.generate().stripIndent()

        assertEquals(expected, observed)
    }

    @Test
    void "generate a class that both extends and implements"() {
        data = Type.builder().type(Type.CLASS)
                .name("Test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Type parent = Type.builder().type(Type.CLASS)
                .name("Parent")
                .compKey("parent")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.generalizedBy(parent)
        Type parent2 = Type.builder().type(Type.INTERFACE)
                .name("Parent2")
                .compKey("parent2")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.realizes(parent2)
        Type parent3 = Type.builder().type(Type.INTERFACE)
                .name("Parent3")
                .compKey("parent3")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.realizes(parent3)

        ns.addType(data)
        ns.addType(parent)
        file.addType(data)
        file.addType(parent)

        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public class Test extends Parent implements Parent2, Parent3 {

}
"""
        ctx.typeGen.init(type: data)
        String observed = ctx.typeGen.generate().stripIndent()

        assertEquals(expected, observed)
    }

    @Test
    void "generate an interface that extends another inteface"() {
        data = Type.builder().type(Type.INTERFACE)
                .name("Test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Type parent = Type.builder().type(Type.INTERFACE)
                .name("Parent")
                .compKey("parent")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.generalizedBy(parent)
        Type parent2 = Type.builder().type(Type.INTERFACE)
                .name("Parent2")
                .compKey("parent2")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.generalizedBy(parent2)
        Type parent3 = Type.builder().type(Type.INTERFACE)
                .name("Parent3")
                .compKey("parent3")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.generalizedBy(parent3)

        ns.addType(data)
        ns.addType(parent)
        ns.addType(parent2)
        ns.addType(parent3)
        file.addType(data)
        file.addType(parent)
        file.addType(parent2)
        file.addType(parent3)

        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public interface Test extends Parent2, Parent, Parent3 {

}
"""
        ctx.typeGen.init(type: data)
        String observed = ctx.typeGen.generate().stripIndent()

        assertEquals(expected, observed)
    }

    @Test
    void "generate an enum that implements an interface"() {
        data = Type.builder().type(Type.ENUM)
                .name("Test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Type parent = Type.builder().type(Type.INTERFACE)
                .name("Parent")
                .compKey("parent")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.realizes(parent)

        ns.addType(data)
        ns.addType(parent)
        file.addType(data)
        file.addType(parent)

        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public enum Test implements Parent {

}
"""
        ctx.typeGen.init(type: data)
        String observed = ctx.typeGen.generate().stripIndent()

        assertEquals(expected, observed)
    }

    @Test
    void "generate an enum that implements multiple interfaces"() {
        data = Type.builder().type(Type.ENUM)
                .name("Test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Type parent = Type.builder().type(Type.INTERFACE)
                .name("Parent")
                .compKey("parent")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.realizes(parent)
        Type parent2 = Type.builder().type(Type.INTERFACE)
                .name("Parent2")
                .compKey("parent2")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.realizes(parent2)
        Type parent3 = Type.builder().type(Type.INTERFACE)
                .name("Parent3")
                .compKey("parent3")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.realizes(parent3)

        ns.addType(data)
        ns.addType(parent)
        ns.addType(parent2)
        ns.addType(parent3)
        file.addType(data)
        file.addType(parent)
        file.addType(parent2)
        file.addType(parent3)

        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public enum Test implements Parent, Parent2, Parent3 {

}
"""
        ctx.typeGen.init(type: data)
        String observed = ctx.typeGen.generate().stripIndent()

        assertEquals(expected, observed)
    }

//    @Test
//    void "test generate with pgcl and disregard"() {
//        edu.isu.isuese.datamodel.File file = edu.isu.isuese.datamodel.File.builder()
//                .name("file")
//                .fileKey("file")
//                .create()
//        data = Type.builder().type(Type.CLASS)
//                .name("Test")
//                .compKey("test")
//                .accessibility(Accessibility.PUBLIC)
//                .create()
//        file.addType(data)
//
//        edu.montana.gsoc.msusel.rbml.model.Role role = ClassRole.builder()
//                .name("test")
//                .create()
//        ctx.rbmlManager.addMapping(role, data)
//
//        CueRole cueRole = new CueRole()
//        cueRole.disregard = true
//        cueRole.content = "Testing"
//
//        Cue cue = new Cue()
//        cue.name = "Test"
//        cue.roles = ["test": cueRole]
//        ctx.cue = cue
//
//        ctx.typeGen.init(type: data, parent: file)
//        String observed = ctx.typeGen.generate()
//        String expected = ""
//
//        assertEquals(expected, observed)
//    }
//
//    @Test
//    void "test generate with pgcl and content"() {
//        edu.isu.isuese.datamodel.File file = edu.isu.isuese.datamodel.File.builder()
//                .name("file")
//                .fileKey("file")
//                .create()
//        data = Type.builder().type(Type.CLASS)
//                .name("Test")
//                .compKey("test")
//                .accessibility(Accessibility.PUBLIC)
//                .create()
//        file.addType(data)
//
//        edu.montana.gsoc.msusel.rbml.model.Role role = ClassRole.builder()
//                .name("test")
//                .create()
//        ctx.rbmlManager.addMapping(role, data)
//
//        CueRole cueRole = new CueRole()
//        cueRole.disregard = false
//        cueRole.content = "Testing"
//
//        Cue cue = new Cue()
//        cue.name = "Test"
//        cue.roles = ["test": cueRole]
//        ctx.cue = cue
//
//        ctx.typeGen.init(type: data, parent: file)
//        String observed = ctx.typeGen.generate().stripIndent(8)
//        String expected = """\
///**
// * Generated Class
// *
// * @author Isaac Griffith
// * @version 1.0
// */
//public class Test {
//    Testing
//}
//"""
//
//        assertEquals(expected, observed)
//    }
//
//    @Test
//    void "test generate with pgcl and definition"() {
//        edu.isu.isuese.datamodel.File file = edu.isu.isuese.datamodel.File.builder()
//                .name("file")
//                .fileKey("file")
//                .create()
//        data = Type.builder().type(Type.CLASS)
//                .name("Test")
//                .compKey("test")
//                .accessibility(Accessibility.PUBLIC)
//                .create()
//        file.addType(data)
//
//        edu.montana.gsoc.msusel.rbml.model.Role role = ClassRole.builder()
//                .name("test")
//                .create()
//        ctx.rbmlManager.addMapping(role, data)
//
//        CueRole cueRole = new CueRole()
//        cueRole.disregard = false
//        cueRole.definition = "Testing"
//
//        Cue cue = new Cue()
//        cue.name = "Test"
//        cue.roles = ["test": cueRole]
//        ctx.cue = cue
//
//        ctx.typeGen.init(type: data, parent: file)
//        String observed = ctx.typeGen.generate()
//        String expected = "Testing"
//
//        assertEquals(expected, observed)
//    }

    @Test
    void "Gen Methods with Cue"() {
        given:
        // build up components for State Context
        System sys = System.builder().name("test2").key("test2").basePath(".").create()
        Project project = Project.builder().name("test").relPath("test").projKey("test").create()
        sys.addProject(project)
        Namespace ns = Namespace.builder().name("test").nsKey("test").relPath("test").create()
        project.addNamespace(ns)
        edu.isu.isuese.datamodel.File file = edu.isu.isuese.datamodel.File.builder().name("Context.java").relPath("Context.java").fileKey("Context.java").create()
        project.addFile(file)
        Type context = Type.builder().type(Type.CLASS).name("Context").compKey("Context").accessibility(Accessibility.PUBLIC).create()
        file.addType(context)
        ns.addType(context)
        Type other = Type.builder().type(Type.CLASS).name("Other").compKey("Other").accessibility(Accessibility.PUBLIC).create()
        file.addType(other)
        ns.addType(other)
        Field cs = Field.builder()
                .name("testCurrState")
                .compKey("testCurrState")
                .type(other.createTypeRef())
                .accessibility(Accessibility.PRIVATE)
                .create()
        context.addMember(cs)
        Method req = Method.builder()
                .name("testRequest")
                .compKey("testRequest")
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .accessibility(Accessibility.PUBLIC)
                .create()
        context.addMember(req)
        sys.updateKeys()
        req.refresh()
        cs.refresh()
        other.refresh()
        context.refresh()
        file.refresh()
        ns.refresh()
        project.refresh()
        sys.refresh()

        String expected = """\

                
            /**
             * @return the value of testCurrState
             */
            public Other getTestCurrState() {
                return testCurrState;
            }
                
            /**
             * @param testCurrState the new value for testCurrState
             */
            public void setTestCurrState(Other testCurrState) {
                this.testCurrState = testCurrState;
            }"""

        SPS sps = ctx.loader.loadPattern("state")
        ctx.loader.loadPatternCues("state", "java")
        CueManager.instance.selectCue()
        Cue cue = CueManager.instance.getCurrent().getCueForRole("Context", context)
        ctx.rbmlManager.addMapping(sps.getClassifierByName("Context").getStructuralFeatureByName("currentState"), cs)
        ctx.rbmlManager.addMapping(sps.getClassifierByName("Context").getBehavioralFeatureByName("Request"), req)
        ctx.rbmlManager.addMapping(sps.getClassifierByName("Context"), context)

        sps.getClassifierByName("Context").structFeats.each {
            println "Structural Feature Name: ${it.name}"
        }
        sps.getClassifierByName("Context").behFeats.each {
            println "Behavioral Feature Name: ${it.name}"
        }

        when:
        String actual = ((JavaTypeGenerator) ctx.typeGen).genMethods(context)

        then:
        the actual shouldBeEqual expected
    }
}
