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
import edu.montana.gsoc.msusel.pattern.gen.event.GeneratorEvent.EventType
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
abstract class CueScript extends Script {

    public static final EventType UnitCreationStarted = EventType.UnitCreationStarted 
    public static final EventType UnitCreationComplete = EventType.UnitCreationComplete
    public static final EventType TypesStarted = EventType.TypesStarted
    public static final EventType TypesComplete = EventType.TypesComplete
    public static final EventType TypeCreationStarted = EventType.TypeCreationStarted
    public static final EventType TypeCreationComplete = EventType.TypeCreationComplete
    public static final EventType TypeHeader = EventType.TypeHeader
    public static final EventType TypeBody = EventType.TypeBody
    public static final EventType FieldsStarted = EventType.FieldsStarted
    public static final EventType FieldsComplete = EventType.FieldsComplete
    public static final EventType FieldCreationStarted = EventType.FieldCreationStarted
    public static final EventType FieldCreationComplete = EventType.FieldCreationComplete
    public static final EventType FieldInitialization = EventType.FieldInitialization
    public static final EventType FieldDefinition = EventType.FieldDefinition
    public static final EventType MethodsStarted = EventType.MethodsStarted
    public static final EventType MethodsComplete = EventType.MethodsComplete
    public static final EventType MethodCreationStarted = EventType.MethodCreationStarted
    public static final EventType MethodCreationComplete = EventType.MethodCreationComplete
    public static final EventType MethodHeader = EventType.MethodHeader
    public static final EventType MethodBody = EventType.MethodBody
    public static final EventType LiteralsStarted = EventType.LiteralsStarted
    public static final EventType LiteralsComplete = EventType.LiteralsComplete
    public static final EventType LiteralCreationStarted = EventType.LiteralCreationStarted
    public static final EventType LiteralCreationComplete = EventType.LiteralCreationComplete
    public static final EventType Disregard = EventType.Disregard
    
    CueBuilder builder = new CueBuilder()
    
    def pattern(String name, Closure c) {
        builder.pattern(name, c)
    }
}
