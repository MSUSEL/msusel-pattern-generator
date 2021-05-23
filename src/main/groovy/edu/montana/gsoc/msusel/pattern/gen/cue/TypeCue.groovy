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


import edu.isu.isuese.datamodel.Component
import edu.isu.isuese.datamodel.Field
import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.pattern.gen.generators.pb.RBML2DataModelManager
import edu.montana.gsoc.msusel.rbml.model.Classifier
import groovy.transform.TupleConstructor

@TupleConstructor(includeSuperProperties = true, includeSuperFields = true, excludes = ["fieldCues", "methodCues"])
class TypeCue extends CueContainer {

    Map<String, FieldCue> fieldCues
    Map<String, MethodCue> methodCues

    @Override
    def getDelimString() {
        return (/(?ms)start_type: ${name}.*?end_type: ${name}/)
    }

    @Override
    def getReplacement() {
        return "[[type: ${name}]]"
    }

    @Override
    def getCueForRole(String roleName, Component c) {
        Cue retVal = null
        if (c instanceof Type && name == roleName) {
            retVal = this
        }
        else {
            for (child in children) {
                if (child.value.hasCueForRole(roleName, c)) {
                    retVal = child.value.getCueForRole(roleName, c)
                    break
                }
            }
        }
        return retVal
    }

    @Override
    def hasCueForRole(String roleName, Component t) {
        boolean retVal = false
        if (t instanceof Type) {
            retVal = name == roleName
        }
        else {
            for (child in children) {
                if (child.value.hasCueForRole(roleName, t)) {
                    retVal = true
                    break
                }
            }
        }
        return retVal
    }

    @Override
    String content(String text, Component comp, CueParams params, RBML2DataModelManager manager) {
        Type type = (Type) comp
        Classifier compRole = (Classifier) manager.getRole((Type) comp)
        compRole.behFeats.each { role ->
            String combined = ""
            Cue cue = null
            def comps = manager.getComponentsByRole(role).findAll { ((Method) it).parentType == type }
            comps.each { meth ->
                cue = getCueForRole(role.name, (Component) meth)
                if (cue) {
                    combined += cue.compile(meth, params, manager) + "\n    "
                }
            }
            if (cue) {
                combined = combined.trim()
                text = text.replaceAll(cue.replacement, combined)
            }
        }

        compRole.structFeats.each { role ->
            String combined = ""
            Cue cue = null
            manager.getComponentsByRole(role).each { fld ->
                cue = getCueForRole(role.name, (Component) fld)
                if (cue)
                    combined += cue.compile((Component) fld, params, manager) + "\n    "
            }
            if (cue) {
                combined = combined.trim()
                text = text.replaceAll(cue.replacement, combined)
            }
        }
        text
    }
}
