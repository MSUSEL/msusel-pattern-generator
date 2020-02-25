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

import edu.montana.gsoc.msusel.pattern.gen.current.FileGenerator
import edu.montana.gsoc.msusel.pattern.gen.current.LanguageProvider
import edu.montana.gsoc.msusel.pattern.gen.current.LicenseGenerator
import edu.montana.gsoc.msusel.pattern.gen.current.ModuleGenerator
import edu.montana.gsoc.msusel.pattern.gen.current.NamespaceGenerator
import edu.montana.gsoc.msusel.pattern.gen.current.ProjectGenerator
import edu.montana.gsoc.msusel.pattern.gen.current.ReadmeGenerator
import edu.montana.gsoc.msusel.pattern.gen.current.SystemGenerator
import spock.lang.Specification

class JavaLanguageProviderTest extends Specification {

    LanguageProvider fixture

    def setup() {
        fixture = new JavaLanguageProvider()
    }

    def "test createDirectoryGenerator"() {
        given:
        fixture

        when:
        def dirGen = fixture.createDirectoryGenerator()

        then:
        dirGen instanceof JavaDirectoryGenerator
    }

    def "test createBuildFileGenerator"() {
        given:
        fixture

        when:
        def buildGen = fixture.createBuildFileGenerator()

        then:
        buildGen instanceof JavaBuildFileGenerator
    }

    def "test createGitIgnoreGenerator"() {
        given:
        fixture

        when:
        def gigGen = fixture.createGitIgnoreGenerator()

        then:
        gigGen instanceof JavaGitIgnoreGenerator
    }

    def "test createCodeGenerator"() {
        given:
        fixture

        when:
        def codeGen = fixture.createCodeGenerator()

        then:
        codeGen instanceof JavaCodeGenerator
    }

    def "test createTypeGenerator"() {
        given:
        fixture

        when:
        def typeGen = fixture.createTypeGenerator()

        then:
        typeGen instanceof JavaTypeGenerator
    }

    def "test createFieldGenerator"() {
        given:
        fixture

        when:
        def fieldGen = fixture.createFieldGenerator()

        then:
        fieldGen instanceof JavaFieldGenerator
    }

    def "test createMethodGenerator"() {
        given:
        fixture

        when:
        def methodGen = fixture.createMethodGenerator()

        then:
        methodGen instanceof JavaMethodGenerator
    }

    def "test createLicenseGenerator"() {
        given:
        fixture

        when:
        def licGen = fixture.createLicenseGenerator()

        then:
        licGen instanceof LicenseGenerator
    }

    def "test createReadmeGenerator"() {
        given:
        fixture


        when:
        def readGen = fixture.createReadmeGenerator()

        then:
        readGen instanceof ReadmeGenerator
    }

    def "test createFileGenerator"() {
        given:
        fixture

        when:
        def fileGen = fixture.createFileGenerator()

        then:
        fileGen instanceof FileGenerator
    }

    def "test createModuleGenerator"() {
        given:
        fixture

        when:
        def modGen = fixture.createModuleGenerator()

        then:
        modGen instanceof ModuleGenerator
    }

    def "test createProjectGenerator"() {
        given:
        fixture

        when:
        def projGen = fixture.createProjectGenerator()

        then:
        projGen instanceof ProjectGenerator
    }

    def "test createNamespaceGenerator"() {
        given:
        fixture

        when:
        def nsGen = fixture.createNamespaceGenerator()

        then:
        nsGen instanceof NamespaceGenerator
    }

    def "test createSystemGenerator"() {
        given:
        fixture

        when:
        def sysGen = fixture.createSystemGenerator()

        then:
        sysGen instanceof SystemGenerator
    }
}
