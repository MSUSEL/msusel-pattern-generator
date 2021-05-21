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

import com.google.common.collect.Lists
import edu.isu.isuese.datamodel.Component
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.pattern.gen.generators.pb.RBML2DataModelManager
import edu.montana.gsoc.msusel.rbml.model.Role
import groovy.transform.TupleConstructor
import groovy.util.logging.Log4j2

@Log4j2
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
            text = text.replaceAll(/\[\[$key\]\]/, value)
        }

        return text
    }

    String content(String text, Component comp, CueParams params, RBML2DataModelManager manager) {
        return text
    }

    String postContent(String text, Component comp, CueParams params, RBML2DataModelManager manager) {
        keys(text).each { key ->
            if (key.contains(".")) {
                String roleName = key.split(/\./)[0]
                String property = key.split(/\./)[1]
                Role role = manager.findRoleByName(roleName)
                log.info "Looking for role with name: $roleName"
                List<Type> types = manager.getTypes(role)

                switch (property) {
                    case "root":
                        String rootName = manager.getTypes(role).first().name
                        text = replace(text, key as String, rootName)
                        break
                    case "count":
                        text = replace(text, key as String, "${types.size()}")
                        break
                    case "random":
                        if (types.size() > 0) {
                            String name = randomSelect(types)?.name
                            text = replace(text, key as String, name)
                        }
                        break
                    case "name":
                        List comps = Lists.newArrayList(manager.getComponentsByRole(role))
                        String name = randomSelect(comps)?.name
                        text = replace(text, key as String, name)
                        break
                }
            } else {
                switch (key) {
                    case "name":
                        text = replace(text, key as String, comp.name)
                        break
                    default:
                        text = replace(text, key as String, "")
                        break
                }
            }
        }

        return text
    }

    private static String replace(String current, String key, String value) {
        return current.replaceAll(/\[\[$key\]\]/, value)
    }

    private def keys(String text) {
        def keys = []
        def pattern = ~/(?ms)\[\[(?<content>[\w\.\d\s:]*?)\]\]/
        def results = (text =~ pattern)
        for (int i = 0; i < results.size(); i++)
            keys << results[i][1]

        return keys
    }

    private static def randomSelect(List list) {
        Random rand = new Random()
        if (list.size() > 0) {
            int index = rand.nextInt(list.size())
            return list[index]
        }
        return null
    }

    abstract def getDelimString()

    abstract def getReplacement()

    abstract def getCueForRole(String roleName, Component c)

    abstract def hasCueForRole(String roleName, Component t)

    @Override
    String toString() {
        return name
    }
}
