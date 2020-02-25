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
package edu.montana.gsoc.msusel.pattern.gen

import org.javalite.activejdbc.Base

import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class DBManager {

    GeneratorContext context

    List<String> tables = [
            'classes', 'classes_modifiers', 'constructors',
            'constructors_method_exceptions', 'constructors_modifiers',
            'destructors', 'destructors_method_exceptions',
            'destructors_modifiers', 'enums', 'enums_modifiers',
            'fields', 'fields_modifiers', 'files', 'findings',
            'imports', 'initializers', 'initializers_modifiers',
            'interfaces', 'interfaces_modifiers',
            'languages', 'literals', 'literals_modifiers',
            'measures', 'method_exceptions', 'methods',
            'methods_method_exceptions', 'methods_modifiers',
            'metric_repositories', 'metrics', 'modifiers',
            'modules', 'namespaces', 'parameters', 'parameters_modifiers',
            'pattern_chains', 'pattern_instances', 'pattern_repositories',
            'patterns', 'projects', 'projects_languages', 'refs',
            'relations', 'role_bindings', 'roles', 'rule_repositories',
            'rules', 'rules_tags', 'scms', 'systems', 'tags',
            'type_refs', 'unknown_types'
    ]

    def open() {
        Base.open(context.db.driver, context.db.url, context.db.user, context.db.pass)
    }

    def close() {
        Base.close()
    }

    void createDatabase() {
        resetDatabase()
    }

    void resetDatabase() {
        try {
            Connection conn = DriverManager.getConnection(context.db.url, context.db.user, context.db.pass)
            if (conn != null) {
                tables.each {
                    try {
                        Statement stmt = conn.createStatement()
                        stmt.execute("drop table $it;")
                        stmt.closeOnCompletion()
                    } catch (Exception e) {

                    }
                }

                def text = DBManager.class.getResourceAsStream("/edu/montana/gsoc/msusel/pattern/gen/reset.sql").text
                // be sure to not have line starting with "--" or "/*" or any other non aplhabetical character

                // here is our splitter ! We use ";" as a delimiter for each request
                // then we are sure to have well formed statements
                String[] inst = text.split(";")

                for (int i = 0; i < inst.length; i++) {
                    // we ensure that there is no spaces before or after the request string
                    // in order to not execute empty statements
                    if (inst[i].trim() != "") {
                        Statement stmt = conn.createStatement()
                        stmt.execute(inst[i])
                        System.out.println(">>" + inst[i])
                        stmt.closeOnCompletion()
                    }
                }
                conn.close()
            }
        }
        catch (Exception e) {
            System.out.println("*** Error : " + e.toString())
            System.out.println("*** ")
            System.out.println("*** Error : ")
            e.printStackTrace()
            System.out.println("################################################")
            System.out.println(sb.toString())
        }

    }
}
