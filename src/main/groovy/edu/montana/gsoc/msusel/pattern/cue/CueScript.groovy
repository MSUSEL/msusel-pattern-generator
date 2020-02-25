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
package edu.montana.gsoc.msusel.pattern.cue

import edu.montana.gsoc.msusel.pattern.cue.factory.CueBuilder
import edu.montana.gsoc.msusel.pattern.gen.event.EventType

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
abstract class CueScript extends Script {

    static final EventType UnitCreationStarted = EventType.UnitCreationStarted
    static final EventType UnitCreationComplete = EventType.UnitCreationComplete
    static final EventType TypesStarted = EventType.TypesStarted
    static final EventType TypesComplete = EventType.TypesComplete
    static final EventType TypeCreationStarted = EventType.TypeCreationStarted
    static final EventType TypeCreationComplete = EventType.TypeCreationComplete
    static final EventType TypeHeader = EventType.TypeHeader
    static final EventType TypeBody = EventType.TypeBody
    static final EventType FieldsStarted = EventType.FieldsStarted
    static final EventType FieldsComplete = EventType.FieldsComplete
    static final EventType FieldCreationStarted = EventType.FieldCreationStarted
    static final EventType FieldCreationComplete = EventType.FieldCreationComplete
    static final EventType FieldInitialization = EventType.FieldInitialization
    static final EventType FieldDefinition = EventType.FieldDefinition
    static final EventType MethodsStarted = EventType.MethodsStarted
    static final EventType MethodsComplete = EventType.MethodsComplete
    static final EventType MethodCreationStarted = EventType.MethodCreationStarted
    static final EventType MethodCreationComplete = EventType.MethodCreationComplete
    static final EventType MethodHeader = EventType.MethodHeader
    static final EventType MethodBody = EventType.MethodBody
    static final EventType LiteralsStarted = EventType.LiteralsStarted
    static final EventType LiteralsComplete = EventType.LiteralsComplete
    static final EventType LiteralCreationStarted = EventType.LiteralCreationStarted
    static final EventType LiteralCreationComplete = EventType.LiteralCreationComplete
    static final EventType Disregard = EventType.Disregard
    
    CueBuilder builder = new CueBuilder()
    
    def pattern(String name, Closure c) {
        builder.pattern(name, c)
    }
}
