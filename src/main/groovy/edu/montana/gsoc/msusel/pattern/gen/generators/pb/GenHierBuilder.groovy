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

import edu.isu.isuese.datamodel.Class
import edu.isu.isuese.datamodel.Interface
import edu.isu.isuese.datamodel.Namespace
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.pattern.gen.generators.pb.tree.Node
import edu.montana.gsoc.msusel.pattern.gen.generators.pb.tree.Tree
import edu.montana.gsoc.msusel.rbml.model.ClassRole
import edu.montana.gsoc.msusel.rbml.model.Classifier
import edu.montana.gsoc.msusel.rbml.model.GeneralizationHierarchy
import edu.montana.gsoc.msusel.rbml.model.Role
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class GenHierBuilder extends AbstractBuilder {

    Map<String, Map<String, Set<Type>>> ghmap
    List<Type> terms = []
    List<Type> nonterms = []
    List<Type> roots = []
    Random rand = new Random()

    def create() {
        if (!params.gh)
            throw new IllegalArgumentException("createGenHierarchy: gh cannot be null")
        if (!params.ns)
            throw new IllegalArgumentException("createGenHierarchy: ns cannot be null")
        if (ghmap == null)
            throw new IllegalArgumentException("createGenHierarchy: ghmap cannot be empty or null")

        log.info( "Generating Hierarchy")

        GeneralizationHierarchy gh = (GeneralizationHierarchy) params.gh
        Namespace ns = (Namespace) params.ns

        updateLists(gh)
        ctx.treeGenerator.init(gh.root.name, getNonTermNames(gh), getTermNames(gh), ctx.arities, ctx.maxDepth, ctx.maxBreadth * ctx.maxDepth)
        Tree tree = ctx.treeGenerator.generate()
        if (!roots.isEmpty()) {
            tree.root.type = roots[0]
            roots.remove(0)
        }
        populateTree(gh, ns, tree)
    }

    private List<String> getNonTermNames(GeneralizationHierarchy gh) {
        gh.children.findAll {
            !(it instanceof ClassRole)
        }*.name
    }

    private List<String> getTermNames(GeneralizationHierarchy gh) {
        gh.children.findAll {
            (it instanceof ClassRole)
        }*.name
    }

    protected void updateLists(GeneralizationHierarchy gh) {
        if (ghmap) {
            if (ghmap[gh.name]) {
                roots = ghmap[gh.name]['roots'].toList()
                nonterms = ghmap[gh.name]['nonterms'].toList()
                terms = ghmap[gh.name]['leaves'].toList()
            } else {
                roots = []
                nonterms = []
                terms = []
            }
        } else {
            roots = []
            nonterms = []
            terms = []
        }

        if (!terms) terms = []
        if (!nonterms) nonterms = []
        if (!roots) roots = []
    }

    protected void populateTree(GeneralizationHierarchy gh, Namespace ns, Tree tree) {
        if (!gh)
            throw new IllegalArgumentException("populateTree: gh cannot be null")
        if (!ns)
            throw new IllegalArgumentException("populateTree: ns cannot be null")
        if (!tree)
            throw new IllegalArgumentException("populateTree: tree cannot be null")
        if (tree.isEmpty())
            return

        Queue<Node> q = new LinkedList<>()
        q.offer(tree.root)

        while (!q.isEmpty()) {
            Node n = q.poll()
            if (n.getValue()) {
                createClassifier(ns, getClassifier(gh, n.getValue()), n)
                if (n.parent && n.parent.getType()) {
                    generalizes(n.parent.getType(), n.getType())
                }

                q.addAll(n.children)
            }
        }
    }

    protected Classifier getClassifier(GeneralizationHierarchy gh, String name) {
        if (!gh)
            throw new IllegalArgumentException("getClassifier: gh cannot be null")
        if (!name)
            throw new IllegalArgumentException("getClassifier: name cannot be null or empty")

        Classifier found = (Classifier) gh.children.find {
            it.name == name
        }
        if (!found && gh.root.name == name)
            found = gh.root

        found
    }

    protected void createClassifier(Namespace ns, Classifier role, Node n) {
        if (!ns)
            throw new IllegalArgumentException("createClassifier: ns cannot be null")
        if (!role)
            throw new IllegalArgumentException("createClassifier: role cannot be null")
        if (!n)
            throw new IllegalArgumentException("createClassifier: n cannot be null")

        handleKnownTypes(n, role, ns)
    }

    private def handleKnownTypes(Node n, Role role, Namespace ns) {
        if (n.children.size == 1 && n.parent == null) {
            assignOrCreate(n, roots, role, ns)
        } else if (n.parent == null) {
            assignOrCreate(n, roots, role, ns)
        } else if (n.children) {
            assignOrCreate(n, nonterms, role, ns)
        } else {
            assignOrCreate(n, terms, role, ns)
        }
    }

    private void assignOrCreate(Node n, List<Type> types, Role role, Namespace ns) {
        if (n.type)
            return
        else if (!types.isEmpty()) {
            n.type = types.remove(0)
        } else {
            ctx.clsBuilder.init(classifier: role, ns: ns)
            n.type = ctx.clsBuilder.create()
        }
    }

    protected def generalizes(Type parent, Type child) {
        if (!parent)
            throw new IllegalArgumentException("generalizes: parent cannot be null")
        if (!child)
            throw new IllegalArgumentException("generalizes: child cannot be null")

        if (parent instanceof Interface) {
            if (child instanceof Interface)
                child.generalizedBy(parent)
            else
                child.realizes(parent)
        } else {
            if (child instanceof Class)
                child.generalizedBy(parent)
        }
    }
}
