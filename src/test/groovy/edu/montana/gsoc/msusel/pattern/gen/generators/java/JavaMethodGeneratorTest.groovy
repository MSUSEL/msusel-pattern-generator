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
package edu.montana.gsoc.msusel.pattern.gen.generators.java

import edu.isu.isuese.datamodel.*
import edu.montana.gsoc.msusel.pattern.gen.GeneratorContext
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals

class JavaMethodGeneratorTest extends DBSpec {

    Method data
    GeneratorContext ctx
    Type type
    Project project
    Namespace ns
    File file

    @Before
    void setup() {
        ctx = GeneratorContext.instance
        ctx.resetPatternBuilderComponents()
        ctx.plugin = new JavaLanguageProvider()
        ctx.resetComponentGenerators()

        type = Type.builder().type(Type.CLASS)
                .name("Type")
                .accessibility(Accessibility.PUBLIC)
                .compKey("type")
                .create()

        project = Project.builder()
                .name("Test")
                .projKey("Test")
                .version("1.0")
                .create()
        project.save()

        System sys = System.builder().name("test").key("test").create()

        ns = Namespace.builder().name("test").nsKey("test").relPath("test").create()

        file = File.builder().type(FileType.SOURCE).name("Test.java").fileKey("Test.java").relPath("Test.java").create()

        ns.save()
        project.addNamespace(ns)
        project.addFile(file)
        type.save()
        ns.addType(type)
        file.addType(type)
        sys.addProject(project)
        sys.updateKeys()
        type.refresh()
        ns.refresh()
        project.refresh()
    }

    @After
    void teardown() {

    }

    @Test
    void "test constructor"() {
        data = Constructor.creator()
                .name("Test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .create()

        type.addMember(data)

        ctx.methodGen.init(method: data)
        String observed = ctx.methodGen.generate().stripIndent(8)
        String expected = """
    /**
     * Construct a new Test instance
     */
    public Test() {
    }"""

        assertEquals(expected, observed)
    }

    @Test
    void "test destructor"() {
        data = Destructor.creator()
                .name("Test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .create()

        type.addMember(data)

        ctx.methodGen.init(method: data)
        String observed = ctx.methodGen.generate().stripIndent(8)
        String expected = """
    /**
     *
     */
    @Override
    protected void finalize() throws Throwable {
    }"""

        assertEquals(expected, observed)
    }

    @Test
    void "test normal method"() {
        data = Method.builder()
                .name("test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .create()

        type.addMember(data)

        ctx.methodGen.init(method: data)
        String observed = ctx.methodGen.generate().stripIndent(8)
        String expected = """
    /**
     *
     */
    public void test() {
    }"""

        assertEquals(expected, observed)
    }

    @Test
    void "generate an abstract method"() {
        data = Method.builder()
                .name("test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .create()
        data.addModifier(Modifier.forName("ABSTRACT"))

        type.addMember(data)

        ctx.methodGen.init(method: data)
        String observed = ctx.methodGen.generate().stripIndent(8)
        String expected = """
    /**
     *
     */
    public abstract void test();"""

        assertEquals(expected, observed)
    }

    @Test
    void "generate public method"() {
        data = Method.builder()
                .name("test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .create()

        type.addMember(data)

        ctx.methodGen.init(method: data)
        String observed = ctx.methodGen.generate().stripIndent(8)
        String expected = """
    /**
     *
     */
    public void test() {
    }"""

        assertEquals(expected, observed)
    }

    @Test
    void "generate private method"() {
        data = Method.builder()
                .name("test")
                .compKey("test")
                .accessibility(Accessibility.PRIVATE)
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .create()

        type.addMember(data)

        ctx.methodGen.init(method: data)
        String observed = ctx.methodGen.generate().stripIndent(8)
        String expected = """
    /**
     *
     */
    private void test() {
    }"""

        assertEquals(expected, observed)
    }

    @Test
    void "generate protected method"() {
        data = Method.builder()
                .name("test")
                .compKey("test")
                .accessibility(Accessibility.PROTECTED)
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .create()

        type.addMember(data)

        ctx.methodGen.init(method: data)
        String observed = ctx.methodGen.generate().stripIndent(8)
        String expected = """
    /**
     *
     */
    protected void test() {
    }"""

        assertEquals(expected, observed)
    }

    @Test
    void "generate default method"() {
        data = Method.builder()
                .name("test")
                .compKey("test")
                .accessibility(Accessibility.DEFAULT)
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .create()

        type.addMember(data)

        ctx.methodGen.init(method: data)
        String observed = ctx.methodGen.generate().stripIndent(8)
        String expected = """
    /**
     *
     */
    void test() {
    }"""

        assertEquals(expected, observed)
    }

    @Test
    void "generate final method"() {
        data = Method.builder()
                .name("test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .create()
        data.addModifier(Modifier.forName("FINAL"))

        type.addMember(data)

        ctx.methodGen.init(method: data)
        String observed = ctx.methodGen.generate().stripIndent(8)
        String expected = """
    /**
     *
     */
    public final void test() {
    }"""

        assertEquals(expected, observed)
    }

    @Test
    void "generate static method"() {
        data = Method.builder()
                .name("test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .create()
        data.addModifier(Modifier.forName("STATIC"))

        type.addMember(data)

        ctx.methodGen.init(method: data)
        String observed = ctx.methodGen.generate().stripIndent(8)
        String expected = """
    /**
     *
     */
    public static void test() {
    }"""

        assertEquals(expected, observed)
    }

    @Test
    void "generate native method"() {
        data = Method.builder()
                .name("test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .create()
        data.addModifier(Modifier.forName("NATIVE"))

        type.addMember(data)

        ctx.methodGen.init(method: data)
        String observed = ctx.methodGen.generate().stripIndent(8)
        String expected = """
    /**
     *
     */
    public native void test() {
    }"""

        assertEquals(expected, observed)
    }

    @Test
    void "generate static native method"() {
        data = Method.builder()
                .name("test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .create()
        data.addModifier(Modifier.forName("STATIC"))
        data.addModifier(Modifier.forName("NATIVE"))

        type.addMember(data)

        ctx.methodGen.init(method: data)
        String observed = ctx.methodGen.generate().stripIndent(8)
        String expected = """
    /**
     *
     */
    public static native void test() {
    }"""

        assertEquals(expected, observed)
    }

    @Test
    void "generate method with one param"() {
        data = Method.builder()
                .name("test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .create()
        Parameter param = Parameter.builder()
                .name("param")
                .type(TypeRef.createPrimitiveTypeRef("int"))
                .create()
        data.addParameter(param)

        type.addMember(data)

        ctx.methodGen.init(method: data)
        String observed = ctx.methodGen.generate().stripIndent(8)
        String expected = """
    /**
     *
     * @param param
     */
    public void test(int param) {
    }"""

        assertEquals(expected, observed)
    }

    @Test
    void "generate method with multiple params"() {
        data = Method.builder()
                .name("test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .create()
        Parameter param = Parameter.builder()
                .name("param")
                .type(TypeRef.createPrimitiveTypeRef("int"))
                .create()
        data.addParameter(param)
        Parameter param2 = Parameter.builder()
                .name("param2")
                .type(TypeRef.createPrimitiveTypeRef("float"))
                .create()
        data.addParameter(param2)

        type.addMember(data)

        ctx.methodGen.init(method: data)
        String observed = ctx.methodGen.generate().stripIndent(8)
        String expected = """
    /**
     *
     * @param param
     * @param param2
     */
    public void test(int param, float param2) {
    }"""

        assertEquals(expected, observed)
    }

    @Test
    void "generate method with final parameter"() {
        data = Method.builder()
                .name("test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .create()
        Parameter param = Parameter.builder()
                .name("param")
                .type(TypeRef.createPrimitiveTypeRef("int"))
                .create()
        param.addModifier(Modifier.forName("FINAL"))
        data.addParameter(param)

        type.addMember(data)

        ctx.methodGen.init(method: data)
        String observed = ctx.methodGen.generate().stripIndent(8)
        String expected = """
    /**
     *
     * @param param
     */
    public void test(final int param) {
    }"""

        assertEquals(expected, observed)
    }

    @Test
    void "generate method with return type"() {
        data = Method.builder()
                .name("test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .type(TypeRef.createPrimitiveTypeRef("int"))
                .create()

        type.addMember(data)

        ctx.methodGen.init(method: data)
        String observed = ctx.methodGen.generate().stripIndent(8)
        String expected = """
    /**
     *
     * @return
     */
    public int test() {
    }"""

        assertEquals(expected, observed)
    }

    @Test
    void "generate method with void return type"() {
        data = Method.builder()
                .name("test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .create()

        type.addMember(data)

        ctx.methodGen.init(method: data)
        String observed = ctx.methodGen.generate().stripIndent(8)
        String expected = """
    /**
     *
     */
    public void test() {
    }"""

        assertEquals(expected, observed)
    }

    @Test
    void "generate for accessor and mutator for field"() {
        Field field = Field.builder()
                .name("field")
                .compKey("field")
                .create()
        field.setType(TypeRef.createPrimitiveTypeRef("int"))

        type.addMember(field)

        ctx.methodGen.init(field: field)
        String observed = ctx.methodGen.generate().stripIndent(8)
        String expected = """
    /**
     * @return the value of field
     */
    public int getField() {
        return field;
    }

    /**
     * @param field the new value for field
     */
    public void setField(int field) {
        this.field = field;
    }"""

        assertEquals(expected, observed)
    }

    @Test
    void "generate for accessor and mutator of boolean field"() {
        Field field = Field.builder()
                .name("field")
                .compKey("field")
                .create()
        field.setType(TypeRef.createPrimitiveTypeRef("boolean"))

        type.addMember(field)

        ctx.methodGen.init(field: field)
        String observed = ctx.methodGen.generate().stripIndent(8)
        String expected = """
    /**
     * @return the value of field
     */
    public boolean isField() {
        return field;
    }

    /**
     * @param field the new value for field
     */
    public void setField(boolean field) {
        this.field = field;
    }"""

        assertEquals(expected, observed)
    }

    @Test
    void "generate accessors and mutators for static field"() {
        Field field = Field.builder()
                .name("field")
                .compKey("field")
                .create()
        field.setType(TypeRef.createPrimitiveTypeRef("int"))
        field.addModifier(Modifier.forName("STATIC"))

        type.addMember(field)

        ctx.methodGen.init(field: field)
        String observed = ctx.methodGen.generate().stripIndent(8)
        String expected = """
    /**
     * @return the value of field
     */
    public static int getField() {
        return field;
    }

    /**
     * @param field the new value for field
     */
    public static void setField(int field) {
        this.field = field;
    }"""

        assertEquals(expected, observed)
    }

    @Test
    void "generate accessors and mutators for final field"() {
        Field field = Field.builder()
                .name("field")
                .compKey("field")
                .create()
        field.setType(TypeRef.createPrimitiveTypeRef("int"))
        field.addModifier(Modifier.forName("FINAL"))

        type.addMember(field)

        ctx.methodGen.init(field: field)
        String observed = ctx.methodGen.generate().stripIndent(8)
        String expected = """
    /**
     * @return the value of field
     */
    public int getField() {
        return field;
    }"""

        assertEquals(expected, observed)
    }

    @Test(expected = IllegalArgumentException.class)
    void "generate for null field"() {
        Field f = null
        ctx.methodGen.init(field: f)
        ctx.methodGen.generate()
    }

    @Test(expected = IllegalArgumentException.class)
    void "generate method from null"() {
        data = null
        ctx.methodGen.init(method: data)
        ctx.methodGen.generate()
    }

    @Test
    void "generate method that throws an exception"() {
        data = Method.builder()
                .name("test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .create()
        data.addException(TypeRef.createPrimitiveTypeRef("IllegalArgumentException"))

        type.addMember(data)

        ctx.methodGen.init(method: data)
        String observed = ctx.methodGen.generate().stripIndent(8)
        String expected = """
    /**
     *
     * @throws IllegalArgumentException
     */
    public void test() throws IllegalArgumentException {
    }"""

        assertEquals(expected, observed)
    }

    @Test
    void "generate method that throws multiple exceptions"() {
        data = Method.builder()
                .name("test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .create()
        data.addException(TypeRef.createPrimitiveTypeRef("IllegalArgumentException"))
        data.addException(TypeRef.createPrimitiveTypeRef("NullPointerException"))

        type.addMember(data)

        ctx.methodGen.init(method: data)
        String observed = ctx.methodGen.generate().stripIndent(8)
        String expected = """
    /**
     *
     * @throws IllegalArgumentException
     * @throws NullPointerException
     */
    public void test() throws IllegalArgumentException, NullPointerException {
    }"""

        assertEquals(expected, observed)
    }

    @Test
    void "generate an overriding method"() {
        Type t1 = Type.builder().type(Type.CLASS).name("class1").compKey("class1").create()
        Type t2 = Type.builder().type(Type.CLASS).name("class2").compKey("class2").create()
        t2.generalizes(t1)
        Method data2 = Method.builder()
                .name("test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .create()
        t1.addMember(data2)
        file.addType(t1)
        ns.addType(t1)

        data = Method.builder()
                .name("test")
                .compKey("test")
                .accessibility(Accessibility.PUBLIC)
                .type(TypeRef.createPrimitiveTypeRef("void"))
                .create()
        t2.addMember(data)
        file.addType(t2)
        ns.addType(t2)

        ctx.methodGen.init(method: data)
        String observed = ctx.methodGen.generate().stripIndent(8)
        String expected = """
    /**
     *
     */
    public void test() {
    }"""

        assertEquals(expected, observed)
    }

//    @Test
//    void "test generate with pgcl with disregard flag"() {
//        Type t1 = Type.builder().type(Type.CLASS).name("class1").compKey("class1").create()
//
//        Method data = Method.builder()
//                .name("test")
//                .compKey("test")
//                .accessibility(Accessibility.PUBLIC)
//                .type(TypeRef.createPrimitiveTypeRef("void"))
//                .create()
//        t1.addMember(data)
//
//        Role role = BehavioralFeature.builder()
//                .name("test")
//                .create()
//        ctx.rbmlManager.addMapping(role, data)
//
//        CueRole cueRole = new CueRole()
//        cueRole.disregard = true
//        cueRole.definition = "Testing"
//
//        Cue cue = new Cue()
//        cue.name = "Test"
//        cue.roles = ["test": cueRole]
//        ctx.cue = cue
//
//        ctx.methodGen.init(method: data)
//        String observed = ctx.methodGen.generate().stripIndent(8)
//        String expected = ""
//
//        assertEquals(expected, observed)
//    }
//
//    @Test
//    void "test generate with pgcl with definition"() {
//        Type t1 = Type.builder().type(Type.CLASS).name("class1").compKey("class1").create()
//
//        Method data = Method.builder()
//                .name("test")
//                .compKey("test")
//                .accessibility(Accessibility.PUBLIC)
//                .type(TypeRef.createPrimitiveTypeRef("void"))
//                .create()
//        t1.addMember(data)
//
//        Role role = BehavioralFeature.builder()
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
//        ctx.methodGen.init(method: data, parent: t1)
//        String observed = ctx.methodGen.generate()
//        String expected = "Testing"
//
//        assertEquals(expected, observed)
//    }
//
//    @Test
//    void "test generate with pgcl with content"() {
//        Type t1 = Type.builder().type(Type.CLASS).name("class1").compKey("class1").create()
//
//        Method data = Method.builder()
//                .name("test")
//                .compKey("test")
//                .accessibility(Accessibility.PUBLIC)
//                .type(TypeRef.createPrimitiveTypeRef("void"))
//                .create()
//        t1.addMember(data)
//
//        Role role = BehavioralFeature.builder()
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
//        ctx.methodGen.init(method: data)
//        String observed = ctx.methodGen.generate().stripIndent(8)
//        String expected = """
//    /**
//     *
//     */
//    public void test() {
//        Testing
//    }"""
//
//        assertEquals(expected, observed)
//    }
}
