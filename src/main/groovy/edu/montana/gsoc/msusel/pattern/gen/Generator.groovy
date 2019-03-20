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

import edu.montana.gsoc.msusel.pattern.cue.CueScriptLoader
import edu.montana.gsoc.msusel.pattern.gen.plugin.AbstractLanguagePlugin
import edu.montana.gsoc.msusel.pattern.gen.plugin.LanguageDescriptor
import edu.montana.gsoc.msusel.pattern.gen.plugin.LanguagePlugin
import edu.montana.gsoc.msusel.rbml.RBMLScriptLoader
import org.reflections.Reflections

import java.util.jar.Attributes
import java.util.jar.JarFile
import java.util.jar.Manifest

import static groovy.io.FileType.FILES
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class Generator {

    static final String PLUGIN_PKG = "edu.montana.gsoc.msusel.pattern.gen"
    static def langMap = [:]

    /**
     * @param args
     */
    static main(String[] args) {
        def cli = new CliBuilder(usage:'pattern-gen [options]', footer:'Copyright (c) 2017-2018 Montana State University')
        cli.s(longOpt:'spec', args:1, argName:'spec-file', 'RBML Specification file for the pattern to generate')
        cli.p(longOpt:'pgcl', args:1, argName:'pgcl-file', 'PGCL Specification file for the pattern to generate')
        cli.c(longOpt:'config', args:1, argName:'config-file', 'Configuration file to use to guide the pattern generation')
        cli.l(longOpt:'lang',  args:1, argName:'language', "Language to create patterns in. Can be one of ${langMap.keySet()}")
        cli.b(longOpt:'base', args:1, argName:'directory', 'Path to use as root of projects created')
        cli.h(longOpt:'help', 'print this message')

        def options = cli.parse(args)

        if (args.length == 0 || options.h || options.help) {
            cli.usage()
        }

        loadBuiltInPlugins()
        GeneratorConfig conf = buildConfig(options)

        if (conf.plugin) {
            conf.plugin.initialize(conf.base)
            conf.plugin.execute(conf)
        }
    }

    static GeneratorConfig buildConfig(OptionAccessor options) {
        def config = GeneratorConfig.builder().create()
        if (options.b || options.base) {
            config.base = options.b
        }

        if (langMap[options.l]) {
            config.plugin = langMap[options.l]
        }

        if (options.s || options.spec) {
            config.rbml = RBMLScriptLoader().load(options.s)
        }

        if (options.p || options.pgcl) {
            config.cue = CueScriptLoader().load(options.p)
        }

        if (options.c || options.config) {
            config = new ConfigSlurper().parse(new File(options.c).toURL()) // TODO fix this
        }

        config
    }

    static void loadBuiltInPlugins() {
        Reflections reflections = new Reflections(PLUGIN_PKG)

        Set<Class<?>> plugins = reflections.getSubTypesOf(AbstractLanguagePlugin.class)

        plugins.each {
            AbstractLanguagePlugin inst = it.newInstance()
            LanguageDescriptor desc = inst.languageDescriptor()
            langMap[desc.name] = inst
        }
    }

    /**
     * 
     */
    static void loadPlugins() {
        def jars = []
        new File("./plugin").eachFileRecurse(FILES) {
            if (it.name.endsWith('.jar'))
                jars << it
        }

        jars.each {
            Manifest m = new JarFile(it.name).getManifest()
            Attributes attrs = m.getAttributes("Plugin-Class")
            String pluginClass
            if (attrs.size() > 0) {
                pluginClass = attrs.get("Plugin-Class")
            }

            if (pluginClass) {
                def cls = Class.forName(pluginClass).newInstance()
                if (cls instanceof LanguagePlugin) {
                }
            }

            this.class.classLoader.rootLoader.addURL(it.toURL())
        }
    }
}
