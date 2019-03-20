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
import edu.montana.gsoc.msusel.codetree.node.structural.Project
import edu.montana.gsoc.msusel.pattern.gen.java.*
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

class JavaFileGeneratorSpec extends Specification {

    def testCorrectMavenFileGeneration() {
        given:
        CodeTree tree = new DefaultCodeTree()
        tree.setProject(Project.builder().key("Project").create())
        JavaFileGenerator gen = new JavaMavenFileGenerator(base: "testdata", tree: tree, patternName: "Pattern")
        JavaMavenDirStructGenerator dirGen = new JavaMavenDirStructGenerator(base: "testdata")
        deleteDir(new File("testdata"))
        dirGen.generateDirStructure()

        when:
        gen.generateFiles()

        then:
        Files.exists(Paths.get("testdata/pom.xml"))
        Files.exists(Paths.get("testdata/README.md"))
        Files.exists(Paths.get("testdata/LICENSE"))
        Files.size(Paths.get("testdata/pom.xml")) > 0l
        Files.size(Paths.get("testdata/README.md")) > 0l
        Files.size(Paths.get("testdata/LICENSE")) > 0l
    }

    def testCorrectGradleFileGeneration() {
        given:
        CodeTree tree = new DefaultCodeTree()
        tree.setProject(Project.builder().key("Project").create())
        JavaFileGenerator gen = new JavaGradleFileGenerator(base: "testdata", tree: tree, patternName: "Pattern")
        JavaGradleDirStructGenerator dirGen = new JavaGradleDirStructGenerator(base: "testdata")
        deleteDir(new File("testdata"))
        dirGen.generateDirStructure()

        when:
        gen.generateFiles()

        then:
        Files.exists(Paths.get("testdata/build.gradle"))
        Files.exists(Paths.get("testdata/settings.gradle"))
        Files.exists(Paths.get("testdata/README.md"))
        Files.exists(Paths.get("testdata/LICENSE"))
        Files.size(Paths.get("testdata/build.gradle")) > 0l
        Files.size(Paths.get("testdata/settings.gradle")) > 0l
        Files.size(Paths.get("testdata/README.md")) > 0l
        Files.size(Paths.get("testdata/LICENSE")) > 0l
    }

    static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list()
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir.delete()
    }
}
