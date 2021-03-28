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
package edu.montana.gsoc.msusel.pattern.gen.generators.pb

import edu.isu.isuese.datamodel.*
import edu.montana.gsoc.msusel.pattern.gen.logging.LoggerInit
import edu.montana.gsoc.msusel.rbml.model.Classifier
import edu.montana.gsoc.msusel.rbml.model.InterfaceRole
import groovy.util.logging.Log

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log
class ClassifierBuilder extends AbstractBuilder {

    ClassifierBuilder() {
        LoggerInit.init(log)
    }

    Type create() {
        if (!params.classifier)
            throw new IllegalArgumentException("classifier cannot be null")

        Type t
        String name = getClassName()
        String compKey = ((Namespace) params.ns).getNsKey() + ":" + name
        if (Class.findFirst("compKey = ?", compKey)) {
            return Class.findFirst("compKey = ?", compKey)
        }
        if (params.classifier instanceof InterfaceRole) {
            t = Interface.builder()
                    .name(name)
                    .accessibility(Accessibility.PUBLIC)
                    .compKey(compKey)
                    .create()
            if (((Classifier) params.classifier).isAbstrct())
                t.addModifier(Modifier.forName("ABSTRACT"))
        } else {
            t = Class.builder()
                    .name(name)
                    .accessibility(Accessibility.PUBLIC)
                    .compKey(compKey)
                    .create()
            if (((Classifier) params.classifier).isAbstrct())
                t.addModifier(Modifier.forName("ABSTRACT"))
        }

        ctx.rbmlManager.addMapping((Classifier) params.classifier, t)

        ctx.fileBuilder.init(parent: params.ns, typeName: t.getName())
        File f = ctx.fileBuilder.create()

        ((Namespace) params.ns).addType(t)
        f.addType(t)

        t
    }

    void createFeatures(Classifier classifier) {
        log.info("Creating features for ${classifier.name}")
        if (!classifier)
            throw new IllegalArgumentException("createFeatures: classifier cannot be null")
        ctx.rbmlManager.getTypes(classifier).each { Type t ->
            log.info("Number of structural features: ${classifier.structFeats.size()}")
            log.info("Number of behavioral features: ${classifier.behFeats.size()}")
            classifier.structFeats.each {
                ctx.fldBuilder.init(feature: it)
                t.addMember((Field) ctx.fldBuilder.create())
            }

            classifier.behFeats.each {
                ctx.methBuilder.init(feature: it)
                t.addMember((Method) ctx.methBuilder.create())
            }
        }
        log.info("Done creating features for ${classifier.name}")
    }

    private String getClassName() {
        String name = ""
        Random rand = new Random()
        int nums = rand.nextInt(3) + 1
        nums.each{ int entry ->
            String[] lines = ClassifierBuilder.class.getResourceAsStream("/edu/montana/gsoc/msusel/pattern/gen/classnames${entry}.txt").readLines()
            int ndx = rand.nextInt(lines.length)
            name += lines[ndx]
        }

        name
    }
}
