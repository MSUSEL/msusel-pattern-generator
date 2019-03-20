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
package edu.montana.gsoc.msusel.pattern.gen.java

import edu.montana.gsoc.msusel.datamodel.member.Constructor
import edu.montana.gsoc.msusel.datamodel.member.Method
import edu.montana.gsoc.msusel.datamodel.structural.File
import edu.montana.gsoc.msusel.datamodel.type.Enum
import edu.montana.gsoc.msusel.datamodel.type.Interface
import edu.montana.gsoc.msusel.datamodel.type.Type
import edu.montana.gsoc.msusel.pattern.gen.AbstractSourceBuilder

/**
 * Java SrcML Builder. Generates an SrcML implementation of design patterns from a Code Tree representation.
 * @author Isaac Griffith
 * @version 1.3.0
 */
class JavaSourceBuilder extends AbstractSourceBuilder {

    @Override
    def createTypeHeader(String designator, Type typ, StringBuilder builder) {

    }

    @Override
    def createEnumItems(Enum type, StringBuilder builder) {

    }

    @Override
    def createFields(Type t, StringBuilder builder) {

    }

    @Override
    def createMethods(Type typ, StringBuilder builder) {
        evtMgr.fireMethodsStarted(typ.key, null, builder, typ.key)
        typ.methods().each { Method method ->
            evtMgr.fireMethodCreationStarted(method.key, lookupFeatureRole(method), builder, typ.key)
//            if (!currentCue?.roles["${lookupTypeRole(typ)}::${lookupFeatureRole(method)}"] || !currentCue?.roles["${lookupTypeRole(typ)}::${lookupFeatureRole(method)}"].disregard) {
                createMethodComment(typ, method, builder)
                builder << "\n    "
                if (method instanceof Constructor) {
                    createConstructor(typ, method, builder)
                } else {
                    createMethod(typ, method, builder)
                }
                builder << "\n"
//            }
            evtMgr.fireMethodCreationComplete(method.key, lookupFeatureRole(method), builder, typ.key)
        }

    }

    private void createMethod(Type typ, Method method, StringBuilder builder) {

    }

    private void createConstructor(Type typ, Constructor method, StringBuilder builder) {

    }

    @Override
    def handleNamespace(File file, StringBuilder builder) {

    }

    @Override
    def handleImports(File file, StringBuilder builder) {

    }

    @Override
    def createTypeComment(Type type, StringBuilder builder) {

    }

    @Override
    def createMethodComment(Type type, Method method, StringBuilder builder) {

    }

    @Override
    String getLanguage() {
        "Java"
    }

    @Override
    def createTypes(File file, StringBuilder builder) {
    }

    void createClass(Type typ, builder) {
    }

    void createEnum(Enum typ, builder) {
    }

    void createInterface(Interface typ, builder) {
    }

    @Override
    def createUnitContents(File file, StringBuilder builder) {

    }

    @Override
    def createProperty(Type type, StringBuilder builder) {

    }

    /**
     * Retrieves the role associated with the given Type
     * @param type Type
     * @return Role the given type is associated with
     */
    private def lookupTypeRole(type) {
        pg?.findTypeRole(type)
    }

    /**
     *
     * @param feat
     * @return
     */
    private def lookupFeatureRole(feat) {
        pg?.findFeatureRole(feat)
    }
}
