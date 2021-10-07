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

class AppTest extends Specification {

    ByteArrayOutputStream consoleText
    PrintStream oldOut
    PrintStream console
    File config

    def setup() {
        oldOut = System.out
        consoleText = new ByteArrayOutputStream()
        console = new PrintStream(consoleText)
        System.setOut(console)

        // generate config file
        config = new File("PatternGenConfig")
        if (!config.exists()) {
            config << """\
output = "."
language = "java"
patterns = ["singleton"]
numInstances = 5
maxBreadth = 5
maxDepth = 3
version = "1.0.0"
arities = [1, 3, 5]
srcPath = "src/main/java"
srcExt = ".java"

license {
    name = "MIT"
    year = 2020
    holder = 'Isaac D Griffith'
    project = 'Design Pattern Generator'
    url = 'isu-ese.github.io/patterngen'
}

db {
    driver = "org.sqlite.JDBC"
    url = "jdbc:sqlite:data/testing.db"
    user = "dev1"
    pass = "passwd"
}

build {
    project = 'test'
    artifact = 'test'
    description = 'a test project'
}
"""
        }
    }

    def cleanup() {
        System.setOut(oldOut)
    }

//    def "test main -h"() {
//        given:
//        String[] args = ['-h']
//        String expected = """\
//Usage: patterngen [options] <base>
//
//Options:
// -c, --config-file=<file>   File to use for configuration
// -d, --data-elements-only   Only construct data elements, no code
// -g, --generate-only        Only generate patterns from the database
// -h, --help                 Print this help text and exit.
// -l, --language             Sets the language used by the generator
// -r, --reset-db             Resets the database to an empty configuration
// -v, --version              Print the version information
// -x, --reset-only           Only resets the database
//
//Copyright (c) 2018-2020 Isaac Griffith and Montana State University
//"""
//
//        MySecurityManager secManager = new MySecurityManager()
//        System.setSecurityManager(secManager)
//
//        when:
//        App.main(args)
//
//        then:
//        thrown SecurityException
//        consoleText.toString() == expected
//    }

//    def "test main -v"() {
//        given:
//        String[] args = ['-v']
//        String expected = """\
//patterngen version 1.3.0
//"""
//        MySecurityManager secManager = new MySecurityManager()
//        System.setSecurityManager(secManager)
//
//        when:
//        App.main(args)
//
//        then:
//        thrown SecurityException
//        consoleText.toString() == expected
//    }

    def "test main -g"() {}

    def "test main -d"() {}

//    def "test main -x"() {
//        given:
//        GeneratorContext ctx = GeneratorContext.instance
//        ctx.db = [
//            "driver" : "org.sqlite.JDBC",
//            "url"    : "jdbc:sqlite:data/testing.db",
//            "user"   : "dev1",
//            "pass"   : "passwd"
//        ]
//
//        DBManager manager = new DBManager(context: ctx)
//        manager.open()
//        Class.builder().name("Test").compKey("Test").create()
//        manager.close()
//        String[] args = ['-x', 'systems/']
//
//        MySecurityManager secManager = new MySecurityManager()
//        System.setSecurityManager(secManager)
//
//        when:
//        App.main(args)
//
//        then:
//        manager.open()
//        Class.findAll().isEmpty()
//        manager.close() == null
//    }
}
