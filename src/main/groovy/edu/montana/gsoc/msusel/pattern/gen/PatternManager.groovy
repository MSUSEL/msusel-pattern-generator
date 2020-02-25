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

import edu.isu.isuese.datamodel.Pattern
import edu.montana.gsoc.msusel.pattern.cue.Cue
import edu.montana.gsoc.msusel.pattern.cue.CuePattern
import edu.montana.gsoc.msusel.pattern.cue.CueScriptLoader
import edu.montana.gsoc.msusel.rbml.PatternLoader
import edu.montana.gsoc.msusel.rbml.model.SPS

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Singleton
class PatternManager {

    PatternLoader patternLoader = PatternLoader.getInstance()
    CueScriptLoader cueLoader = new CueScriptLoader()


    CuePattern loadPatternCues(String pattern) {
        cueLoader.loadPatternCues(getPatternName(pattern))
    }

    Cue loadAndSelectCue(String pattern) {
        CuePattern cues = this.loadPatternCues(getPatternName(pattern))
        def list = cues.cues.keySet().asList()
        if (list) {
            if (list.size() > 1) {
                Random rand = new Random()
                return (Cue) cues.cues[list[rand.nextInt(list.size())]]
            } else {
                return (Cue) cues.cues[list[0]]
            }
        }

        null
    }

    SPS loadPattern(String pattern) {
        patternLoader.loadPattern(getPatternName(pattern))
    }

    String getPatternName(String pattern) {
        return pattern.toLowerCase().replaceAll(/_/, ' ')
    }

    Pattern findPatternForName(String pattern) {
        pattern = getPatternName(pattern).capitalize()
        Pattern.findFirst("name = ?", pattern)
    }
}
