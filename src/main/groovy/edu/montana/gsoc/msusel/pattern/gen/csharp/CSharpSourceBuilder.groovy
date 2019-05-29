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
package edu.montana.gsoc.msusel.pattern.gen.csharp

import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Import
import edu.isu.isuese.datamodel.Type
import edu.isu.isuese.datamodel.Enum
import edu.montana.gsoc.msusel.pattern.gen.AbstractSourceBuilder

/**
 * C# srcML Builder
 * @author Isaac Griffith
 * @version 1.3.0
 */
class CSharpSourceBuilder extends AbstractSourceBuilder {

    /**
     * Constructs a new C# SrcML Builder
     */
    CSharpSourceBuilder()
    {
        // TODO Auto-generated constructor stub
    }

    @Override
    def createTypeHeader(String designator, Type typ, StringBuilder builder) {
    }
    
    @Override
    def createEnumItems(Enum typ, StringBuilder builder) {
    }
    
    @Override
    def createFields(Type typ, StringBuilder builder) {
        
    }
    
    @Override
    def createMethods(Type typ, StringBuilder builder) {
        
    }

    @Override
    def handleNamespace(File file, StringBuilder builder)
    {
        if (file.getNamespace()) {
            builder << "namespace "
            builder << file.getNamespace().getKey()
            builder << " {"
            builder << "\n"
            createTypes(file, builder)
            builder << "\n"
            builder << "}"
        }
    }

    @Override
    def handleImports(File file, StringBuilder builder)
    {
        // TODO need to handle initialized using statements
        file.getImports().each { Import imp ->
            builder << "using "
            builder << imp.getName()
            builder << ";\n"
        }
    }

    @Override
    def createTypeComment(Type type, StringBuilder builder)
    {
        builder << "/// \n"
        builder << "/// \n"
        builder << "/// \n"
    }

    @Override
    def createMethodComment(Type type, Method method, StringBuilder builder)
    {
        builder << "/// \n"
        builder << "/// \n"
        builder << "/// \n"
    }

    @Override
    String getLanguage()
    {
        return "C#"
    }

    @Override
    def createUnitContents(File file, StringBuilder builder)
    {
        builder << "\n"
        handleImports(file, builder)
        builder << "\n"
        
        handleNamespace(file, builder)
        builder << "\n"
    }

    @Override
    def createTypes(File file, StringBuilder builder)
    {

    }
    
    @Override
    def createProperty(Type type, StringBuilder builder)
    {

    }
}
