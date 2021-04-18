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

import edu.montana.gsoc.msusel.pattern.gen.generators.GitIgnoreGenerator
import groovy.util.logging.Log4j2
import org.apache.logging.log4j.Level

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class JavaGitIgnoreGenerator extends GitIgnoreGenerator {

    JavaGitIgnoreGenerator() {
    }

    @Override
    def generate() {
        log.log(Level.INFO,"Generating GitIgnore File")
        if (!params.tree)
            throw new IllegalArgumentException("File Tree Builder cannot be null")

        FileTreeBuilder tree = (FileTreeBuilder) params.tree

        tree.'.gitignore'("""\
        ##########################
        ## Java
        ##########################
        *.class
        .mtj.tmp/
        *.jar
        *.war
        *.ear
        hs_err_pid*
        
        ##########################
        ## Maven
        ##########################
        target/
        pom.xml.tag
        pom.xml.releaseBackup
        pom.xml.versionsBackup
        pom.xml.next
        release.properties
        
        ##########################
        ## Gradle
        ##########################
        .gradle
        build/
        !gradle-wrapper.jar
        .gradletasknamecache
        
        ##########################
        ## IntelliJ
        ##########################
        *.iml
        .idea/
        *.ipr
        *.iws
        out/
        .idea_modules/
        
        ##########################
        ## Eclipse
        ##########################
        .metadata
        .classpath
        .project
        .settings/
        bin/
        tmp/
        *.tmp
        *.bak
        *.swp
        *~.nib
        local.properties
        .loadpath
        
        ##########################
        ## NetBeans
        ##########################
        nbproject/private/
        build/
        nbbuild/
        dist/
        nbdist/
        nbactions.xml
        nb-configuration.xml
        
        ##########################
        ## OS X
        ##########################
        .DS_Store
        
        ##########################
        ## Other
        ##########################
        *.log
        hs_err_pid*
        """.stripIndent())
    }
}
