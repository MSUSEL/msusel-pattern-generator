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

import edu.isu.isuese.datamodel.Pattern
import spock.lang.Specification

class DBManagerTest extends Specification {

    DBManager fixture
    GeneratorContext context

    void setup() {
        context = GeneratorContext.getInstance()
        context.db = [
                'driver' : 'org.sqlite.JDBC',
                'url'    : 'jdbc:sqlite:data/testing.db',
                'user'   : 'dev2',
                'pass'   : 'passwd',
                'file'   : 'data/testing.db'
        ]
        fixture = new DBManager(context: context)
    }

    void cleanup() {
//        File f = new File(context.db.file)
//        f.delete()
    }

    def "Open"() {
        given:
        fixture
        fixture.createDatabase()

        when:
        fixture.open()

        then:
        !Pattern.findAll().isEmpty()
        fixture.close() == null
    }

    def "Close"() {
        given:
        fixture
        fixture.createDatabase()

        when:
        fixture.open()

        then:
        fixture.close() == null
    }

    def "CreateDatabase"() {
        given:
        fixture

        when:
        fixture.createDatabase()
        File f = new File(context.db.file)

        then:
        f.exists()
    }

    def "ResetDatabase"() {
    }
}
