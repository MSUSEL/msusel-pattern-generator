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
package edu.montana.gsoc.msusel.pattern.gen

import spock.lang.Specification

class ConfigLoaderTest extends Specification {

    ConfigLoader fixture

    def setup() {
        fixture = new ConfigLoader()
    }

    def cleanup() {
    }

    def "test buildContext"() {
        given:
        String text = '''\
output = "."
patterns = ["Strategy", "Singleton"]
numInstances = 5
maxBreadth = 5
maxDepth = 3
version = "1.0.0"
license {
    name = "MIT"
    year = 2020
    holder = 'Isaac D Griffith'
    project = 'Design Pattern Generator'
    url = 'isu-ese.github.io/patterngen'
}
db {
    driver = "org.sqlite.JDBC"
    url = "jdbc:sqlite:data/test.db"
    user = "dev1"
    pass = "passwd"
}
build {
    project = ''
    artifact = ''
    description = ''
}
arities = [1, 3, 5]
srcPath = "src/main/java"
'''
        File base = new File("data/")
        def config = new ConfigSlurper().parse(text)
        GeneratorContext context = GeneratorContext.getInstance()

        when:
        fixture.buildContext(config, base)

        then:
        context.base == base
        context.output == '.'
        context.loader != null
        context.patterns.contains('Strategy')
        context.numInstances == 5
        context.maxBreadth == 5
        context.maxDepth == 3
        context.version == "1.0.0"
        context.license.name == "MIT"
        context.license.year == 2020
        context.license.holder == "Isaac D Griffith"
        context.license.project == 'Design Pattern Generator'
        context.license.url == 'isu-ese.github.io/patterngen'
        context.db.driver == "org.sqlite.JDBC"
        context.db.url == "jdbc:sqlite:data/test.db"
        context.db.user == "dev1"
        context.db.pass == "passwd"
        context.build.project == ''
        context.build.artifact == ''
        context.build.description == ''
        context.arities == [1, 3, 5]
        context.srcPath == "src/main/java"
    }

    def "test buildContext null ok"() {
        given:
        def config = null
        def base = new File("test")

        when:
        fixture.buildContext(config, base)

        then:
        thrown IllegalArgumentException
    }

    def "test buildContext ok null"() {
        given:
        def text = '''\
output = "."
language = "java"
'''
        File base = null
        def config = new ConfigSlurper().parse(text)

        when:
        fixture.buildContext(config, base)

        then:
        thrown IllegalArgumentException
    }

    def "test loadBuiltInPlugins"() {

    }
}