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
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class ConfigLoader {

    void loadConfiguration(File config, File base) {
        if (!config.exists()) {
            System.err << "Config file ${config.name} does not exist\n"
            System.exit 1
        }

        ConfigSlurper cs = new ConfigSlurper()
        def confObj = cs.parse(config.text)
        buildContext(confObj, base)
    }

    void buildContext(ConfigObject config, File base) {
        if (!config)
            throw new IllegalArgumentException("buildContext: config cannot be null")
        if (!base)
            throw new IllegalArgumentException("buildContext: base cannot be null")

        GeneratorContext context = GeneratorContext.getInstance()
        context.base         = base
        context.output       = config.output ?: "."
        context.loader       = PatternManager.getInstance()
        context.patterns     = config.patterns     ?: []
        context.numInstances = config.numInstances ?: 1
        context.maxBreadth   = config.maxBreadth   ?: 3
        context.maxDepth     = config.maxDepth     ?: 3
        context.version      = config.version      ?: "1.0.0"
        context.license      = config.license      ?: "MIT"
        context.db           = config.db           ?: [:]
        context.build        = config.build        ?: [:]
        context.arities      = config.arities      ?: [3]
        context.srcPath      = config.srcPath      ?: "."
        context.testPath     = config.testPath     ?: "."
        context.binPath      = config.binPath      ?: "."
    }
}
