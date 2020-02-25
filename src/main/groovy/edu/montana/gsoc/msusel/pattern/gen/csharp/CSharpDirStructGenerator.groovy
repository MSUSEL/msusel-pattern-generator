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

import java.nio.file.Path

/**
 * Constructs the following directory structure for C# projects
 * 
 * base/ProjectName
 *   |--artifacts/
 *   |--build/
 *   |--docs/
 *   |--lib/
 *   |--packages/
 *   |--samples/
 *   |--src/
 *   |--tests/
 *   |--build.cmd
 *   |--build.sh
 *   |--LICENSE
 *   |--NuGet.Config
 *   |--README.md
 *   |--ProjectName.sln
 * 
 * @author Isaac Griffith
 * @version 1.3.0
 */
class CSharpDirStructGenerator {

    private Path projDir
    
    /**
     * Constructs a new C# Directory Structure Generator
     */
    CSharpDirStructGenerator() { }

    /**
     * {@inheritDoc}
     */
    void generateDirStructure() {
        def tree = new FileTreeBuilder()
        tree.dir("base\\${ProjectName}") {
           artifacts {}
           build {
               debug {}
               release {}
           }
           docs {}
           lib {}
           packages {}
           samples {}
           src {
               // Here we need to create separate project files and assembly info files
               // for each module in project construct a directory with a subdirectory "Properties" containing the AssemblyInfo.cs file
               //   each module folder will contain all the files and the .csproj file
               //   as each file is created it is added to a map (along with its UUID), in the FileGenerator for use when creating the solution file
               dir(project.getName()) {
                   Properties {
                       'AssemblyInfo.cs'(fileGen.generateAssemblyInfo())
                   }
                   //'App.config'(fileGen.generateAppConfigFile())
                   "${module.name()}.csproj"(fileGen.generateProjectFile(project))
               }
           }
           tests {} // Here we need to create separate project files and assembly info files
//           'build.cmd'()
//           'build.sh'()
           'LICENSE'(fileGen.createLicenseFile())
           'NuGet.Config'(fileGen.createNuGetConfigFile())
           'README.md'(fileGen.createReadmeMDFile())
           'ProjectName.sln'(fileGen.createSolutionFile())
           'sonar.properties'(fileGen.createSonarPropFile())
           'source.srcml'(patternGen.generate())
        }
    }
}
