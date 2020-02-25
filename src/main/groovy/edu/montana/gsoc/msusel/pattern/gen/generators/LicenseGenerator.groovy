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

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class LicenseGenerator extends AbstractGenerator {

    def final licenses = [
            "Boost Software License 1.0"   : "bsl-1.0",
            "Zero BSD License"             : "0bsd.txt",
            "BSD 2-clause"                 : "bsd-2-clause.txt",
            "BSD 3-clause"                 : "bsd-3-clause.txt",
            "BSD 3-clause Clear"           : "bsd-3-clause-clear.txt",
            "Educational Community License": "ecl-2.0.txt",
            "ISC"                          : "isc.txt",
            "MIT"                          : "mit.txt",
            "PostgreSQL License"           : "postgresql.txt",
            "NCSA Open Source License"     : "ncsa.txt",
            "Universal Permissive License" : "upl-1.0.txt",
            "The Unlicense"                : "unlicense.txt",
            "zLib License"                 : "zlib.txt"
    ]

    def generate() {
        generate((FileTreeBuilder) params.tree, ctx.license.name, ctx.license.year.toString(), ctx.license.holder, ctx.license.project, ctx.license.url)
    }

    void generate(FileTreeBuilder tree, String name, String year, String holderName, String project, String projecturl) {
        if (!tree)
            throw new IllegalArgumentException("File Builder cannot be null")
        if (!name)
            throw new IllegalArgumentException("Name cannot be null or empty")
        if (!year)
            throw new IllegalArgumentException("Year cannot be null or empty")
        if (!holderName)
            throw new IllegalArgumentException("Holder Name cannot be null or empty")
        if (!project)
            throw new IllegalArgumentException("Project Name cannot be null or empty")
        if (!projecturl)
            throw new IllegalArgumentException("Project URL cannot be null or empty")

        String output = ""
        if (licenses[name]) {
            LicenseGenerator.class.getResourceAsStream("/licenses/" + licenses[name]).withReader {
                it.readLines().each { String line ->
                    if (line.contains("[year]")) {
                        line = line.replaceAll(/\[year]/, year)
                    } else if (line.contains("[yyyy]")) {
                        line = line.replaceAll(/\[yyyy]/, year)
                    }

                    if (line.contains("[fullname]")) {
                        line = line.replaceAll(/\[fullname]/, holderName)
                    } else if (line.contains("[name of copyright owner]")) {
                        line = line.replaceAll(/\[name of copyright owner]/, holderName)
                    }

                    if (line.contains("[project]")) {
                        line = line.replaceAll(/\[project]/, project)
                    }

                    if (line.contains("[projecturl]")) {
                        line = line.replaceAll(/\[projecturl]/, projecturl)
                    }

                    output += "$line\n"
                }
            }
        }
        tree.'LICENSE'(output)
    }
}
