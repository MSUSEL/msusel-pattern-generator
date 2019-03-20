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
package edu.montana.gsoc.msusel.pattern.gen

import edu.montana.gsoc.msusel.codetree.CodeTree

/**
 * Abstract Base class for Project File Generation
 * @author Isaac Griffith
 * @version 1.3.0
 */
abstract class AbstractFileGenerator {

	/**
	 * Name of the pattern to be constructed
	 */
	String patternName
	/**
	 * Code Tree containing the data for the project to be generated
	 */
	CodeTree tree
	/**
	 * Project base path
	 */
	String base

	/**
	 * Constructs the Project Build file
	 * @return String content of the build file
	 */
	abstract def createBuildFile()

	/**
	 * Constructs the Project License File
	 * @return String content of the License file
	 */
	abstract def createLicenseFile()

	/**
	 * Constructs the Project Readme Markdown File
	 * @return String content of the Readme File
	 */
	abstract def createReadmeMDFile()

	abstract void generateFiles()
}
