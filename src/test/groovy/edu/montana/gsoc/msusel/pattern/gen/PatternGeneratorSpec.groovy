/**
 * MIT License
 *
 * MSUSEL Design Pattern Generator
 * Copyright (c) 2017-2018 Montana State University, Gianforte School of Computing
 * Software Engineering Laboratory
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
/**
 * 
 */
package edu.montana.gsoc.msusel.pattern.gen

import edu.montana.gsoc.msusel.pattern.cue.CueScriptLoader
import edu.montana.gsoc.msusel.pattern.gen.java.JavaPatternGenerator
import edu.montana.gsoc.msusel.pattern.gen.java.JavaSourceBuilder
import edu.montana.gsoc.msusel.rbml.RBMLScriptLoader
import spock.lang.Specification

/**
 * @author Isaac Griffith
 *
 */
class PatternGeneratorSpec extends Specification {

    def testReadClassFileNames() {
        given: "A pattern generator instance"
        AbstractPatternGenerator pg = null
        
        when: "we call readNamespaceNames()"
        pg.readClassFileNames()
        
        then: "classNames is not empty or null"
        pg.classNames != null
        !pg.classNames.isEmpty()
    }
    
    def testReadMethodNames() {
        given: "A pattern generator instance"
        AbstractPatternGenerator pg = null
        
        when: "we call readMethodNames()"
        pg.readMethodNames()
        
        then: "methodNames is not empty or null"
        pg.methodNames != null
        !pg.methodNames.isEmpty()
    }
    
    def testReadFieldNames() {
        given: "A pattern generator instance"
        AbstractPatternGenerator pg = null
        
        when: "we call readFieldNames()"
        pg.readFieldNames()
        
        then: "fieldNames is not empty or null"
        pg.fieldNames != null
        !pg.fieldNames.isEmpty()
    }
    
    def testReadNamespaceNames() {
        given: "A pattern generator instance"
        AbstractPatternGenerator pg = null
        
        when: "we call readNamespaceNames()"
        pg.readNamespaceNames()
        
        then: "nsNames is not empty or null"
        pg.nsNames != null
        !pg.nsNames.isEmpty()
    }

    def test() {
        AbstractPatternGenerator pg = new JavaPatternGenerator()

        pg.setExtension 'java'
        pg.setBuilder(new JavaSourceBuilder(pg: pg))
        new RBMLScriptLoader().load('testdata/test.rbml')
        pg.getBuilder().setOutput('testdata/output/')

        new CueScriptLoader().load('testdata/test-java.pgcl')

        pg.setNumInstances 3
        pg.setMaxBreadth 3
        pg.setMaxDepth 3
        pg.setLang "Java"

        pg.generate()
    }
}
