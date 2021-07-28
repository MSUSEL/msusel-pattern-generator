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

import com.google.common.collect.Sets
import edu.isu.isuese.datamodel.*
import edu.montana.gsoc.msusel.pattern.gen.methodnames.MethodNameGenerator
import edu.montana.gsoc.msusel.pattern.gen.methodnames.NameGenManager
import edu.montana.gsoc.msusel.rbml.model.Classifier
import edu.montana.gsoc.msusel.rbml.model.InterfaceRole
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class ClassifierBuilder extends AbstractBuilder {

    ClassifierBuilder() {
    }

    Type create() {
        if (!params.classifier)
            throw new IllegalArgumentException("classifier cannot be null")

        Type t
        String name = getClassName() + ((Classifier) params.classifier).name
        String compKey = ((Namespace) params.ns).getNsKey() + ":" + name
        if (Type.findFirst("compKey = ?", compKey)) {
            return Type.findFirst("compKey = ?", compKey)
        }
        if (params.classifier instanceof InterfaceRole) {
            t = Type.builder().type(Type.INTERFACE)
                    .name(name)
                    .accessibility(Accessibility.PUBLIC)
                    .compKey(compKey)
                    .create()
            if (((Classifier) params.classifier).isAbstrct())
                t.addModifier(Modifier.forName("ABSTRACT"))
        } else {
            t = Type.builder().type(Type.CLASS)
                    .name(name)
                    .accessibility(Accessibility.PUBLIC)
                    .compKey(compKey)
                    .create()
            if (((Classifier) params.classifier).isAbstrct())
                t.addModifier(Modifier.forName("ABSTRACT"))
        }

        ctx.fileBuilder.init(parent: params.ns, typeName: t.getName())
        File f = ctx.fileBuilder.create()

        ((Namespace) params.ns).addType(t)
        f.addType(t)

        ((Namespace) params.ns).updateKey()
        t.save()
        ctx.rbmlManager.addMapping((Classifier) params.classifier, t)

        t
    }

    void createFeatures(Classifier classifier, String pattern) {
        if (!classifier)
            throw new IllegalArgumentException("createFeatures: classifier cannot be null")

        log.info("Creating features for ${classifier.name}")
        createFieldNames(classifier)
        createMethodNames(classifier, pattern)

        ctx.rbmlManager.getTypes(classifier).each { Type t ->
            classifier.structFeats.each {structFeat ->
                ctx.rbmlManager.fieldNames[structFeat.name].each {name ->
                    ctx.fldBuilder.init(owner: t, feature: structFeat, fieldName: name)
                    ctx.fldBuilder.create()
                }
            }

            classifier.behFeats.each {behFeat ->
                ctx.rbmlManager.methodNames[behFeat.name].each {name ->
                    ctx.methBuilder.init(owner: t, feature: behFeat, methodName: name)
                    ctx.methBuilder.create()
                }
            }
        }
        log.info("Done creating features for ${classifier.name}")
    }

    private List createFieldNames(Classifier classifier) {
        classifier.structFeats.each {
            if (!ctx.rbmlManager.fieldNames[it.name]) {
                int min = it.getMult().lower
                int max = it.getMult().upper
                if (min < 0) {
                    max = ctx.maxFields
                    min = max
                }
                else if (max < 0) {
                    max = ctx.maxMethods
                }

                Set<String> set = Sets.newHashSet()
                Random rand = new Random()

                int num
                if (min == max)
                    num = max
                else
                    num = rand.nextInt(max - min) + min
                for (int i = 0; i < num; i++)
                    set << ctx.fldBuilder.getFieldName()
                ctx.rbmlManager.fieldNames[it.name] = set
            }
        }
    }

    private List createMethodNames(Classifier classifier, String pattern) {
        Set<String> allNames = [] as Set<String>
        ctx.rbmlManager.methodNames.values().each {
            allNames.addAll(it)
        }

        classifier.behFeats.each {
            if (!ctx.rbmlManager.methodNames[it.name]) {
                int min = it.getMult().lower
                int max = it.getMult().upper
                if (min < 0) {
                    max = ctx.maxMethods
                    min = max
                }
                else if (max < 0) {
                    max = ctx.maxMethods
                }

                NameGenManager mgr = NameGenManager.instance
                mgr.min = min
                mgr.max = max
                mgr.ctx = ctx
                MethodNameGenerator gen = mgr.getNameGeneratorFor(pattern, classifier.name, it.name)

                Set<String> set = gen.generate(allNames)
                ctx.rbmlManager.methodNames[it.name] = set
            }
        }
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
