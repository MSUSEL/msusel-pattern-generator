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

import edu.montana.gsoc.msusel.pattern.gen.java.JavaGradleDirStructGenerator
import edu.montana.gsoc.msusel.pattern.gen.java.JavaMavenDirStructGenerator
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

class JavaDirStructGeneratorSpec extends Specification {

    def testCorrectMavenDirStructure() {
        given: "a new JavaMavenDirStructGenerator"
        File f = new File("testdata")
        f.deleteDir()
        JavaMavenDirStructGenerator gen = new JavaMavenDirStructGenerator(base: "testdata")

        when: "we call generateDirStructure()"
        gen.generateDirStructure()

        then:
        Files.exists(Paths.get("testdata/src")) && Files.isDirectory(Paths.get("testdata/src"))
        Files.exists(Paths.get("testdata/src/main")) && Files.isDirectory(Paths.get("testdata/src/main"))
        Files.exists(Paths.get("testdata/src/main/java")) && Files.isDirectory(Paths.get("testdata/src/main/java"))
        Files.exists(Paths.get("testdata/src/main/resources")) && Files.isDirectory(Paths.get("testdata/src/main/resources"))
        Files.exists(Paths.get("testdata/src/test")) && Files.isDirectory(Paths.get("testdata/src/test"))
        Files.exists(Paths.get("testdata/src/test/java")) && Files.isDirectory(Paths.get("testdata/src/test/java"))
        Files.exists(Paths.get("testdata/src/test/resources")) && Files.isDirectory(Paths.get("testdata/src/test/resources"))
        Files.exists(Paths.get("testdata/target")) && Files.isDirectory(Paths.get("testdata/target"))
    }

    def testCorrectGradleDirStructure() {
        given: "a new JavaGradleDirStructGenerator"
        File f = new File("testdata")
        f.deleteDir()
        JavaGradleDirStructGenerator gen = new JavaGradleDirStructGenerator(base: "testdata")

        when: "we call generateDirStructure()"
        gen.generateDirStructure()

        then:
        Files.exists(Paths.get("testdata/src")) && Files.isDirectory(Paths.get("testdata/src"))
        Files.exists(Paths.get("testdata/src/main")) && Files.isDirectory(Paths.get("testdata/src/main"))
        Files.exists(Paths.get("testdata/src/main/java")) && Files.isDirectory(Paths.get("testdata/src/main/java"))
        Files.exists(Paths.get("testdata/src/main/resources")) && Files.isDirectory(Paths.get("testdata/src/main/resources"))
        Files.exists(Paths.get("testdata/src/test")) && Files.isDirectory(Paths.get("testdata/src/test"))
        Files.exists(Paths.get("testdata/src/test/java")) && Files.isDirectory(Paths.get("testdata/src/test/java"))
        Files.exists(Paths.get("testdata/src/test/resources")) && Files.isDirectory(Paths.get("testdata/src/test/resources"))
        Files.exists(Paths.get("testdata/build")) && Files.isDirectory(Paths.get("testdata/build"))
    }
}
