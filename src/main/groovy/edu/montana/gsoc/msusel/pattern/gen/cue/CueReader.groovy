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
package edu.montana.gsoc.msusel.pattern.gen.cue

@Singleton
class CueReader {

    final def delims = [
            "pattern": new Tuple2("start_pattern:", "end_pattern:"),
            "type"   : new Tuple2("start_type:", "end_type:"),
            "method" : new Tuple2("start_method:", "end_method:"),
            "field"  : new Tuple2("start_field:", "end_field:")
    ]

    final def nextTypes = [
            "pattern": [CueType.Type],
            "type"   : [CueType.Method, CueType.Field],
            "field"  : [],
            "method" : []
    ]

    def read(path) {
        String text = CueReader.class.getResource(path).text

        String ppText = preProcess(text)
        return processCueText(null, CueType.Pattern, ppText)
    }

    String preProcess(String text) {
        List<String> lines = text.split("\n")
        lines.removeIf { it.startsWith("#") }
        return lines.join("\n")
    }

    def processCueText(Cue parent, CueType type, String text) {
        def cues = [:]
        Cue cue
        int end
        int start = 0
        int current = 0
        String startDelim = delims[type.toString().toLowerCase()].first()
        String endDelim = delims[type.toString().toLowerCase()].last()
        List<String> lines = text.split("\n")

        for (String line : lines) {
            if (line.trim().startsWith(startDelim)) {
                start = current
                String name = line.split(": ")[1]
                cue = CueFactory.instance.createCue(type, name)
                if (parent && parent instanceof CueContainer) {
                    parent.addChildCue(cue)
                } else {
                    cues[cue.name] = cue
                }
            } else if (line.trim().startsWith(endDelim)) {
                end = current
                String cueText = String.join("\n", lines.subList(start + 1, end))
                for (CueType next : nextTypes[type.toString().toLowerCase()]) {
                    processCueText(cue, next, cueText)
                }
                cue.templateText = cueText.trim()

                if (cue instanceof CueContainer) {
                    cue.children.values().each {
                        cue.templateText = cue.templateText.replaceAll(it.delimString, it.replacement)
                    }
                }
            }
            current++
        }

        return cues
    }
}
