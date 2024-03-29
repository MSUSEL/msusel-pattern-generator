/*
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
package edu.montana.gsoc.msusel.pattern.gen.methodnames

import com.google.common.collect.Sets
import edu.isu.isuese.datamodel.Type
import edu.montana.gsoc.msusel.pattern.gen.GeneratorContext
import edu.montana.gsoc.msusel.pattern.gen.generators.pb.RBML2DataModelManager
import edu.montana.gsoc.msusel.rbml.model.Role

class RoleBasedMethodNameGenerator extends MethodNameGenerator {

    RBML2DataModelManager manager
    String dependsOn
    String prefix

    RoleBasedMethodNameGenerator(GeneratorContext ctx, String dependsOn, String prefix) {
        super(ctx)
        this.dependsOn = dependsOn
        this.prefix = prefix
    }

    @Override
    Set<String> generate(Set<String> allNames) {
        Set<String> set = Sets.newHashSet()
        Role role = manager.findRoleByName(dependsOn)
        List<Type> types = manager.getTypes(role)

        int num = types.size()

        num.times {int index ->
            Type type = types.get(index)
            String s = prefix + type.getName()
            if (!allNames.contains(s)) {
                set << s
            }
        }
        return set
    }
}
