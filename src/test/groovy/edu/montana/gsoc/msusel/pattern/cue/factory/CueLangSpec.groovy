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
package edu.montana.gsoc.msusel.pattern.cue.factory

import spock.lang.Specification
import edu.montana.gsoc.msusel.pattern.cue.Cue
import edu.montana.gsoc.msusel.pattern.cue.CueManager
import edu.montana.gsoc.msusel.pattern.cue.CuePattern
import edu.montana.gsoc.msusel.pattern.cue.CueRole

/**
 * @author Isaac Griffith
 *
 */
class CueLangSpec extends Specification {

    def testCuePattern() {
        CueManager.reset()
        
        given: "A CueBuilder"
        CueBuilder builder = new CueBuilder()
        
        when: "we have a pattern"
        def inst = builder.pattern "Test"
        
        then: "Instance is not null"
        inst != null
        
        then: "Instance is of type CuePattern"
        inst instanceof CuePattern
        
        then: "Instance name is correct"
        inst.getName() == "Test"
        
        then: "Instance cues is empty"
        inst.getCues() == [:]
    }

    def testCuePatternWithContents() {
        CueManager.reset()
        
        given: "A CueBuilder"
        CueBuilder builder = new CueBuilder()
        
        when: "we have a pattern"
        def inst = builder.pattern "Test", {
            cue "TestCue"
        }
        
        then: "Instance is not null"
        inst != null
        
        then: "Instance is of type CuePattern"
        inst instanceof CuePattern
        
        then: "Instance name is correct"
        inst.getName() == "Test"
        
        then: "Instance cues is empty"
        inst.getCues() != [:]
        
        then: "Instance has one cue"
        inst.getCues().size() == 1
    }

    def testCue() {
        CueManager.reset()
        
        given: "A CueBuilder"
        CueBuilder builder = new CueBuilder()
        
        when: "we have a cue"
        def inst = builder.cue "Test"
        
        then: "Instance is not null"
        inst != null
        
        then: "Instance is of type Cue"
        inst instanceof Cue
        
        then: "Instance name is correct"
        inst.getName() == "Test"
        
        then: "Instance roles is empty"
        inst.getRoles() == [:]
    }

    def testCueWithContents() {
        CueManager.reset()
        
        given: "A CueBuilder"
        CueBuilder builder = new CueBuilder()
        
        when: "we have a cue"
        def inst = builder.cue "Test", {
            role "role1"
        }
        
        then: "Instance is not null"
        inst != null
        
        then: "Instance is of type Cue"
        inst instanceof Cue
        
        then: "Instance name is correct"
        inst.getName() == "Test"
        
        then: "Instance roles is empty"
        inst.getRoles() != [:]
        
        then: "Instance has one role"
    }

    def testCueRole() {
        CueManager.reset()
        
        given: "A CueBuilder"
        CueBuilder builder = new CueBuilder()
        
        when: "we have a role"
        def inst = builder.role "Test"
        
        then: "Instance is not null"
        inst != null
        
        then: "Instance is of type CueRole"
        inst instanceof CueRole
        
        then: "Instance name is correct"
        inst.getName() == "Test"
    }
    
    def testComplete() {
        CueManager.reset()
        
        given: "A CueBuilder"
        CueBuilder builder = new CueBuilder()
        
        when: "We have the following pattern with cues"
        def inst = builder.pattern "Singleton", {
            cue  "enumSingleton", {
                role "Singleton", definition: '''
    <enum><specifier>public</specifier> enum <name>[InstName]</name> <block>{
        <decl><name>INSTANCE</name></decl>;
    }</block></enum>'''
            
                role "Singleton::GetInstance()", disregard: true
            
                role "Singleton::Instance", disregard: true
            }
            
            cue "eagerInit", {
                role "Singleton", event: "FieldsComplete", content: '''
    <constructor><specifier>private</specifier> <name>[InstName]</name><parameter_list>()</parameter_list> <block>{}</block></constructor>'''
            
                role "Singleton::Instance", definition: '''
    <decl_stmt><decl><specifier>private</specifier> <type><specifier>static</specifier> <specifier>final</specifier> <name>[TypeName]</name></type> <name>instance</name> <init>= <expr><operator>new</operator> <call><name>[TypeName]</name><argument_list>()</argument_list></call></expr></init></decl>;</decl_stmt>'''
            
                role "Singleton::GetInstance()", content: "<return>return <expr><name>instance</name>;</expr></return>"
            }
        }
        
        then: "Instance name is correct"
        inst.getName() == "Singleton"
        
        def first = inst.getCues()["enumSingleton"]
        def second = inst.getCues()["eagerInit"]
        
        then: "First Cue name is correct"
        first.getName() == "enumSingleton"
        
        def firstRole1 = first.getRoles()["Singleton"]
        def firstRole2 = first.getRoles()["Singleton::Instance"]
        
        then: "First Cue Roles properties are correct"
        firstRole1.content == null
        firstRole1.definition != null
        firstRole1.event == null
        firstRole1.name == "Singleton"
        !firstRole1.disregard
        
        firstRole2.content == null
        firstRole2.definition == null
        firstRole2.event == null
        firstRole2.name == "Singleton::Instance"
        firstRole2.disregard
        
        then: "First Cue name is correct"
        second.getName() == "eagerInit"
        
        def secondRole1 = second.getRoles()["Singleton"]
        def secondRole2 = second.getRoles()["Singleton::Instance"]
        
        then: "First Cue Roles properties are correct"
        secondRole1.content != null
        secondRole1.definition == null
        secondRole1.event.toString() == "FieldsComplete"
        secondRole1.name == "Singleton"
        !secondRole1.disregard
        
        secondRole2.content == null
        secondRole2.definition != null
        secondRole2.event == null
        secondRole2.name == "Singleton::Instance"
        !secondRole2.disregard
    }
}
