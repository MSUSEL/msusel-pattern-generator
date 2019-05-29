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

import edu.isu.isuese.datamodel.Class
import edu.isu.isuese.datamodel.Constructor
import edu.isu.isuese.datamodel.Field
import edu.isu.isuese.datamodel.Import
import edu.isu.isuese.datamodel.Literal
import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Enum
import edu.isu.isuese.datamodel.Interface
import edu.isu.isuese.datamodel.Namespace
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.pattern.gen.AbstractSourceBuilder

/**
 * Java SrcML Builder. Generates an SrcML implementation of design patterns from a Code Tree representation.
 * @author Isaac Griffith
 * @version 1.3.0
 */
class JavaSourceBuilder extends AbstractSourceBuilder {

    @Override
    def createTypeHeader(String designator, Type typ, StringBuilder builder) {
        evtMgr.fireTypeHeader(typ.compKey, null, builder, typ.compKey)
        builder << "${typ.getAccessibility().toString().toLowerCase()} "
        switch (typ) {
            case Interface:
                builder << "interface ${typ.getName()}"
                if (typ.getGeneralizes()) {
                    builder << " extends ${typ.getGeneralizes().first().getName()}"
                }
                builder " {\n"
                break
            case Class:
                if (typ.isAbstract())
                    builder << "abstract "
                builder << "class ${typ.getName()}"
                if (typ.getGeneralizes()) {
                    builder << " extends ${typ.getGeneralizes().first().getName()}"
                }
                if (typ.getRealizes()) {
                    builder << " implements "
                    List<Type> real = typ.getRealizes()
                    real.eachWithIndex { Type entry, int i ->
                        builder << "${entry.getName()}"
                        if (i < real.size())
                            builder << ", "
                    }
                }
                builder " {\n"
                break
            case Enum:
                builder << "enum ${typ.getName()}"
                if (typ.getRealizes()) {
                    builder << " implements "
                    List<Type> real = typ.getRealizes()
                    real.eachWithIndex { Type entry, int i ->
                        builder << "${entry.getName()}"
                        if (i < real.size())
                            builder << ", "
                    }
                }
                builder << " {\n"
        }
    }

    @Override
    def createEnumItems(Enum type, StringBuilder builder) {
        evtMgr.fireLiteralsStarted()

        type.getLiterals().each { Literal l ->
            evtMgr.fireLiteralCreationStarted()

            evtMgr.fireLiteralCreationComplete()
        }

        evtMgr.fireLiteralsCompleted()
    }

    @Override
    def createFields(Type t, StringBuilder builder) {
        evtMgr.fireFieldsStarted()

        t.getFields().each { Field f ->
            evtMgr.fireFieldCreationStarted()

            evtMgr.fireFieldCreationComplete()
        }

        evtMgr.fireFieldsComplete()
    }

    @Override
    def createMethods(Type typ, StringBuilder builder) {
        evtMgr.fireMethodsStarted(typ.compKey, null, builder, typ.compKey)
        typ.getMethods().each { Method method ->
            evtMgr.fireMethodCreationStarted(method.compKey, lookupFeatureRole(method), builder, typ.compKey)
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
            evtMgr.fireMethodCreationComplete(method.compKey, lookupFeatureRole(method), builder, typ.compKey)
        }

    }

    private void createMethod(Type typ, Method method, StringBuilder builder) {

    }

    private void createConstructor(Type typ, Constructor method, StringBuilder builder) {

    }

    @Override
    def handleNamespace(File file, StringBuilder builder) {
        Namespace ns = file.getParentNamespaces().first()
        if (ns) {
            builder << "package ${ns.getName()};\n"
        }
    }

    @Override
    def handleImports(File file, StringBuilder builder) {
        file.getImports().each { Import imp ->
            builder << "import ${imp.getName()};\n"
        }
    }

    @Override
    def createTypeComment(Type type, StringBuilder builder) {
        builder << "/*\n"
        builder << " * @author Isaac Griffith\n"
        builder << " * @version 1.0\n"
        builder << " */\n"
    }

    @Override
    def createMethodComment(Type type, Method method, StringBuilder builder) {
        builder << "/*\n"
        builder << " * @param ${}\n"
        builder << " * @return ${}\n"
        builder << " */"
    }

    @Override
    String getLanguage() {
        "Java"
    }

    @Override
    def createTypes(File file, StringBuilder builder) {
        evtMgr.fireTypesStarted()

        file.getTypes().each { Type t ->
            createTypeHeader()
            switch (t) {
                case Interface:
                    createInterface((Interface) t, builder)
                    break
                case Class:
                    createClass((Class) t, builder)
                    break
                case Enum:
                    createEnum((Enum) t, builder)
            }
        }

        evtMgr.fireTypesComplete()
    }

    void createClass(Class typ, builder) {
        createFields(typ, builder)
        createMethods(typ, builder)
    }

    void createEnum(Enum typ, builder) {
        createLiterals(typ, builder)
        createFields(typ, builder)
        createMethods(typ, builder)
    }

    void createInterface(Interface typ, builder) {
        createFields(typ, builder)
        createMethods(typ, builder)
    }

    @Override
    def createUnitContents(File file, StringBuilder builder) {
        evtMgr.fireUnitCreationStarted()

        handleNamespace(file, builder)
        builder << "\n"
        handleImports(file, builder)

        evtMgr.fireUnitCreationComplete()
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
