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
package edu.montana.gsoc.msusel.pattern.gen.generators.java

import edu.isu.isuese.datamodel.*
import edu.montana.gsoc.msusel.pattern.gen.generators.FileGenerator
import edu.montana.gsoc.msusel.pattern.gen.logging.LoggerInit
import groovy.util.logging.Log

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log
class JavaFileGenerator extends FileGenerator {

    JavaFileGenerator() {
        LoggerInit.init(log)
    }

    @Override
    def generate() {
        File file = (File) params.file
        FileTreeBuilder builder = (FileTreeBuilder) params.builder
        Project project = (Project) params.project

        // fire UnitCreationStarted event
//        events.fireUnitCreationStarted(file.fileKey, null, this, project.projectKey)

        builder."${file.getName()}"(
        """\
        ${createFileComment(file)}
        ${createPackageStatement(file)}${createImportStatements(file)}
        ${createTypes(file)}""".stripIndent()
        )

        // fire UnitCreationComplete event
//        events.fireUnitCreationComplete(file.fileKey, null, this, project.projectKey)
    }

    private def createFileComment(File file) {
        """/**
         * MIT License
         *
         * MSUSEL Design Pattern Generator
         * Copyright (c) 2015-2020 Montana State University, Gianforte School of Computing,
         * Software Engineering Laboratory and Idaho State University, Informatics and
         * Computer Science, eXtended Reality and Empirical Software Engineering Laboratory
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
        """
    }

    private def createPackageStatement(File file) {
        def pkg = ""
        if (file.getParentNamespace()) {
            Namespace parent = file.getParentNamespace()
            pkg = "package ${parent.getFullName()};"
        }
        pkg
    }

    private def createImportStatements(File file) {
        List<String> imports = file.getImports()*.getName()

        file.getAllTypedMembers().each {
            addTypeToImports(file, it.getType(), imports)

            if (it instanceof Method) {
                it.getParams().each {
                    addTypeToImports(file, it.getType(), imports)
                }
            }
        }

        String output = ""
        if (imports)
            output += "\n"
        imports.each {
            output += "\n        import ${it};"
        }
        if (!imports.contains("java.util.*"))
            output += "\n        import java.util.*;"
        output
    }

    private def addTypeToImports(File file, TypeRef type, List<String> imports) {
        if (type.getType() == TypeRefType.Type) {
            if (file.getParentProject()) {
                Type t = type.getType(file.getParentProject().getProjectKey())
                if (t) imports << t.getFullName()
            }
        }
    }

    private def createTypes(File file) {
        Project project = (Project) params.project

        // fire TypesStarted event
//        events.fireTypesStarted(file.fileKey, null, this, project.projectKey)

        def output = ""

        file.getAllTypes().each {
            ctx.typeGen.init(type: it, parent: file)
            output += "\n" + ctx.typeGen.generate()
        }

        // fire TypesComplete event
//        events.fireTypesComplete(file.fileKey, null, this, project.projectKey)

        output
    }
}
