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

import edu.isu.isuese.datamodel.Component
import edu.isu.isuese.datamodel.Field
import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.rbml.model.BehavioralFeature
import edu.montana.gsoc.msusel.rbml.model.ClassRole
import edu.montana.gsoc.msusel.rbml.model.Classifier
import edu.montana.gsoc.msusel.rbml.model.InterfaceRole
import edu.montana.gsoc.msusel.rbml.model.Role
import edu.montana.gsoc.msusel.rbml.model.StructuralFeature

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class RBML2DataModelManager {

    Map<Role, List<Type>> typeMapping = [:]
    Map<Type, Role> roleMapping = [:]
    Map<Method, Role> roleMethodMapping = [:]
    Map<Field, Role> roleFieldMapping = [:]
    Map<String, Set<String>> methodNames = [:]
    Map<String, Set<String>> fieldNames = [:]

    Type getType(Role role) {
        if (!role)
            return null

        Type type = null
        if (typeMapping[role])
            type = typeMapping[role].get(0)

        type
    }

    List<Type> getTypes(Role role) {
        if (!role)
            throw new IllegalArgumentException("role must not be null")

        List<Type> types = typeMapping[role] ?: []
        types.asImmutable()
    }

    Role getRole(Type type) {
        if (!type)
            throw new IllegalArgumentException("type must not be null")

        roleMapping[type] ?: null
    }

    Role getRole(Method method) {
        if (!method)
            throw new IllegalArgumentException("getRole: method must not be null")

        roleMethodMapping[method] ?: null
    }

    Role getRole(Field field) {
        if (!field)
            throw new IllegalArgumentException("getRole: field must not be null")

        roleFieldMapping[field] ?: null
    }

    def addMapping(Role role, Type type) {
        if (!role || !type)
            return
        if (typeMapping[role]) {
            if (!typeMapping[role].contains(type))
                typeMapping[role] << type
        }
        else {
            typeMapping[role] = [type]
        }
        if (roleMapping[type]) {
            Role r = roleMapping[type]
            typeMapping[r].remove(type)
        }
        roleMapping[type] = role
    }

    def addMapping(Role role, Method method) {
        if (!role || !method)
            return

        roleMethodMapping[method] = role
    }

    def addMapping(Role role, Field field) {
        if (!role || !field)
            return

        roleFieldMapping[field] = role
    }

    def removeMapping(Role role, Type type) {
        if (!role || !type)
            return
        if (typeMapping[role] && typeMapping[role].contains(type))
            typeMapping[role].remove(type)
        if (roleMapping[type] && roleMapping[type] == role)
            roleMapping.remove(type)
    }

    def remove(Type type) {
        if (!type)
            return

        Role role
        if (roleMapping[type]) {
            role = roleMapping[type]
            roleMapping.remove(type)
        }

        if (role)
            typeMapping[role].remove(type)
    }

    def remove(Role role) {
        if (!role)
            return

        List<Type> types
        if (typeMapping[role]) {
            types = typeMapping[role]
            typeMapping.remove(role)
        }

        if (types) {
            types.each {
                roleMapping.remove(it)
            }
        }
    }

    Set<Role> getRoles() {
        Set<Role> roles = typeMapping.keySet()
        roles.asImmutable()
    }

    Set<Role> getFieldRoles() {
        Set<Role> roles = roleFieldMapping.values().toSet()
        roles.asImmutable()
    }

    Set<Field> getFields() {
        roleFieldMapping.keySet().asImmutable()
    }

    Set<Role> getMethodRoles() {
        Set<Role> roles = roleMethodMapping.values().toSet()
        roles.asImmutable()
    }

    Set<Method> getMethods() {
        roleMethodMapping.keySet().asImmutable()
    }

    Role findRoleByName(String name) {
        Role r = roleMapping.values().find {it.name == name }
        if (!r) r = roleFieldMapping.values().find { it.name == name }
        if (!r) r = roleMethodMapping.values().find {it.name == name + "()"}
        r
    }

    def getComponentsByRole(Role role) {
        if (role instanceof Classifier || role instanceof ClassRole || role instanceof InterfaceRole)
            typeMapping[role]
        else if (role instanceof StructuralFeature)
            roleFieldMapping.keySet().findAll {roleFieldMapping[it] == role }
        else if (role instanceof BehavioralFeature)
            roleMethodMapping.keySet().findAll {roleMethodMapping[it] == role }
        else return [] as List<Component>
    }
}
