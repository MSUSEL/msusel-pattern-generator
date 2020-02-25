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
package edu.montana.gsoc.msusel.pattern.gen.generators


import edu.isu.isuese.datamodel.Project

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
abstract class BuildFileGenerator extends AbstractGenerator {

    @Override
    def generate() {
        if (!params.tree)
            throw new IllegalArgumentException("File Tree Builder cannot be null")
        if (!params.project)
            throw new IllegalArgumentException("generate: project cannot be null")

        FileTreeBuilder tree = (FileTreeBuilder) params.tree
        Project project = (Project) params.project

        generateBuild(tree)
        generateSettings(tree, project)
    }

    void generateSubproject(FileTreeBuilder tree) {
        if (!tree)
            throw new IllegalArgumentException("File Tree Builder cannot be null")

        generateSubprojectBuild(tree)
    }

    void generateRootProject(FileTreeBuilder tree, Project project) {
        if (!tree)
            throw new IllegalArgumentException("File Tree Builder cannot be null")

        generateRootProjectBuild(tree, project)
        generateRootProjectSettings(tree, project)
    }

    abstract void generateSubprojectBuild(FileTreeBuilder tree)

    abstract void generateRootProjectBuild(FileTreeBuilder tree, Project proj)

    abstract void generateRootProjectSettings(FileTreeBuilder tree, Project proj)

    abstract void generateBuild(FileTreeBuilder tree)

    abstract void generateSettings(FileTreeBuilder tree, Project project)
}
