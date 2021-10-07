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

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class JavaGradleBuildFileGenerator extends JavaBuildFileGenerator {

    JavaGradleBuildFileGenerator() {
    }

    @Override
    void generateSubprojectBuild(FileTreeBuilder tree) {
        if (!tree)
            throw new IllegalArgumentException("File Tree Builder cannot be null")

        tree.'build.gradle'('''\
        // In this section you declare the dependencies for your production and test code
        dependencies {
            
        }
        '''.stripIndent())
    }

    @Override
    void generateRootProjectBuild(FileTreeBuilder tree, Project proj) {
        if (!tree)
            throw new IllegalArgumentException("File Tree Builder cannot be null")

        tree.'build.gradle'("""\
        // In this section you declare where to find the dependencies of your project
        allprojects {
            repositories {
                // Use 'jcenter' for resolving your dependencies.
                // You can declare any Maven/Ivy/file repository here.
                jcenter()
                mavenCentral()
            }
            
            plugins {
                id 'java'
            }
            
            sourceCompatibility = 1.8
            targetCompatibility = 1.8
            compileJava.options.fork = true
            
            // In this section you declare the dependencies for your production and test code
            dependencies {
                // The production code uses the SLF4J logging API at compile time
                implementation 'org.slf4j:slf4j-api:1.7.21'
                
                // Declare the dependency for your favourite test framework you want to use in your tests.
                // TestNG is also supported by the Gradle Test task. Just change the
                // testCompile dependency to testCompile 'org.testng:testng:6.8.1' and add
                // 'test.useTestNG()' to your build script.
                testImplementation 'junit:junit:4.12'
                testImplementation 'org.mockito:mockito-all:1.10.19'
            }
        }
        
        subprojects {
            version = '${proj.version}'
        }
        """.stripIndent())
    }

    @Override
    void generateRootProjectSettings(FileTreeBuilder tree, Project proj) {
        if (!tree)
            throw new IllegalArgumentException("File Tree Builder cannot be null")
        if (!proj)
            throw new IllegalArgumentException("project cannot be null or empty")

        def includes = []
        proj.getModules().each {
            includes << "'${it.getName()}'"
        }

        tree.'settings.gradle'("""\
        /*
        // To declare projects as part of a multi-project build use the 'include' method
        include 'shared'
        include 'api'
        include 'services:webservice'
        */
        
        rootProject.name = '${proj.name}'
        
        include ${includes.join(', ')}""".stripIndent()
        )

    }

    @Override
    void generateBuild(FileTreeBuilder tree) {
        log.info("generating build")
        if (!tree)
            throw new IllegalArgumentException("File Tree Builder cannot be null")

        tree.'build.gradle'("""\
        // Apply the java plugin to add support for Java
        plugins {
            id 'java'
        }
        
        sourceCompatibility = 1.8
        targetCompatibility = 1.8
        compileJava.options.fork = true
        compileJava.options.forkOptions.executable = '${this.ctx.javaBinary}'
        
        // In this section you declare where to find the dependencies of your project
        repositories {
            // Use 'jcenter' for resolving your dependencies.
            // You can declare any Maven/Ivy/file repository here.
            jcenter()
            mavenCentral()
        }
        
        // In this section you declare the dependencies for your production and test code
        dependencies {
            // The production code uses the SLF4J logging API at compile time
            implementation 'org.slf4j:slf4j-api:1.7.21'
            
            // Declare the dependency for your favourite test framework you want to use in your tests.
            // TestNG is also supported by the Gradle Test task. Just change the
            // testCompile dependency to testCompile 'org.testng:testng:6.8.1' and add
            // 'test.useTestNG()' to your build script.
            testImplementation 'junit:junit:4.12'
            testImplementation 'org.mockito:mockito-all:1.10.19'
        }
        """.stripIndent())
    }

    @Override
    void generateSettings(FileTreeBuilder tree, Project project) {
        if (!tree)
            throw new IllegalArgumentException("File Tree Builder cannot be null")
        if (!project)
            throw new IllegalArgumentException("project cannot be null or empty")

        tree.'settings.gradle'("""\
        /*
        // To declare projects as part of a multi-project build use the 'include' method
        include 'shared'
        include 'api'
        include 'services:webservice'
        */
        
        rootProject.name = '${project.name}'
        """.stripIndent())
    }
}
