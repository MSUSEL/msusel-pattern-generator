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
package edu.montana.gsoc.msusel.pattern.cue

import edu.montana.gsoc.msusel.pattern.gen.event.EventManager
import edu.montana.gsoc.msusel.pattern.gen.event.GeneratorEvent
import edu.montana.gsoc.msusel.pattern.gen.event.GeneratorEventListener
import groovy.transform.ToString

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@ToString(includes = ["name"], includePackage = false, includeNames = true)
class CueRole implements GeneratorEventListener {

    String name
    GeneratorEvent.EventType event
    String content
    String definition
    boolean disregard = false

    /**
     * 
     */
    CueRole() {
        // TODO Auto-generated constructor stub
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    def handleEvent(evt) {
        if (evt.type == event && name == evt.role) {
            println "Event"
            println "\tkey: ${evt.key}"
            println "\tbuilder: ${evt.builder}"
            println "\trole: ${evt.role}"
            println "\ttype: ${evt.type}"
            println "\townerKey: ${evt.ownerKey}"
            if (!disregard) {
                if (content) {
                    println "content: ${content}"
                    def temp = content
                    if (temp.contains("[InstName]") && evt.ownerKey)
                        temp = temp.replaceAll(/\[InstName\]/, evt.ownerKey.split("\\.").last())
                    if (temp.contains("[TypeName]") && evt.ownerKey)
                        temp = temp.replaceAll(/\[TypeName\]/, evt.ownerKey.split("\\.").last())
                    evt.builder << temp
                    println "temp: ${temp}"
                    evt.builder << temp
                } else if (definition) {
                    println "definition: ${definition}"
                    def temp = definition
                    if (temp.contains("[InstName]") && evt.ownerKey)
                        temp = temp.replaceAll(/\[InstName\]/, evt.ownerKey.split("\\.").last())
                    if (temp.contains("[TypeName]") && evt.ownerKey)
                        temp = temp.replaceAll(/\[TypeName\]/, evt.ownerKey.split("\\.").last())
                    evt.builder << temp
                    println "temp: ${temp}"
                }
            evt.builder << ""
            }
            else {
                EventManager.instance.fireDisregard(evt.key)
            }
        }
    }
    
    def register() {
        EventManager.instance.registerListener(this, event)
    }
}
