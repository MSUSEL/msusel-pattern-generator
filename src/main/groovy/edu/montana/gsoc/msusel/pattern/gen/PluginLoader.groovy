package edu.montana.gsoc.msusel.pattern.gen

import edu.montana.gsoc.msusel.pattern.gen.generators.LanguageProvider
import edu.montana.gsoc.msusel.pattern.gen.generators.java.JavaLanguageProvider
import edu.montana.gsoc.msusel.pattern.gen.plugin.LanguageDescriptor

@Singleton
class PluginLoader {

    static final String PLUGIN_PKG = "edu.montana.gsoc.msusel.pattern.gen"
    static Map<String, LanguageProvider> langMap = [:]

    void loadBuiltInLanguageProviders() {

        def plugins = [new JavaLanguageProvider()]

        plugins.each {
            LanguageDescriptor desc = it.languageDescriptor()
            langMap[desc.name] = it
        }
    }

    void loadLanguage(String lang) {
        GeneratorContext ctx = GeneratorContext.instance
        if (langMap[lang]) {
            ctx.plugin = langMap[lang]
            ctx.srcExt = ctx.plugin.languageDescriptor().fileExt
            ctx.resetComponentGenerators()
            ctx.resetPatternBuilderComponents()
        }
    }
}
