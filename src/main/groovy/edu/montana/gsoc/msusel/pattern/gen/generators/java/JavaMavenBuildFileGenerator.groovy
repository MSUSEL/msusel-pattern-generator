/*
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

import edu.isu.isuese.datamodel.Project
import groovy.util.logging.Log4j2
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class JavaMavenBuildFileGenerator extends JavaBuildFileGenerator {

    JavaMavenBuildFileGenerator() {
    }

    @Override
    void generateSubprojectBuild(FileTreeBuilder tree) {

    }

    @Override
    void generateRootProjectBuild(FileTreeBuilder tree, Project proj) {

    }

    @Override
    void generateRootProjectSettings(FileTreeBuilder tree, Project proj) {

    }

    @Override
    void generateBuild(FileTreeBuilder tree) {
        log.info("Generating maven build")
        if (!tree)
            throw new IllegalArgumentException("File Tree Builder cannot be null")

        StreamingMarkupBuilder smb = new StreamingMarkupBuilder()
        smb.encoding = 'UTF-8'
        String str = smb.bind {
            mkp.xmlDeclaration(version:'1.0')
            project(xmlns: "http://maven.apache.org/POM/4.0.0", "xmlns:xsi": "http://www.w3.org/2001/XMLSchema-instance",
                    "xsi:schemaLocation": "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd") {
                modelVersion("4.0.0")
                groupId("dpgen")
                artifactId("${params.buildArtifact}")
                version("1.0.0.0")
                name("${params.buildProject}")
                description("${params.buildDescription}")
                mkp.yieldUnescaped("\n\n")
                mkp.yield("  ")
                organization {
                    name("Montana State University - Software Engineering Laboratory")
                    url("https://msusel.github.io")
                }
                mkp.yieldUnescaped("\n\n")
                mkp.yield("  ")
                licenses {
                    license {
                        name("The MIT License(MIT)")
                        url("https://opensource.org/licenses/mit-license.php")
                        distribution()
                    }
                }
                mkp.yieldUnescaped("\n\n")
                mkp.yield("  ")
                build {
                    plugins {
                        plugin {
                            groupId("org.apache.maven.plugins")
                            artifactId("maven-compiler-plugin")
                            version("3.1")
                            configuration {
                                source("1.8")
                                target("1.8")
                            }
                        }
                    }
                }
            }
        }
        tree.'pom.xml'(XmlUtil.serialize(str))
    }

    @Override
    void generateSettings(FileTreeBuilder tree, Project project) {

    }
}
