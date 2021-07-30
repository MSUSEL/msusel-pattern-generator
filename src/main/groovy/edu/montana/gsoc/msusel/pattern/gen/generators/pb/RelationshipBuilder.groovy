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

import edu.isu.isuese.datamodel.Accessibility
import edu.isu.isuese.datamodel.Field
import edu.isu.isuese.datamodel.Namespace
import edu.isu.isuese.datamodel.RelationType
import edu.isu.isuese.datamodel.Type
import edu.isu.isuese.datamodel.TypeRef
import edu.montana.gsoc.msusel.pattern.gen.cue.Cue
import edu.montana.gsoc.msusel.pattern.gen.cue.CueManager
import edu.montana.gsoc.msusel.rbml.model.*
import org.apache.logging.log4j.Level

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class RelationshipBuilder extends AbstractComponentBuilder {

    Map<Role, Set<Type>> map = [:]
    Map<String, Map<String, Set<Type>>> ghmap = [:]

    def create() {
        if (!params.rbml)
            throw new IllegalArgumentException("create: rbml cannot be null")
        if (!params.ns)
            throw new IllegalArgumentException("create: ns cannot be null")

        Namespace ns = (Namespace) params.ns
        SPS sps = (SPS) params.rbml

        sps.relations.each { rel ->
            if (rel instanceof Relationship) {
                if (manager.getTypes(rel.source()))
                    processRole(ns, rel.source(), rel.srcPort)
                if (manager.getTypes(rel.dest()))
                    processRole(ns, rel.dest(), rel.destPort)
                selectAndCreateRelationship(rel)
            }
        }

        sps.genHierarchies.each {
            if (it instanceof GeneralizationHierarchy) {
                ctx.ghBuilder.init(ns: ns, gh: (GeneralizationHierarchy) it)
                ctx.ghBuilder.ghmap = ghmap
                ctx.ghBuilder.create()
            }
        }
    }

    void processRole(Namespace ns, Role role, String port) {
        if (!role)
            throw new IllegalArgumentException("processRole: role cannot be null")
        if (role instanceof GeneralizationHierarchy && !port)
            throw new IllegalArgumentException("processRole: if role is a generalization hierarchy then port cannot be null or empty")

        if (role instanceof GeneralizationHierarchy) {
            processGHRole(ns, role, port)
        } else if (role instanceof Classifier) {
            generateClassifier(ns, (Classifier) role)
        }
    }

    Set<Type> generateClassifier(Namespace ns, Classifier role, boolean ghRoot = false) {
        if (!role)
            throw new IllegalArgumentException("generateClassifier: role cannot be null")

        Random rand = new Random()
        int num

        if (ghRoot) {
            num = 1
        } else {
            if (role.mult.lower == role.mult.upper) {
                num = (Integer) (role.mult.upper < ctx.maxBreadth ? role.mult.upper : ctx.maxBreadth)

            } else if (role.mult.upper == -1) {
                num = rand.nextInt(ctx.maxBreadth) + role.mult.lower
            } else {
                if (role.mult.upper < ctx.maxBreadth) {
                    num = rand.nextInt(role.mult.upper) + role.mult.lower
                } else {
                    num = rand.nextInt(ctx.maxBreadth) + role.mult.lower
                }
            }
        }

//        boolean makeInterface = rand.nextBoolean()
        boolean makeInterface = false
        Role newRole = role
//        if (ghRoot)
//            newRole = copyToInterface((Classifier) role)

        boolean isRoot = isGHRoot(role)

        if (!(ghRoot || isRoot)) {
            num.times {
                if (newRole)
                    ctx.clsBuilder.init(ns: ns, classifier: newRole)
                else
                    ctx.clsBuilder.init(ns: ns, classifier: role)
                Type clazz = (Type) ctx.clsBuilder.create()
                if (clazz != null) {
                    if (map[role])
                        map[role] << clazz
                    else
                        map[role] = [clazz].toSet()
                }
            }
            return map[role]
        }

        return new HashSet<>()
    }

    private boolean isGHRoot(Role role) {
        SPS sps = params.rbml as SPS
        sps.genHierarchies.each {
            if (it instanceof GeneralizationHierarchy) {
                if ((it as GeneralizationHierarchy).root == role)
                    return true
            }
        }
        return false
    }

    ClassRole copyToInterface(Classifier role) {
        ClassRole ir = ClassRole.builder()
            .name(role.name)
            .mult(role.mult)
            .create()
        ir.structFeats = role.structFeats
        ir.behFeats = role.behFeats
        ir.abstrct = role.abstrct

        ir
    }

    void processGHRole(Namespace ns, Role role, String port) {
        if (!role)
            throw new IllegalArgumentException("processGHRole: role cannot be null")
        if (!port)
            throw new IllegalArgumentException("processGHRole: port cannot be empty or null")

        Classifier toGen
        boolean isRoot = false
        boolean nonterm = false

        if (role instanceof GeneralizationHierarchy) {
            String name = role.name
            if (port == name) // root
            {
                // TODO Continue with this
                toGen = (Classifier) ((GeneralizationHierarchy) role).root
                isRoot = true
            } else {
                Role child = role.children.find {
                    it.name == port
                }
                if (child instanceof ClassRole) { // leaf
                    toGen = child
                } else { // root
                    toGen = (Classifier) child
                    nonterm = true
                }
            }
            Set<Type> generated = generateClassifier(ns, toGen, isRoot)
            if (!ghmap[name]) {
                ghmap[name] = new HashMap<String, Set<Type>>()
                ghmap[name]["roots"] = [] as Set<Type>
                ghmap[name]["leaves"] = [] as Set<Type>
                ghmap[name]["nonterms"] = [] as Set<Type>
            }

            if (isRoot) {
                ghmap[name]["roots"] += generated
            } else if (nonterm) {
                ghmap[name]["nonterms"] += generated
            } else {
                ghmap[name]["leaves"] += generated
            }
        }
    }

    void selectAndCreateRelationship(Relationship rel) {
        if (!rel)
            throw new IllegalArgumentException("selectAndCreateRelationship: rel cannot be null")

        String srcName = ""
        String destName = ""
        int srcUpper = 0
        int destUpper = 0
        if (rel instanceof Association) {
            srcName = rel.sourceName
            destName = rel.destName
            srcUpper = rel.sourceMult.upper
            destUpper = rel.destMult.upper
        }

        switch (rel) {
            case Generalization:
                prepareAndCreateRelationship(rel.source(), rel.dest(), rel.srcPort, rel.destPort, RelationType.GENERALIZATION, srcName, destName, srcUpper, destUpper)
                break
            case Realization:
                prepareAndCreateRelationship(rel.source(), rel.dest(), rel.srcPort, rel.destPort, RelationType.REALIZATION, srcName, destName, srcUpper, destUpper)
                break
            case Usage:
                prepareAndCreateRelationship(rel.source(), rel.dest(), rel.srcPort, rel.destPort, RelationType.USE, srcName, destName, srcUpper, destUpper)
                break
            case Aggregation || Composition:
                prepareAndCreateRelationship(rel.source(), rel.dest(), rel.srcPort, rel.destPort, RelationType.COMPOSITION, srcName, destName, srcUpper, destUpper)
                break
            case Association:
                prepareAndCreateRelationship(rel.source(), rel.dest(), rel.srcPort, rel.destPort, RelationType.ASSOCIATION, srcName, destName, srcUpper, destUpper)
                break
        }
    }

    List<Role> findGenHierarchyComponents(GeneralizationHierarchy gh, String port) {
        if (!gh)
            throw new IllegalArgumentException("findGenHierarchyComponents: gh cannot be null")
        if (!port)
            throw new IllegalArgumentException("findGenHierarchyComponents: port cannot be null or empty")

        List<Role> roles = []
        if (gh.name == port) {
            roles << gh.root
        } else {
            def role = gh.children.find {
                it.name == port
            }
            if (role)
                roles << role
        }

        roles
    }

    void prepareAndCreateRelationship(Role src, Role dest, String srcPort, String destPort, RelationType type, String srcName = "", String destName = "", int srcUpper = 0, int destUpper = 0) {
        if (!src)
            throw new IllegalArgumentException("prepareAndCreateRelationship: src cannot be null")
        if (!dest)
            throw new IllegalArgumentException("prepareAndCreateRelationship: dest cannot be null")
        if (src instanceof GeneralizationHierarchy && !srcPort)
            throw new IllegalArgumentException("prepareAndCreateRelationship: if src is instance of GeneralizationHierarchy then srcPort cannot be null or empty")
        if (dest instanceof GeneralizationHierarchy && !destPort)
            throw new IllegalArgumentException("prepareAndCreateRelationship: if dest is instance of GeneralizationHierarchy then destPort cannot be null or empty")
        if (!type)
            throw new IllegalArgumentException("prepareAndCreateRelationship: type cannot be null")

        List<Type> sources = []
        List<Type> dests = []
        if (src instanceof GeneralizationHierarchy) {
            findGenHierarchyComponents((GeneralizationHierarchy) src, srcPort).each {
                if (map[it])
                    sources += map[it]
            }
        } else {
            if (map[src])
                sources = map[src].asList()
        }
        if (dest instanceof GeneralizationHierarchy) {
            findGenHierarchyComponents((GeneralizationHierarchy) dest, destPort).each {
                if (map[it])
                    dests += map[it]
            }
        } else {
            if (map[dest])
                dests = map[dest].asList()
        }

        if (sources.size() == dests.size()) {
            for (int i = 0; i < sources.size(); i++) {
                createRelationship(sources[i], dests[i], type, srcName, destName, srcUpper, destUpper)
            }
        } else if (sources.size() == 1 && dests.size() > 1 && sources.first()) {
            dests.each {
                createRelationship(sources.first(), it, type, srcName, destName, srcUpper, destUpper)
            }
        } else if (sources.size() > 1 && dests.size() == 1 && dests.first()) {
            sources.each {
                createRelationship(it, dests[0], type, srcName, destName, srcUpper, destUpper)
            }
        } else if (sources.size() >= 1 && dests.size() >= 1) {
            if (sources.size() < dests.size()) {
                for (int i = 1; i <= dests.size(); i++) {
                    createRelationship(sources[(sources.size() % i) - 1], dests[i - 1], type, srcName, destName, srcUpper, destUpper)
                }
            } else {
                for (int i = 1; i <= sources.size(); i++) {
                    createRelationship(sources[i - 1], dests[(dests.size() % i) - 1], type, srcName, destName, srcUpper, destUpper)
                }
            }
        }

    }

    void createRelationship(Type src, Type dest, RelationType type, String srcName = "", String destName = "", int srcUpper = 0, int destUpper = 0) {
        if (!src)
            throw new IllegalArgumentException("createRelationship: src cannot be null")
        if (!dest)
            throw new IllegalArgumentException("createRelationship: dest cannot be null")
        if (!type)
            throw new IllegalArgumentException("createRelationship: type cannot be null")

        switch (type) {
            case RelationType.GENERALIZATION:
                src.generalizedBy(dest)
                break
            case RelationType.COMPOSITION:
                src.composedTo(dest)
                createFields(src, dest, srcName, destName, srcUpper, destUpper)
                break
            case RelationType.ASSOCIATION:
                src.associatedTo(dest)
                createFields(src, dest, srcName, destName, srcUpper, destUpper)
                break
            case RelationType.REALIZATION:
                src.realizes(dest)
                break
            case RelationType.USE:
                src.useTo(dest)
                break
        }
    }

    private void createFields(Type src, Type dest, String srcName, String destName, int srcUpper, int destUpper) {
        Cue cue = CueManager.instance.getCurrent()
//        if (!(cue?.hasCueForRole(destName, src))) {
            if (!src.hasFieldWithName(destName)) {
                Accessibility access = Accessibility.PRIVATE
                if (src.isAbstract())
                    access = Accessibility.PROTECTED

                TypeRef destRef = createTypeRef(dest)
                Field srcField = Field.builder()
                        .name(destName)
                        .accessibility(access)
                        .compKey(src.getCompKey() + "." + destName)
                        .type(destRef)
                        .create()
                srcField.save()
                src.addMember(srcField)
                srcField.updateKey()
                ctx.rbmlManager.addMapping(destName, srcField)
            }
//        }

        if (srcUpper == -1 && destUpper == -1) {
//            if (!(cue?.hasCueForRole(srcName, dest))) {
                if (!dest.hasFieldWithName(srcName)) {
                    TypeRef srcRef = createTypeRef(src)
                    Accessibility access = Accessibility.PRIVATE
                    if (dest.isAbstract())
                        access = Accessibility.PROTECTED

                    Field destField = Field.builder()
                            .name(srcName)
                            .accessibility(access)
                            .compKey(dest.getCompKey() + "." + srcName)
                            .type(srcRef)
                            .create()
                    destField.save()
                    dest.addMember(destField)
                    destField.updateKey()
                    ctx.rbmlManager.addMapping(srcName, destField)
                }
//            }
        }
    }

    @Override
    TypeRef createDefaultTypeRef() {
        return null
    }
}
