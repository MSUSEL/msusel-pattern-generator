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
package edu.montana.gsoc.msusel.pattern.gen.generators


import spock.lang.Specification

class LicenseGeneratorTest extends Specification {

    LicenseGenerator fixture
    FileTreeBuilder builder
    File testDir = new File('testdir')

    void setup() {
        fixture = new LicenseGenerator()
        testDir.mkdirs()
        builder = new FileTreeBuilder(testDir)
    }

    void cleanup() {
        if (testDir.exists())
            testDir.deleteDir()
    }

    def "Generate"() {
        given:
        String name = 'MIT'
        String year = '2020'
        String holderName = 'Isaac Griffith'
        String project = 'TestGen'
        String projectUrl = 'https://testgen.com'

        when:
        fixture.generate(builder, name, year, holderName, project, projectUrl)
        File f = new File(testDir, 'LICENSE')

        then:
        f.exists()
        f.text == """\
MIT License

Copyright (c) $year $holderName

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
"""
    }

    def "test generate when builder is null"() {
        given:
        fixture
        builder = null

        when:
        fixture.generate(builder, "Test", "2020", "Isaac G.", "Test", "https://test.com")

        then:
        IllegalArgumentException ex = thrown()
        ex.message == "File Builder cannot be null"
    }

    def "test generate when parameters are null or empty"(name, year, holderName, project, projecturl, message) {
        given:
        fixture

        when:
        fixture.generate(builder, name, year, holderName, project, projecturl)

        then:
        IllegalArgumentException ex = thrown()
        ex.message == message

        where:
        name   | year   | holderName | project | projecturl         | message
        null   | "2020" | "Isaac G." | "Test"  | "https://test.com" | "Name cannot be null or empty"
        ""     | "2020" | "Isaac G." | "Test"  | "https://test.com" | "Name cannot be null or empty"
        "Test" | null   | "Isaac G." | "Test"  | "https://test.com" | "Year cannot be null or empty"
        "Test" | ""     | "Isaac G." | "Test"  | "https://test.com" | "Year cannot be null or empty"
        "Test" | "2020" | null       | "Test"  | "https://test.com" | "Holder Name cannot be null or empty"
        "Test" | "2020" | ""         | "Test"  | "https://test.com" | "Holder Name cannot be null or empty"
        "Test" | "2020" | "Isaac G." | null    | "https://test.com" | "Project Name cannot be null or empty"
        "Test" | "2020" | "Isaac G." | ""      | "https://test.com" | "Project Name cannot be null or empty"
        "Test" | "2020" | "Isaac G." | "Test"  | null               | "Project URL cannot be null or empty"
        "Test" | "2020" | "Isaac G." | "Test"  | ""                 | "Project URL cannot be null or empty"
    }

    def "GetLicenses"() {
    }
}
