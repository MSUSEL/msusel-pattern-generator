/**
 * MIT License
 *
 * MSUSEL Design Pattern Generator
 * Copyright (c) 2017-2018 Montana State University, Gianforte School of Computing
 * Software Engineering Laboratory
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

import com.google.common.collect.Lists
import com.google.common.collect.Queues
import com.google.common.graph.GraphBuilder
import com.google.common.graph.MutableGraph
import edu.montana.gsoc.msusel.datamodel.AbstractTypeRef
import edu.montana.gsoc.msusel.datamodel.Accessibility
import edu.montana.gsoc.msusel.datamodel.DataModelMediator
import edu.montana.gsoc.msusel.datamodel.Modifier
import edu.montana.gsoc.msusel.datamodel.member.Constructor
import edu.montana.gsoc.msusel.datamodel.member.Field
import edu.montana.gsoc.msusel.datamodel.member.Method
import edu.montana.gsoc.msusel.datamodel.member.Parameter
import edu.montana.gsoc.msusel.datamodel.structural.File
import edu.montana.gsoc.msusel.datamodel.structural.Namespace
import edu.montana.gsoc.msusel.datamodel.type.Class
import edu.montana.gsoc.msusel.datamodel.type.Interface
import edu.montana.gsoc.msusel.datamodel.type.Type
import edu.montana.gsoc.msusel.datamodel.typeref.PrimitiveTypeRef
import edu.montana.gsoc.msusel.datamodel.typeref.TypeRef
import edu.montana.gsoc.msusel.rbml.PatternManager
import edu.montana.gsoc.msusel.rbml.model.*
import org.apache.commons.lang3.tuple.Pair
import org.apache.commons.math3.distribution.TriangularDistribution

import java.security.SecureRandom

/**
 * Abstract Base class used to Generate Pattern Instances
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
abstract class AbstractPatternGenerator {

    /**
     * The Code Tree containing the date used to generate a pattern instance project with
     */
    DataModelMediator tree
    /**
     * The extension for code files generated
     */
    def extension
    /**
     * The number of pattern instances to be generated
     */
    int numInstances
    /**
     * The maximum breadth of any hierarchy in a generated pattern instance
     */
    int maxBreadth
    /**
     * The maximum depth of any hierarchy in a generated pattern instance
     */
    int maxDepth
    /**
     * The srcML builder associated with this pattern generator
     */
    AbstractSourceBuilder builder
    /**
     * The string representation of the output path where the generated project files will be stored
     */
    String output = "."
    /**
     * The string representation of the language pattern instances are to be generated in
     */
    String lang
    /**
     * The graph representing the pattern instance
     */
    MutableGraph<Type> graph

    /**
     * List of available names for use in generating classes
     */
    def classNames = []
    /**
     * List of available names for use in generating fields
     */
    def fieldNames = []
    /**
     * List of available names for use in generating methods
     */
    def methodNames = []
    /**
     * List of available names for use in generating namespaces
     */
    def nsNames = []
    /**
     * The random number generator used during the generation process
     */
    SecureRandom randGen

    /**
     * map of TypeNodes indexed by the Role they fill in the pattern instance
     */
    def classes = [:]
    /**
     * map of CodeNodes indexed by the FeatureRole they fill in the pattern instance
     */
    def features = [:]

    /**
     * Method to generate project artifacts
     */
    void generate() {
        builder.pg = this
        readClassFileNames()
        readFieldNames()
        readMethodNames()
        readNamespaceNames()
        randGen = new SecureRandom()
        //mediator = new DefaultDataModelMediator()

        (1..numInstances).each { num ->
            PatternManager.instance.patterns.each { specification ->
                //mediator = new DefaultDataModelMediator()
                classes = [:]
                features = [:]
                //mediator.setSystem(Project.builder().key("${specification.getName()}${lang}${num}").create())
                graph = GraphBuilder.directed().allowsSelfLoops(false).build()
                def pkg = createPackage(tree.getProject())
                builder.setPattern(specification.getName())

                specification.getSps().getClassifiers().each { role ->
                    if (role instanceof ClassRole) {
                        genClass(role, pkg)
                    } else if (role instanceof InterfaceRole) {
                        genInterface(role, pkg)
                    } else if (role instanceof GeneralizationHierarchy) {
                        genRandHierarchy(role, classes, pkg, features)
                    } else if (role instanceof Classifier) {
                        genClassifier(role, pkg)
                    }
                }

                specification.getSps().getRelations().each { relation ->
                    def dest
                    def src

                    if (relation instanceof Association) {
                        dest = relation.dest
                        src = relation.source
                    } else if (relation instanceof Aggregation) {
                        dest = relation.dest
                        src = relation.source
                    } else if (relation instanceof Composition) {
                        dest = relation.dest
                        src = relation.source
                    } else if (relation instanceof Generalization) {
                        dest = relation.parent
                        src = relation.child
                    } else if (relation instanceof Realization) {
                        dest = relation.parent
                        src = relation.child
                    } else if (relation instanceof Usage) {
                        dest = relation.dest
                        src = relation.source
                    } else if (relation instanceof AtLeastOne) {

                    }

                    println "Src: ${src} and Dest: ${dest} with relation: ${relation}"

                    if (src && dest) {
                        classes[src.getName()].each { csrc ->
                            classes[dest.getName()].each { cdest ->
                                createRelationship(relation, csrc, cdest)
                            }
                        }
                    }
                }
                //
                //            specification.getIps().getTraces().each { trace ->
                //                trace.getFragments().each { frag ->
                //                    genStatement(frag, classes)
                //                }
                //            }
                builder.construct(tree.getProject(), tree)
            }
        }
    }

    void genClassifier(role, pkg) {
        if (!classes[role.getName()])
            classes[role.getName()] = []
        def rand = nextRandom(role.mult.lower..role.mult.upper) + 1

        rand.times {
            Collections.shuffle(classNames)
            String name = classNames[0]
            if ((randGen.nextDouble() <=> 0.5) < 0)
                name += classNames[1]
            if ((randGen.nextDouble() <=> 0.15) < 0)
                name += classNames[2]
            name += role.getName()

            def file = createFile(output, pkg, name)

            def ioc = Math.random()
            def cls
            if (ioc < 0.5) {
                cls = createInterface(name, role, pkg, file, features)
            } else {
                cls = createClass(name, role, pkg, file, features)
            }
        }
    }

    void genInterface(role, pkg) {
        classes[role.getName()] = []
        def rand = nextRandom(role.mult.lower..role.mult.upper)
        rand.times {
            Collections.shuffle(classNames)
            String name = classNames[0]
            if ((randGen.nextDouble() <=> 0.5) < 0)
                name += classNames[1]
            if ((randGen.nextDouble() <=> 0.15) < 0)
                name += classNames[2]
            name += role.getName()

            def file = createFile(output, pkg, name)

            def cls = createInterface(name, role, pkg, file, features)
        }
    }

    void genClass(role, pkg) {
        classes[role.getName()] = []
        def rand = nextRandom(role.mult.lower..role.mult.upper)
        rand.times {
            Collections.shuffle(classNames)
            String name = classNames[0]
            if ((randGen.nextDouble() <=> 0.5) < 0)
                name += classNames[1]
            if ((randGen.nextDouble() <=> 0.15) < 0)
                name += classNames[2]
            name += role.getName()

            def file = createFile(output, pkg, name)

            def cls = createClass(name, role, pkg, file, features)
        }
    }

    /**
     * Writes the SrcML xml file
     * @param xml XML Content to be writen to the file with the given filename
     * @param filename String representation of the filename that is to be written
     */
    void writeOutput(xml, filename) {
        def file = new File("testdata/${filename}")
        file.withWriter('UTF-8') { writer ->
            writer.write(xml)
        }
    }

    /**
     * @param fragment
     * @param classes
     * @return
     */
    def genStatement(fragment, classes) {
    }

    /**
     * Creates the specified type of relationship between the source and dest nodes in the DataModelMediator
     * @param relation Relation type
     * @param source Source Node
     * @param dest Dest Node
     */
    void createRelationship(relation, source, dest) {
        if (relation instanceof Generalization) {
            tree.addGeneralizes(source, dest)
        } else if (relation instanceof Realization) {
            tree.addRealizes(source, dest)
        } else if (relation instanceof Association) {
            tree.addAssociation(source, dest, false)
        } else if (relation instanceof Usage) {
            tree.addUse(source, dest)
        }
        println "Created Relation from: ${source.name()} to: ${dest.name()} of type: ${relation}"
    }

    /**
     * Generates a randomly grown Generalization Hierarchy within the Code Tree
     * @param role GeneralizationHierarchy corresponding to the hierarchy being grown
     * @param classes Map of Types indexed by their associated Role
     * @param pkg Package containing the hierarchy
     * @param features Map of Features indexed by their associated Role
     */
    void genRandHierarchy(GeneralizationHierarchy role, classes, pkg, features) {

        println "Generating Random Generalization Hierarchy"
        println role

        Queue<Pair> que = Queues.newArrayDeque()
        Type parent = null

        println "Root: ${role.getRoot()}"

        //if (role.getRoot() instanceof Classifier) {
        classes[role.getRoot().getName()] = []
        if (nextRandomBool()) {
            parent = createClass(role.getRoot(), pkg, classes, features)
        } else {
            parent = createClass(role.getRoot(), pkg, classes, features)
        }
        //}

        que.offer(Pair.of(parent, 1))
        int depth = 1

        while (!que.isEmpty() && depth <= maxDepth) {
            Pair t = que.poll()
            parent = t.getLeft()
            depth = t.getRight()

            int rand = nextRandom(1..maxBreadth) + 1
            int num = rand / role.getChildren().size()

            role.getChildren().each { c ->
                rand.times {
                    classes[c.getName()] = []
                    if (c instanceof Classifier) {
                        def cls = createClass(c, pkg, classes, features)
                        que.offer(Pair.of(cls, depth + 1))

                        if (parent.isInterface())
                            createRelationship(new Realization(), cls, parent)
                        else
                            createRelationship(new Generalization(), cls, parent)
                        graph.putEdge((Type) cls, parent)
                    } else {
                        def cls = createClass(c, pkg, classes, features)

                        if (parent.isInterface())
                            createRelationship(new Realization(), cls, parent)
                        else
                            createRelationship(new Generalization(), cls, parent)

                        graph.putEdge((Type) cls, parent)
                    }
                }
            }
        }
    }

    /**
     * TODO Refactor
     * Creates a new class
     * @param role Role to which this class will be associated
     * @param pkg Name of the package the class to be created will belong to
     * @param classes Map of Types indexed by their associated Role
     * @param features Map of Features indexed by their associated Role
     * @return The newly created TypeRole
     */
    def createClass(role, pkg, classes, features) {
        println "Creating Class for Role ${role.name}"
        def cls
        if (role instanceof ClassRole) {
            if (!classes[role.getName()])
                classes[role.getName()] = []

            Collections.shuffle(classNames)
            String name = classNames[0]
            if ((randGen.nextDouble() <=> 0.5) < 0)
                name += classNames[1]
            if ((randGen.nextDouble() <=> 0.15) < 0)
                name += classNames[2]
            name += role.getName()

            def file = createFile(output, pkg, name)

            cls = createClass(name, role, pkg, file, features)
        } else if (role instanceof InterfaceRole) {
            if (!classes[role.getName()])
                classes[role.getName()] = []

            Collections.shuffle(classNames)
            String name = classNames[0]
            if ((randGen.nextDouble() <=> 0.5) < 0)
                name += classNames[1]
            if ((randGen.nextDouble() <=> 0.15) < 0)
                name += classNames[2]
            name += role.getName()

            def file = createFile(output, pkg, name)

            cls = createInterface(name, role, pkg, file)
        } else if (role instanceof Classifier) {
            if (!classes[role.getName()])
                classes[role.getName()] = []

            Collections.shuffle(classNames)
            String name = classNames[0]
            if ((randGen.nextDouble() <=> 0.5) < 0)
                name += classNames[1]
            if ((randGen.nextDouble() <=> 0.15) < 0)
                name += classNames[2]
            name += role.getName()

            def file = createFile(output, pkg, name)

            def ioc = Math.random()
            if (ioc < 0.5) {
                cls = createInterface(name, role, pkg, file, features)
            } else {
                cls = createClass(name, role, pkg, file, features)
            }
        }

        cls
    }

    /**
     * Creates a Field
     * @param role Role to which the field will be associated with
     * @param owner Type owning the field to be created
     * @param features Map of Features indexed by their associated Role
     * @return The recently created Field
     */
    def createField(role, owner, features) {
        def fieldName = role.getName()

        def field = Field.builder().key(fieldName).accessibility(Accessibility.PRIVATE).type(findAppropriateType(owner, role.getType().getName())).create()
        if (!features[role.getName()])
            features[role.getName()] = []

        // handle specifiers
        role.props.each { prop ->
            field.modifiers << Modifier.valueForJava(prop)
        }

        println "Field modifiers: ${field.modifiers}"

        features[role.getName()] << field
        owner.children << field

        field
    }

    /**
     * Constructs a field identifier for the field with the given name which
     * will be attached to the given type
     *
     * @param type
     *            Type to include the field
     * @param name
     *            unqualified field name
     * @return Fully qualified field name
     */
    protected String createFieldIdentifier(Type type, String name) {
        "${type.getQIdentifier()}#${name}"
    }

    /**
     * Creates a new Method from a given Role
     * @param role The Role to which this method will be associated
     * @param owner Type which will contain this method
     * @param features Map of Features indexed by their associated Role
     * @return The newly created Method
     */
    def createMethod(role, Type owner, features) {
        println "Creating Method for role ${role.name}"
        println "\towner: ${owner}"

        def methodName = role.getName()

        int numMethods = nextRandom(role.getMult().getLower()..role.getMult().getUpper())
        for (int l = 0; l < numMethods; l++) {
            Collections.shuffle(methodNames)
            String name = methodName

            if (l >= 1)
                name = methodNames + ((CharSequence) methodName).capitalize()
            boolean constructor = false

            String qId = createMethodIdentifier(owner, name)

            Method mn
            if (constructor)
                mn = Constructor.builder().key(qId).create()
            else {
                println "\trole.getType().getName(): ${role.getType().getName()}"
                mn = Method.builder().key(qId).accessibility(Accessibility.PUBLIC).type(findAppropriateType(owner, role.getType().getName())).create()
            }
            println mn.type

            // handle specifiers
            println "Props: ${role.props}"
            role.props.each { String prop ->
                mn.modifiers << Modifier.valueForJava(prop)
            }

            role.params.each { param ->
                mn.children << Parameter.builder().key(param.getName()).type(findAppropriateType(owner, param.getType().getName())).create()
            }

            owner.children << mn
            if (!features[role.getName()])
                features[role.getName()] = []
            features[role.getName()] << mn

            println "\t${mn}"
        }
    }

    /**
     * Generates a fully qualified method identifier for a method with the given
     * name which will be included in the given Type
     *
     * @param type
     *            Type to include a method with the given name
     * @param name
     *            Unqualified name of the method
     * @return Fully qualified name of the method
     */
    protected String createMethodIdentifier(Type type, String name) {
        "${type.getKey()}#${name}"
    }

    /**
     * Creates a class with the given name, associated to the provided role, member of the given package, contained in the given file.
     * @param name Name of the class to be created
     * @param role Role the class is to be associated with
     * @param pkg Package the class is a member of
     * @param file Name of the file containing the class
     * @param features Map of Features indexed by their associated Role
     * @return The created class
     */
    def createClass(name, role, pkg, File file, features) {
        String identifier = name
        String qIdentifier = createTypeIdentifier(pkg.getKey(), identifier)

        Type clazz = null
        if (role.isAbstrct()) {
            clazz = Class.builder()
                    .key(identifier)
                    .accessibility(Accessibility.PUBLIC)
                    .specifiers([Modifier.ABSTRACT])
                    .create()
        } else {
            clazz = Class.builder()
                    .key(identifier)
                    .accessibility(Accessibility.PUBLIC)
                    .create()
        }

        classes[role.getName()] << clazz
        graph.addNode(clazz)

        role.behFeats.each { feat ->
            createMethod(feat, clazz, features)
        }

        role.structFeats.each { feat ->
            createField(feat, clazz, features)
        }

        file.children << clazz
        println "Created Class: ${name} for role: ${role.getName()}"
        clazz
    }

    /**
     * @param name
     * @param role
     * @param pkg
     * @param file
     * @param features
     * @return
     */
    def createInterface(name, role, pkg, File file, features) {
        String identifier = name
        String qIdentifier = createTypeIdentifier(pkg.getKey(), identifier)

        Type clazz = Interface.builder().key(qIdentifier).accessibility(Accessibility.PUBLIC).create()

        classes[role.getName()] << clazz
        graph.addNode(clazz)

        role.behFeats.each { feat ->
            createMethod(feat, clazz, features)
        }

        role.structFeats.each { feat ->
            createField(feat, clazz, features)
        }

        file.children << clazz
        println "Created Interface: ${name} for role: ${role.getName()}"
        clazz
    }

    /**
     * Construct a class identifier for with the given base namespace and
     * identifier
     *
     * @param namespace
     *            The base namespace
     * @param identifier
     *            The type name
     * @return Fully qualified type name
     */
    protected String createTypeIdentifier(namespace, identifier) {
        "${namespace}.${identifier}"
    }

    /**
     * @param role
     * @return
     */
    def createNamespace(role) {
        TriangularDistribution dist = new TriangularDistribution(5, 10, 15)
        int numPkgs = (int) dist.sample()

        String base = "com.sparqline"

        List<String> pn = Lists.newArrayList()

        TriangularDistribution subdist = new TriangularDistribution(2, 3, 5)
        List<String> pkgs = Lists.newArrayList()
        Queue<String> pq = new LinkedList<>()
        int count = 0
        pq.add(base)
        while (count < numPkgs && !pq.isEmpty()) {
            String pkg = pq.poll()
            int numsubs = (int) subdist.sample()
            Collections.shuffle(pn)
            for (int i = 0; i <= numsubs; i++) {
                String temp = pkg + "." + pn.get(i)
                pq.offer(temp)
                pkgs.add(temp)
            }
            count++
        }

        pkgs
    }

    /**
     * @param output
     * @param pkg
     * @param name
     * @return
     */
    def createFile(output, pkg, name) {
        String fullPath = "${output}/${pkg.getKey().replaceAll('\\.', '/')}/${name}.${extension}"

        def file = File.builder().key(fullPath).create()
        file.setNamespace(pkg)
        tree.getProject().children << file
        file
    }

    /**
     * @param proj
     * @return
     */
    def createPackage(proj) {
        Collections.shuffle(nsNames)
        String name = "dpgen.${proj.getKey()}.${nsNames[0]}"
        if ((0.5 <=> randGen.nextDouble()) < 0)
            name += ".${nsNames[1]}"
        if ((0.15 <=> randGen.nextDouble()) < 0)
            name += ".${nsNames[2]}"

        def ns = Namespace.builder().key(name).create()
        tree.getProject().children << ns
        ns
    }

    /**
     * @param range
     * @return
     */
    def nextRandom(range) {
        int to = range.getTo()
        int from = range.getFrom()
        if (to < 0)
            to = 10
        if (from < 0)
            from = 10
        if (to < from) {
            def temp = to
            to = from
            from = temp
        }

        randGen.nextInt(to - from + 1) + from
    }

    /**
     * @return
     */
    def nextRandomBool() {
        randGen.nextBoolean()
    }

    /**
     * @return
     */
    def readClassFileNames() {
        classNames = []
        def isr = new InputStreamReader(AbstractPatternGenerator.class.getResourceAsStream("classnames1.txt"))
        classNames += isr.readLines()
        isr.close()

        isr = new InputStreamReader(AbstractPatternGenerator.class.getResourceAsStream("classnames2.txt"))
        classNames += isr.readLines()
        isr.close()

        isr = new InputStreamReader(AbstractPatternGenerator.class.getResourceAsStream("classnames3.txt"))
        classNames += isr.readLines()
        isr.close()
    }

    /**
     * @return
     */
    def readFieldNames() {
        fieldNames = []
        def isr = new InputStreamReader(AbstractPatternGenerator.class.getResourceAsStream("fieldnames.txt"))
        fieldNames += isr.readLines()
        isr.close()
    }

    /**
     * @return
     */
    def readMethodNames() {
        methodNames = []
        def isr = new InputStreamReader(AbstractPatternGenerator.class.getResourceAsStream("methodnames1.txt"))
        methodNames += isr.readLines()
        isr.close()

        isr = new InputStreamReader(AbstractPatternGenerator.class.getResourceAsStream("methodnames2.txt"))
        methodNames += isr.readLines()
        isr.close()
    }

    /**
     * @return
     */
    def readNamespaceNames() {
        nsNames = []
        def isr = new InputStreamReader(AbstractPatternGenerator.class.getResourceAsStream("regionnames1.txt"))
        nsNames += isr.readLines()
        isr.close()

        isr = new InputStreamReader(AbstractPatternGenerator.class.getResourceAsStream("regionnames2.txt"))
        nsNames += isr.readLines()
        isr.close()

        isr = new InputStreamReader(AbstractPatternGenerator.class.getResourceAsStream("regionnames3.txt"))
        nsNames += isr.readLines()
        isr.close()
    }

    /**
     * @param type
     * @return
     */
    def findTypeRole(type) {
        def entry = classes.find { key, values -> values.contains(type) }
        if (entry)
            entry.key
        else
            null
    }

    /**
     * @param feat
     * @return
     */
    def findFeatureRole(feat) {
        def entry = features.find { key, values -> values.contains(feat) }
        if (entry)
            entry.key
        else
            null
    }

    /**
     * @param owner
     * @param role
     * @return
     */
    AbstractTypeRef findAppropriateType(Type owner, String role) {
        def matches = findMatchingClassifiers(role)
        println "matching: ${role}"

        if (checkType(owner, matches))
            return TypeRef.builder().type(owner.getKey()).typeName(owner.name()).create()

        def match
        if ((match = checkInheritance(owner, matches)))
            return TypeRef.builder().type(match.getKey()).typeName(match.name()).create()

        def conn = []
        conn.addAll(associationsFrom(owner))
        conn.addAll(usagesFrom(owner))

        for (Type c : conn) {
            if (checkType(c, matches))
                return TypeRef.builder().type(c.getKey()).typeName(c.name()).create()

            if ((match = checkInheritance(c, matches)))
                return TypeRef.builder().type(match.getKey()).typeName(match.name()).create()
        }

        println("Couldn't find type for role ${role}")
        PrimitiveTypeRef.getInstance("void")
    }

    def findMatchingClassifiers(String name) {
        return classes[name]
    }

    def checkType(type, set) {
        (set.contains(type))
    }

    def checkInheritance(Type type, set) {
        def stack = inheritanceStack(type)
        while (!stack.isEmpty()) {
            def s = stack.pop()
            if (checkType(s, set))
                return s
        }
    }

    def associationsFrom(Type type) {
        (List<Type>) tree.getAssociatedFrom(type)
    }

    def usagesFrom(Type type) {
        (List<Type>) tree.getUseFrom(type)
    }

    def inheritanceStack(Type type) {
        def gen = tree.getGeneralizedFrom(type)
        def real = tree.getRealizedFrom(type)

        def stack = new Stack<Type>()
        def que = Queues.newArrayDeque()
        que.addAll(gen)
        que.addAll(real)

        while (!que.isEmpty()) {
            def t = que.poll()
            stack.push(t)
            que.addAll(tree.getGeneralizedFrom(t))
            que.addAll(tree.getRealizedFrom(t))
        }

        return stack
    }
}
