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
package edu.montana.gsoc.msusel.pattern.cue

import java.nio.file.DirectoryStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class CueManager {

    def static patterns = [:]

    def static reset() {
        patterns = [:]
    }

    def static loadCues(String loc) {
        println "Loading pattern cues"
        CueScriptLoader loader = new CueScriptLoader()
        Path p = Paths.get(loc)

        Files.newDirectoryStream(p, new DirectoryStream.Filter<Path>() {
            @Override
            boolean accept(Path entry) throws IOException {
                return entry.fileName.toString().endsWith(".pgcl")
            }
        }).each {
            println it
            CuePattern pattern = (CuePattern) loader.load(it.toAbsolutePath().toString())
            patterns[pattern.name] = pattern
            println "Pattern Loaded: " + pattern.name
        }
    }
}
