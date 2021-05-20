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
import org.javalite.activejdbc.test.DBSpec
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class JavaFileGeneratorTest extends DBSpec {

    GeneratorContext ctx
    File data
    java.io.File testDir
    FileTreeBuilder builder
    Project proj

    @Before
    void setup() {
        testDir = new java.io.File("testdir")
        testDir.mkdirs()
        builder = new FileTreeBuilder(testDir)
        ctx = GeneratorContext.instance
        ctx.resetPatternBuilderComponents()
        ctx.plugin = new JavaLanguageProvider()
        ctx.resetComponentGenerators()

        data = File.builder()
                .type(FileType.SOURCE)
                .name("testdir/Test.java")
                .fileKey("testdir/Test.java")
                .create()
        proj = Project.builder()
                .version("1.0")
                .name("project")
                .projKey("test")
                .create()
        proj.saveIt()
        proj.addFile(data)
    }

    @After
    void teardown() {
        if (testDir.exists())
            testDir.deleteDir()
    }

    @Test
    void "a class with known imports"() {
        Namespace ns1 = Namespace.builder()
                .nsKey("ns1")
                .name("ns1")
                .create()
        Namespace ns2 = Namespace.builder()
                .nsKey("ns1.ns2")
                .name("ns1.ns2")
                .create()
        ns1.addNamespace(ns2)
        ns2.addFile(data)
        proj.addFile(data)
        proj.addNamespace(ns1)
        proj.addNamespace(ns2)

        Class clazz = Class.builder()
                .name("Test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.addType(clazz)

        Import imp = Import.builder().name("java.util.*").create()
        data.addImport(imp)

        def expected = """\
        /**
         * MIT License
         *
         * MSUSEL Design Pattern Generator
         * Copyright (c) 2015-2020 Montana State University, Gianforte School of Computing,
         * Software Engineering Laboratory and Idaho State University, Informatics and
         * Computer Science, eXtended Reality and Empirical Software Engineering Laboratory
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
                
        package ns1.ns2;
        
        import java.util.*;
        
        /**
         * Generated Class
         *
         * @author Isaac Griffith
         * @version 1.0
         */
        public class Test {
        
        }
        
        """.stripIndent()

        ctx.fileGen.init(file: data, builder: builder)
        ctx.fileGen.generate()
        java.io.File created = new java.io.File(testDir, "Test.java")

        a(testDir.exists()).shouldBeTrue()
        a(created.exists()).shouldBeTrue()
        the(created.text).shouldEqual(expected)
    }

    @Test
    void "a class with method imports"() {
        Namespace ns1 = Namespace.builder()
                .nsKey("ns1")
                .name("ns1")
                .create()
        Namespace ns2 = Namespace.builder()
                .nsKey("ns1.ns2")
                .name("ns1.ns2")
                .create()
        ns1.addNamespace(ns2)
        ns2.addFile(data)
        proj.addNamespace(ns1)
        proj.addNamespace(ns2)

        Class clazz = Class.builder()
                .name("Test")
                .compKey("Test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.addType(clazz)
        ns2.addType(clazz)

        File other = File.builder()
                .type(FileType.SOURCE)
                .name("Reference.java")
                .fileKey("Reference.java")
                .create()

        Class ref = Class.builder()
                .name("Reference")
                .accessibility(Accessibility.PUBLIC)
                .compKey("Reference")
                .create()
        other.addType(ref)
        ns1.addType(ref)
        ns1.addFile(other)
        proj.addFile(other)

        Method method = Method.builder()
                .name("method")
                .compKey("method")
                .type(TypeRef.builder()
                        .type(TypeRefType.Type)
                        .typeName(ref.name)
                        .typeFullName(ref.getFullName())
                        .ref(Reference.builder()
                                .refKey(ref.getRefKey())
                                .refType(RefType.TYPE)
                                .create())
                        .create())
                .create()
        clazz.addMember(method)

        Module mod = Module.builder()
                .name("module")
                .moduleKey("module")
                .create()
        proj.addModule(mod)
        mod.addNamespace(ns1)

        def expected = """\
        /**
         * MIT License
         *
         * MSUSEL Design Pattern Generator
         * Copyright (c) 2015-2020 Montana State University, Gianforte School of Computing,
         * Software Engineering Laboratory and Idaho State University, Informatics and
         * Computer Science, eXtended Reality and Empirical Software Engineering Laboratory
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
        
        package ns1.ns2;
        
        import ns1.Reference;
        import java.util.*;
        
        /**
         * Generated Class
         *
         * @author Isaac Griffith
         * @version 1.0
         */
        public class Test {
        
            /**
             *
             * @return
             */
            public Reference method() {
            }
        
        }
        
        """.stripIndent()

        ctx.fileGen.init(file: data, builder: builder)
        ctx.fileGen.generate()
        java.io.File created = new java.io.File(testDir, "Test.java")

        a(testDir.exists()).shouldBeTrue()
        a(created.exists()).shouldBeTrue()
        Assert.assertEquals(created.text, expected)
    }

    @Test
    void "a file with field imports"() {
        Namespace ns1 = Namespace.builder()
                .nsKey("ns1")
                .name("ns1")
                .create()
        Namespace ns2 = Namespace.builder()
                .nsKey("ns1.ns2")
                .name("ns1.ns2")
                .create()
        ns1.addNamespace(ns2)
        ns2.addFile(data)

        proj.addNamespace(ns1)
        proj.addNamespace(ns2)

        Class clazz = Class.builder()
                .name("Test")
                .compKey("Test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.addType(clazz)
        ns2.addType(clazz)

        File other = File.builder()
                .type(FileType.SOURCE)
                .name("Reference.java")
                .fileKey("Reference.java")
                .create()

        Class ref = Class.builder()
                .name("Reference")
                .accessibility(Accessibility.PUBLIC)
                .compKey("Reference")
                .create()
        other.addType(ref)
        ns1.addFile(other)
        ns1.addType(ref)
        proj.updateKeys()
        proj.refresh()
        ns1.refresh()
        ref.refresh()

        Field method = Field.builder()
                .name("ref")
                .compKey("ref")
                .accessibility(Accessibility.PRIVATE)
                .type(TypeRef.builder()
                        .type(TypeRefType.Type)
                        .typeName(ref.name)
                        .typeFullName(ref.getFullName())
                        .ref(ref.createReference())
                        .create())
                .create()
        clazz.addMember(method)

        Module mod = Module.builder()
                .name("module")
                .moduleKey("module")
                .create()
        proj.addModule(mod)
        mod.addNamespace(ns1)

        def expected = """\
        /**
         * MIT License
         *
         * MSUSEL Design Pattern Generator
         * Copyright (c) 2015-2020 Montana State University, Gianforte School of Computing,
         * Software Engineering Laboratory and Idaho State University, Informatics and
         * Computer Science, eXtended Reality and Empirical Software Engineering Laboratory
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
                
        package ns1.ns2;
        
        import ns1.Reference;
        import java.util.*;
        
        /**
         * Generated Class
         *
         * @author Isaac Griffith
         * @version 1.0
         */
        public class Test {
        
            private Reference ref;
            
            /**
             * @return the value of ref
             */
            public Reference getRef() {
                return ref;
            }
        
            /**
             * @param ref the new value for ref
             */
            public void setRef(Reference ref) {
                this.ref = ref;
            }
        }
        """.stripIndent()

        ctx.fileGen.init(file: data, builder: builder)
        ctx.fileGen.generate()
        java.io.File created = new java.io.File(testDir, "Test.java")

        a(testDir.exists()).shouldBeTrue()
        a(created.exists()).shouldBeTrue()
        the(created.text).shouldEqual(expected)
    }

    @Test
    void "a file in a package"() {
        Namespace ns = Namespace.builder()
                .nsKey("ns1")
                .name("ns1")
                .create()

        ns.addFile(data)

        ctx.fileGen.init(file: data, builder: builder)
        ctx.fileGen.generate()
        java.io.File created = new java.io.File(testDir, "Test.java")

        String expected = """\
        /**
         * MIT License
         *
         * MSUSEL Design Pattern Generator
         * Copyright (c) 2015-2020 Montana State University, Gianforte School of Computing,
         * Software Engineering Laboratory and Idaho State University, Informatics and
         * Computer Science, eXtended Reality and Empirical Software Engineering Laboratory
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
                
        package ns1;
        import java.util.*;
        
        """.stripIndent()

        a(testDir.exists()).shouldBeTrue()
        a(created.exists()).shouldBeTrue()
        Assert.assertEquals(created.text, expected)
    }

    @Test
    void "a file in a nested package"() {
        Namespace ns1 = Namespace.builder()
                .nsKey("ns1")
                .name("ns1")
                .create()
        Namespace ns2 = Namespace.builder()
                .nsKey("ns1.ns2")
                .name("ns1.ns2")
                .create()
        ns1.addNamespace(ns2)
        ns2.addFile(data)
        proj.addNamespace(ns1)
        proj.addNamespace(ns2)

        ctx.fileGen.init(file: data, builder: builder)
        ctx.fileGen.generate()
        java.io.File created = new java.io.File(testDir, "Test.java")

        String expected = """\
        /**
         * MIT License
         *
         * MSUSEL Design Pattern Generator
         * Copyright (c) 2015-2020 Montana State University, Gianforte School of Computing,
         * Software Engineering Laboratory and Idaho State University, Informatics and
         * Computer Science, eXtended Reality and Empirical Software Engineering Laboratory
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
                
        package ns1.ns2;
        import java.util.*;
        
        """.stripIndent()

        a(testDir.exists()).shouldBeTrue()
        a(created.exists()).shouldBeTrue()
        the(created.text).shouldEqual(expected)
    }

    @Test
    void "a file with a single type"() {
        Namespace ns1 = Namespace.builder()
                .nsKey("ns1")
                .name("ns1")
                .create()
        Namespace ns2 = Namespace.builder()
                .nsKey("ns1.ns2")
                .name("ns1.ns2")
                .create()
        ns1.addNamespace(ns2)
        ns2.addFile(data)
        proj.addNamespace(ns1)
        proj.addNamespace(ns2)

        Class clazz = Class.builder()
                .name("Test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.addType(clazz)
        ns2.addType(clazz)

        def expected = """\
        /**
         * MIT License
         *
         * MSUSEL Design Pattern Generator
         * Copyright (c) 2015-2020 Montana State University, Gianforte School of Computing,
         * Software Engineering Laboratory and Idaho State University, Informatics and
         * Computer Science, eXtended Reality and Empirical Software Engineering Laboratory
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
                
        package ns1.ns2;
        import java.util.*;
        
        /**
         * Generated Class
         *
         * @author Isaac Griffith
         * @version 1.0
         */
        public class Test {
        
        }
        
        """.stripIndent()

        ctx.fileGen.init(file: data, builder: builder)
        ctx.fileGen.generate()
        java.io.File created = new java.io.File(testDir, "Test.java")

        a(testDir.exists()).shouldBeTrue()
        a(created.exists()).shouldBeTrue()
        the(created.text).shouldEqual(expected)
    }

    @Test
    void "a file with multiple types"() {
        Namespace ns1 = Namespace.builder()
                .nsKey("ns1")
                .name("ns1")
                .create()
        Namespace ns2 = Namespace.builder()
                .nsKey("ns1.ns2")
                .name("ns1.ns2")
                .create()
        ns1.addNamespace(ns2)
        ns2.addFile(data)
        proj.addNamespace(ns1)
        proj.addNamespace(ns2)

        Class clazz = Class.builder()
                .name("Test")
                .accessibility(Accessibility.PUBLIC)
                .create()
        data.addType(clazz)
        ns1.addType(clazz)
        Class clazz2 = Class.builder()
                .name("Test2")
                .accessibility(Accessibility.DEFAULT)
                .create()
        data.addType(clazz2)
        ns1.addType(clazz2)

        def expected = """\
        /**
         * MIT License
         *
         * MSUSEL Design Pattern Generator
         * Copyright (c) 2015-2020 Montana State University, Gianforte School of Computing,
         * Software Engineering Laboratory and Idaho State University, Informatics and
         * Computer Science, eXtended Reality and Empirical Software Engineering Laboratory
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
                
        package ns1.ns2;
        import java.util.*;
        
        /**
         * Generated Class
         *
         * @author Isaac Griffith
         * @version 1.0
         */
        public class Test {
        
        }
        
        /**
         * Generated Class
         *
         * @author Isaac Griffith
         * @version 1.0
         */
        class Test2 {
        
        }
        
        """.stripIndent()

        ctx.fileGen.init(file: data, builder: builder)
        ctx.fileGen.generate()
        java.io.File created = new java.io.File(testDir, "Test.java")

        a(testDir.exists()).shouldBeTrue()
        a(created.exists()).shouldBeTrue()
        the(created.text).shouldEqual(expected)
    }
}
