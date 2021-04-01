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

import edu.isu.isuese.datamodel.Classifier
import edu.isu.isuese.datamodel.Component
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.pattern.gen.generators.pb.RBML2DataModelManager
import edu.montana.gsoc.msusel.rbml.model.Role
import groovy.transform.TupleConstructor

@TupleConstructor(includeFields = true, includeProperties = true)
abstract class Cue {
    protected String name
    protected String templateText

    String compile(Component comp, CueParams params, RBML2DataModelManager manager) {
        String compiledText = templateText
        compiledText = preContent(compiledText, params)
        compiledText = content(compiledText, comp, params, manager)
        compiledText = postContent(compiledText, comp, params, manager)

        return compiledText
    }

    String preContent(String text, CueParams params) {
        params.params.each { key, value ->
            text = text.replaceAll("[[$key]]", value)
        }

        return text
    }

    String content(String text, Component comp, CueParams params, RBML2DataModelManager manager) {
        return text
    }

    String postContent(String text, Component comp, CueParams params, RBML2DataModelManager manager) {
        keys(text).each { String key ->
            if (key.contains(".")) {
                String roleName = key.split(/\./)[0]
                String property = key.split(/\./)[1]
                Role role = manager.findRoleByName(roleName)
                List<Type> types = manager.getTypes(role)

                switch (property) {
                    case "root":
                        String rootName = types.find {
                            it.getGeneralizedBy().isEmpty()
                        }.name
                        text = replace(text, key, rootName)
                        break
                    case "count":
                        text = replace(text, key, "${types.size()}")
                        break
                    case "random":
                        if (types.size() > 0) {
                            String name = randomSelect(types)?.name
                            text = replace(text, key, name)
                        }
                        break
                }
            } else {
                switch (key) {
                    case "name":
                        text = replace(text, key, comp.name)
                        break
                }
            }
        }

        return text
    }

    private String replace(String current, String key, String value) {
        return current.replaceAll(key, value)
    }

    private def keys(String text) {
        def keys = []
        def pattern = ~/(?ms)\[\[(?<content>[\w\.\d\s]*?)\]\]/
        def results = (text =~ pattern)
        for (int i = 0; i < results.size(); i++)
            keys << results[i][1]
    }

    private Type randomSelect(List<Type> types) {
        Random rand = new Random()
        if (types.size() > 0) {
            int index = rand.nextInt(types.size())
            return types[index]
        }
        return null
    }

    abstract def getDelimString()

    abstract def getReplacement()

    abstract def getCueForRole(String roleName, Classifier c)

    abstract def hasCueForRole(String roleName, Component t)
}
