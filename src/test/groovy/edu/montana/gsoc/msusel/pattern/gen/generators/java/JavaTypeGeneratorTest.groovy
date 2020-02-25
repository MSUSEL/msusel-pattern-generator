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
import edu.montana.gsoc.msusel.pattern.cue.Cue
import edu.montana.gsoc.msusel.pattern.cue.CueRole
import edu.montana.gsoc.msusel.pattern.gen.GeneratorContext
import edu.montana.gsoc.msusel.rbml.model.ClassRole
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

    @Before
    void setup() {
        ctx = GeneratorContext.instance
        ctx.resetPatternBuilderComponents()
        ctx.plugin = new JavaLanguageProvider()
        ctx.resetComponentGenerators()
        testDir.mkdirs()
        builder = new FileTreeBuilder(testDir)
    }

    @After
    void cleanup() {
        if (testDir.exists())
            testDir.deleteDir()
    }

    @Test
    void "generate an empty class"() {
        data = Class.builder().name("Test").accessibility(Accessibility.PUBLIC).create()

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
        data = Enum.builder().name("Test").accessibility(Accessibility.PUBLIC).create()

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
        data = Interface.builder().name("Test").accessibility(Accessibility.PUBLIC).create()

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
        data = Class.builder()
                .name("Test")
                .accessibility(Accessibility.PUBLIC)
                .create()

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
        data = Class.builder()
                .name("Test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.addModifier(Modifier.forName("STATIC"))

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
        data = Class.builder()
                .name("Test")
                .accessibility(Accessibility.PRIVATE)
                .create()

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
        data = Class.builder()
                .name("Test")
                .accessibility(Accessibility.DEFAULT)
                .create()

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
        data = Class.builder()
                .name("Test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.addModifier(Modifier.forName("FINAL"))

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
        data = Class.builder()
                .name("Test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Method m = Method.builder()
                .name("test")
                .accessibility(Accessibility.PUBLIC)
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .create()
        data.addMember(m)

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
        data = Class.builder()
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
        data = Class.builder()
                .name("Test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Field f = Field.builder()
                .name("test")
                .accessibility(Accessibility.PRIVATE)
                .create()
        f.setType(TypeRef.createPrimitiveTypeRef("int"))
        data.addMember(f)

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
        data = Class.builder()
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
        data = Interface.builder()
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
        data = Interface.builder()
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
        data = Interface.builder()
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
        data = Enum.builder()
                .name("Test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Literal l = Literal.builder()
                .name("test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.addMember(l)

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
        data = Enum.builder()
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
        data = Class.builder()
                .name("Test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Class parent = Class.builder()
                .name("Parent")
                .compKey("parent")
                .accessibility(Accessibility.PUBLIC)
                .create()
        parent.addModifier(Modifier.forName("ABSTRACT"))
        data.generalizedBy(parent)

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
        data = Class.builder()
                .name("Test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Interface parent = Interface.builder()
                .name("Parent")
                .compKey("parent")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.realizes(parent)

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
        data = Class.builder()
                .name("Test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Interface parent = Interface.builder()
                .name("Parent")
                .compKey("parent")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.realizes(parent)
        Interface parent2 = Interface.builder()
                .name("Parent2")
                .compKey("parent2")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.realizes(parent2)

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
        data = Class.builder()
                .name("Test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Class parent = Class.builder()
                .name("Parent")
                .compKey("parent")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.generalizedBy(parent)
        Interface parent2 = Interface.builder()
                .name("Parent2")
                .compKey("parent2")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.realizes(parent2)
        Interface parent3 = Interface.builder()
                .name("Parent3")
                .compKey("parent3")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.realizes(parent3)

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
        data = Interface.builder()
                .name("Test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Interface parent = Interface.builder()
                .name("Parent")
                .compKey("parent")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.generalizedBy(parent)
        Interface parent2 = Interface.builder()
                .name("Parent2")
                .compKey("parent2")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.generalizedBy(parent2)
        Interface parent3 = Interface.builder()
                .name("Parent3")
                .compKey("parent3")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.generalizedBy(parent3)

        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public interface Test extends Parent, Parent2, Parent3 {
}
"""
        ctx.typeGen.init(type: data)
        String observed = ctx.typeGen.generate().stripIndent()

        assertEquals(expected, observed)
    }

    @Test
    void "generate an enum that implements an interface"() {
        data = Enum.builder()
                .name("Test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Interface parent = Interface.builder()
                .name("Parent")
                .compKey("parent")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.realizes(parent)

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
        data = Enum.builder()
                .name("Test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        Interface parent = Interface.builder()
                .name("Parent")
                .compKey("parent")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.realizes(parent)
        Interface parent2 = Interface.builder()
                .name("Parent2")
                .compKey("parent2")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.realizes(parent2)
        Interface parent3 = Interface.builder()
                .name("Parent3")
                .compKey("parent3")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.realizes(parent3)

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

    @Test
    void "test generate with pgcl and disregard"() {
        edu.isu.isuese.datamodel.File file = edu.isu.isuese.datamodel.File.builder()
                .name("file")
                .fileKey("file")
                .create()
        data = Class.builder()
                .name("Test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        file.addType(data)

        edu.montana.gsoc.msusel.rbml.model.Role role = ClassRole.builder()
                .name("test")
                .create()
        ctx.rbmlManager.addMapping(role, data)

        CueRole cueRole = new CueRole()
        cueRole.disregard = true
        cueRole.content = "Testing"

        Cue cue = new Cue()
        cue.name = "Test"
        cue.roles = ["test": cueRole]
        ctx.cue = cue

        ctx.typeGen.init(type: data, parent: file)
        String observed = ctx.typeGen.generate()
        String expected = ""

        assertEquals(expected, observed)
    }

    @Test
    void "test generate with pgcl and content"() {
        edu.isu.isuese.datamodel.File file = edu.isu.isuese.datamodel.File.builder()
                .name("file")
                .fileKey("file")
                .create()
        data = Class.builder()
                .name("Test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        file.addType(data)

        edu.montana.gsoc.msusel.rbml.model.Role role = ClassRole.builder()
                .name("test")
                .create()
        ctx.rbmlManager.addMapping(role, data)

        CueRole cueRole = new CueRole()
        cueRole.disregard = false
        cueRole.content = "Testing"

        Cue cue = new Cue()
        cue.name = "Test"
        cue.roles = ["test": cueRole]
        ctx.cue = cue

        ctx.typeGen.init(type: data, parent: file)
        String observed = ctx.typeGen.generate().stripIndent(8)
        String expected = """\
/**
 * Generated Class
 *
 * @author Isaac Griffith
 * @version 1.0
 */
public class Test {
    Testing
}
"""

        assertEquals(expected, observed)
    }

    @Test
    void "test generate with pgcl and definition"() {
        edu.isu.isuese.datamodel.File file = edu.isu.isuese.datamodel.File.builder()
                .name("file")
                .fileKey("file")
                .create()
        data = Class.builder()
                .name("Test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        file.addType(data)

        edu.montana.gsoc.msusel.rbml.model.Role role = ClassRole.builder()
                .name("test")
                .create()
        ctx.rbmlManager.addMapping(role, data)

        CueRole cueRole = new CueRole()
        cueRole.disregard = false
        cueRole.definition = "Testing"

        Cue cue = new Cue()
        cue.name = "Test"
        cue.roles = ["test": cueRole]
        ctx.cue = cue

        ctx.typeGen.init(type: data, parent: file)
        String observed = ctx.typeGen.generate()
        String expected = "Testing"

        assertEquals(expected, observed)
    }
}
