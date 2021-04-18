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
import edu.montana.gsoc.msusel.rbml.model.Classifier
import edu.montana.gsoc.msusel.rbml.model.GeneralizationHierarchy
import edu.montana.gsoc.msusel.rbml.model.Role
import edu.montana.gsoc.msusel.rbml.model.SPS
import groovy.util.logging.Log4j2
import org.apache.logging.log4j.Level

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class PatternBuilder extends AbstractBuilder {

    Map<String, Integer> patternCounts = [:]

    def create() {
        log.log(Level.INFO,"Generating pattern")
        if (!params.pattern)
            throw new IllegalArgumentException("create: pattern cannot be null or empty")
        if (!params.parent)
            throw new IllegalArgumentException("create: parent cannot be null")

        String pattern = params.pattern
        Namespace ns = (Namespace) params.parent
        List<Project> projects = ns.getParentProjects()
        Project parentProj = null
        if (!projects.isEmpty())
            parentProj = projects.first()

        SPS rbml = ctx.loader.loadPattern(pattern)
        if (rbml) {
            ctx.relationBuilder.init(ns: ns, rbml: rbml)
            ctx.relationBuilder.create()
        }

        ctx.rbmlManager.getRoles().each {
            if (it instanceof Classifier)
                ctx.clsBuilder.createFeatures(it)
        }

        createInstance(rbml, pattern, parentProj)
    }

    private PatternInstance createInstance(SPS rbml, String pattern, Project parentProj) {
        if (!parentProj)
            return null

        PatternInstance inst = PatternInstance.builder()
                .instKey("${parentProj.projectKey}:${pattern}-${getCount(pattern)}")
                .create()

        Pattern pat = (Pattern) Pattern.find("name = ?", pattern.capitalize()).first()
        if (pat) {
            updateRoles(pat, rbml)

            ctx.rbmlManager.getRoles().each { Role role ->
                String name = role.name
                def roles = pat.getRoles().find { it.name == name }
                if (roles) {
                    def types = ctx.rbmlManager.getTypes(role)

                    roles.each { edu.isu.isuese.datamodel.Role r ->
                        types.each { Type t ->
                            inst.addRoleBinding(createRoleBinding(r, t))
                        }
                    }
                }
            }

            ctx.rbmlManager.getFields().each { Field f ->
                Role role = ctx.rbmlManager.getRole(f)
                processComponent(f, role, pat, inst)
            }

            ctx.rbmlManager.getMethods().each { Method m ->
                Role role = ctx.rbmlManager.getRole(m)
                processComponent(m, role, pat, inst)
            }
        }

        inst
    }

    private int getCount(String pattern) {
        if (patternCounts[pattern])
            patternCounts[pattern] += 1
        else
            patternCounts[pattern] = 1

        patternCounts[pattern]
    }

    private void processComponent(Component comp, Role role, Pattern pat, PatternInstance inst) {
        String name = role.getName()
        def roles = pat.getRoles().find { it.name == name }
        if (roles) {
            roles.each { edu.isu.isuese.datamodel.Role r ->
                inst.addRoleBinding(createRoleBinding(r, comp))
            }
        }
    }

    private void updateRoles(Pattern pat, SPS rbml) {
        rbml.classifiers.each {
            if (it instanceof Classifier && !it.ghMember) {
                buildClassifierRole((Classifier) it, pat)
            }
        }

        rbml.genHierarchies.each {
            if (it instanceof GeneralizationHierarchy) {
                GeneralizationHierarchy gh = (GeneralizationHierarchy) it

//                buildClassifierRole(gh.root, pat)
//                gh.children.each { Role r ->
//                    buildClassifierRole((Classifier) r, pat)
//                }
            }
        }

        rbml.relations.each {
            if (!pat.getRoles().find { r -> r.name == it.name }) {
                edu.isu.isuese.datamodel.Role role = edu.isu.isuese.datamodel.Role.builder()
                        .name(it.name)
                        .roleKey("${pat.patternKey}:${it.name}")
                        .type(RoleType.RELATION)
                        .create()
                pat.addRole(role)
            }
        }
    }

    private void buildClassifierRole(Classifier c, Pattern pat) {

        if (!pat.getRoles().find { r -> r.name == c.name }) {
            edu.isu.isuese.datamodel.Role role = edu.isu.isuese.datamodel.Role.builder()
                    .name(c.name)
                    .roleKey("${pat.patternKey}:${c.name}")
                    .type(RoleType.CLASSIFIER)
                    .create()
            pat.addRole(role)

            c.structFeats.each {
                edu.isu.isuese.datamodel.Role struct = edu.isu.isuese.datamodel.Role.builder()
                        .name(c.name)
                        .roleKey("${pat.patternKey}:${it.name}")
                        .type(RoleType.STRUCT_FEAT)
                        .create()
                pat.addRole(struct)
            }

            c.behFeats.each {
                edu.isu.isuese.datamodel.Role behav = edu.isu.isuese.datamodel.Role.builder()
                        .name(c.name)
                        .roleKey("${pat.patternKey}:${it.name}")
                        .type(RoleType.BEHAVE_FEAT)
                        .create()
                pat.addRole(behav)
            }
        }
    }

    private RoleBinding createRoleBinding(edu.isu.isuese.datamodel.Role role, Component comp) {
        RoleBinding binding = new RoleBinding()
        Reference ref = createReference(comp)
        binding.setRoleRefPair(role, ref)

        binding
    }

    private def createReference(Component comp) {
        RefType type
        switch (comp) {
            case Method:
                type = RefType.METHOD
                break
            case Field:
                type = RefType.FIELD
                break
            default:
                type = RefType.TYPE
                break
        }

        Reference.builder()
                .refType(type)
                .refKey(comp.getRefKey())
                .create()
    }
}
