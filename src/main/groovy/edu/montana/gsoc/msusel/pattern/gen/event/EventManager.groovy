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
package edu.montana.gsoc.msusel.pattern.gen.event

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Singleton
class EventManager {

    def registered = []
    def unitCreationListeners = []
    def typesListeners = []
    def typeCreationListeners = []
    def typeHeaderListeners = []
    def typeBodyListeners = []
    def fieldsListeners = []
    def fieldCreationListeners = []
    def fieldInitListeners = []
    def fieldDefinitionListeners = []
    def methodsListeners = []
    def methodCreationListeners = []
    def methodHeaderListeners = []
    def methodBodyListeners = []
    def literalsListeners = []
    def literalCreationListeners = []
    def disregardListeners = []
    
    void registerListener(listener, type) {
        if (!listener || !type || registered.contains(listener))
            return
        println "registering listener: ${listener} for type: ${type}"
        registered << listener
        switch (type) {
            case GeneratorEvent.EventType.UnitCreationStarted: unitCreationListeners << listener
            case GeneratorEvent.EventType.UnitCreationComplete: unitCreationListeners << listener
            case GeneratorEvent.EventType.TypesStarted: typesListeners << listener
            case GeneratorEvent.EventType.TypesComplete: typesListeners << listener
            case GeneratorEvent.EventType.TypeCreationStarted: typeCreationListeners << listener
            case GeneratorEvent.EventType.TypeCreationComplete: typeCreationListeners << listener
            case GeneratorEvent.EventType.TypeHeader: typeHeaderListeners << listener
            case GeneratorEvent.EventType.TypeBody: typeBodyListeners << listener
            case GeneratorEvent.EventType.FieldsStarted: fieldsListeners << listener
            case GeneratorEvent.EventType.FieldsComplete: fieldsListeners << listener
            case GeneratorEvent.EventType.FieldCreationStarted: fieldCreationListeners << listener
            case GeneratorEvent.EventType.FieldCreationComplete: fieldCreationListeners << listener
            case GeneratorEvent.EventType.FieldInitialization: fieldInitListeners << listener
            case GeneratorEvent.EventType.FieldDefinition: fieldDefinitionListeners << listener
            case GeneratorEvent.EventType.MethodsStarted: methodsListeners << listener
            case GeneratorEvent.EventType.MethodsComplete: methodsListeners << listener
            case GeneratorEvent.EventType.MethodCreationStarted: methodCreationListeners << listener
            case GeneratorEvent.EventType.MethodCreationComplete: methodCreationListeners << listener
            case GeneratorEvent.EventType.MethodHeader: methodHeaderListeners << listener
            case GeneratorEvent.EventType.MethodBody: methodBodyListeners << listener
            case GeneratorEvent.EventType.LiteralsStarted: literalsListeners << listener
            case GeneratorEvent.EventType.LiteralsComplete: literalsListeners << listener
            case GeneratorEvent.EventType.LiteralCreationStarted: literalCreationListeners << listener
            case GeneratorEvent.EventType.LiteralCreationComplete: literalCreationListeners << listener
            case GeneratorEvent.EventType.Disregard: disregardListeners << listener
        }
    }

    void fireUnitCreationStarted(key, role, builder, ownerKey) {
        body(key, role, builder, GeneratorEvent.EventType.UnitCreationStarted, ownerKey, unitCreationListeners)
    }
    
    void fireUnitCreationComplete(key, role, builder, ownerKey) {
        body(key, role, builder, GeneratorEvent.EventType.UnitCreationComplete, ownerKey, unitCreationListeners)
    }
    
    void fireTypesStarted(key, role, builder, ownerKey) {
        body(key, role, builder, GeneratorEvent.EventType.TypesStarted, ownerKey, typesListeners)
    }
    
    void fireTypesComplete(key, role, builder, ownerKey) {
        body(key, role, builder, GeneratorEvent.EventType.TypesComplete, ownerKey, typesListeners)
    }
    
    void fireTypeCreationStarted(key, role, builder, ownerKey) {
        body(key, role, builder, GeneratorEvent.EventType.TypeCreationStarted, ownerKey, typeCreationListeners)
    }
    
    void fireTypeCreationComplete(key, role, builder, ownerKey) {
        body(key, role, builder, GeneratorEvent.EventType.TypeCreationComplete, ownerKey, typeCreationListeners)
    }
    
    void fireTypeHeader(key, role, builder, ownerKey) {
        body(key, role, builder, GeneratorEvent.EventType.TypeHeader, ownerKey, typeHeaderListeners)
    }
    
    void fireTypeBody(key, role, builder, ownerKey) {
        body(key, role, builder, GeneratorEvent.EventType.TypeBody, ownerKey, typeBodyListeners)
    }
    
    void fireFieldsStarted(key, role, builder, ownerKey) {
        body(key, role, builder, GeneratorEvent.EventType.FieldsStarted, ownerKey, fieldsListeners)
    }
    
    void fireFieldsComplete(key, role, builder, ownerKey) {
        println "FIELDS COMPLETE EVENT"
        println "\trole: ${role}"
        body(key, role, builder, GeneratorEvent.EventType.FieldsComplete, ownerKey, fieldsListeners)
    }
    
    void fireFieldCreationStarted(key, role, builder, ownerKey) {
        body(key, role, builder, GeneratorEvent.EventType.FieldCreationStarted, ownerKey, fieldCreationListeners)
    }
    
    void fireFieldCreationComplete(key, role, builder, ownerKey) {
        body(key, role, builder, GeneratorEvent.EventType.FieldCreationComplete, ownerKey, fieldCreationListeners)
    }
    
    void fireFieldInit(key, role, builder, ownerKey) {
        body(key, role, builder, GeneratorEvent.EventType.FieldInitialization, ownerKey, fieldInitListeners)
    }
    
    void fireFieldDef(key, role, builder, ownerKey) {
        body(key, role, builder, GeneratorEvent.EventType.FieldDefinition, ownerKey, fieldDefinitionListeners)
    }
    
    void fireMethodsStarted(key, role, builder, ownerKey) {
        body(key, role, builder, GeneratorEvent.EventType.MethodsStarted, ownerKey, methodsListeners)
    }
    
    void fireMethodsComplete(key, role, builder, ownerKey) {
        body(key, role, builder, GeneratorEvent.EventType.MethodsComplete, ownerKey, methodsListeners)
    }
    
    void fireMethodCreationStarted(key, role, builder, ownerKey) {
        body(key, role, builder, GeneratorEvent.EventType.MethodCreationStarted, ownerKey, methodCreationListeners)
    }
    
    void fireMethodCreationComplete(key, role, builder, ownerKey) {
        body(key, role, builder, GeneratorEvent.EventType.MethodCreationComplete, ownerKey, methodCreationListeners)
    }
    
    void fireMethodHeader(key, role, builder, ownerKey) {
        body(key, role, builder, GeneratorEvent.EventType.MethodHeader, ownerKey, methodHeaderListeners)
    }
    
    void fireMethodBody(key, role, builder, ownerKey) {
        body(key, role, builder, GeneratorEvent.EventType.MethodBody, ownerKey, methodBodyListeners)
    }
    
    void fireLiteralsStarted(key, role, builder, ownerKey) {
        body(key, role, builder, GeneratorEvent.EventType.LiteralsStarted, ownerKey, literalsListeners)
    }
    
    void fireLiteralsCompleted(key, role, builder, ownerKey) {
        body(key, role, builder, GeneratorEvent.EventType.LiteralsComplete, ownerKey, literalsListeners)
    }
    
    void fireLiteralCreationStarted(key, role, builder, ownerKey) {
        body(key, role, builder, GeneratorEvent.EventType.LiteralCreationStarted, ownerKey, literalCreationListeners)
    }
    
    void fireLiteralCreationComplete(key, role, builder, ownerKey) {
        body(key, role, builder, GeneratorEvent.EventType.LiteralCreationComplete, ownerKey, literalCreationListeners)
    }
    
    void fireDisregard(key) {
        body(key, role, builder, GeneratorEvent.EventType.Disregard, null, disregardListeners)
    }
    
    def body = { key, role, builder, type, ownerKey, listeners ->
        def evt = new GeneratorEvent(key: key, role: role, builder: builder, type: type, ownerKey: ownerKey)
        listeners.each { it.handleEvent(evt) }
    }
    
    void clear() {
        registered = []
        unitCreationListeners = []
        typesListeners = []
        typeCreationListeners = []
        typeHeaderListeners = []
        typeBodyListeners = []
        fieldsListeners = []
        fieldCreationListeners = []
        fieldInitListeners = []
        fieldDefinitionListeners = []
        methodsListeners = []
        methodCreationListeners = []
        methodHeaderListeners = []
        methodBodyListeners = []
        literalsListeners = []
        literalCreationListeners = []
        disregardListeners = []
    }
}
