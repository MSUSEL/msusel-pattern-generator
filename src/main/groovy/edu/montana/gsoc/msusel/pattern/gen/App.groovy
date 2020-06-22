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

import groovy.cli.picocli.CliBuilder
import groovy.cli.picocli.OptionAccessor

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class App {

    public static final String VERSION = "1.3.0"

    static void main(String[] args) {
        // Setup CLI
        CommandLineInterface cli = CommandLineInterface.INSTANCE
        ConfigLoader loader = new ConfigLoader()
        PluginLoader plugins = PluginLoader.instance

        // Process Command Line Args
        String config, base, lang
        (config, base, lang) = cli.parse(args)

        File fBase = new File(base)
        fBase.mkdirs()
        File fConfig = new File(config)

        // Load Configuration
        plugins.loadBuiltInLanguageProviders()
        loader.loadConfiguration(fConfig, fBase)
        plugins.loadLanguage(lang)

        // Run the program
        Director director = new Director()
        director.initialize()
        director.execute()
    }
}

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
enum CommandLineInterface {

    INSTANCE

    CliBuilder cliBuilder

    CommandLineInterface() {
        cliBuilder = new CliBuilder(
                usage: "patterngen [options] <base>",
                header: "\nOptions:",
                footer: "\nCopyright (c) 2018-2020 Isaac Griffith and Montana State University",
        )

        // set the amount of columns the usage message will be in width
        cliBuilder.width = 80 // default is 74
        cliBuilder.with {
            h longOpt: 'help', 'Print this help text and exit.'
            g longOpt: 'generate-only', 'Only generate patterns from the database'
            d longOpt: 'data-elements-only', 'Only construct data elements, no code'
            r longOpt: 'reset-db', 'Resets the database to an empty configuration'
            c(longOpt: 'config-file', args: 1, argName: 'file', 'File to use for configuration')
            v longOpt: 'version', 'Print the version information'
            l longOpt: 'language', 'Sets the language used by the generator'
            x longOpt: 'reset-only', 'Only resets the database'
        }
    }

    def parse(args) {
        GeneratorContext context = GeneratorContext.getInstance()
        OptionAccessor options = cliBuilder.parse(args)

        if (!options) {
            System.err.println 'Error while parsing command-line options.\n'
            System.exit 1
        }

        if (options.v) {
            println "patterngen version ${App.VERSION}"
            System.exit 0
        }

        if (options.h) {
            cliBuilder.usage()
            System.exit 0
        }

        if (options.g) {
            context.generateOnly = true
        }

        if (options.d) {
            context.dataOnly = true
        }

        if (options.r) {
            context.resetDb = true
        }

        if (options.x) {
            context.resetDb = true
            context.resetOnly = true
        }

        String config
        if (options.c) {
            config = options.c
        } else {
            config = "PatternGenConfig"
        }

        String base
        if (options.arguments()) {
            base = options.arguments()[0]
        } else {
            base = "."
        }

        String lang
        if (options.l) {
            lang = options.l
        } else {
            lang = "Java"
        }

        return [config, base, lang]
    }
}
