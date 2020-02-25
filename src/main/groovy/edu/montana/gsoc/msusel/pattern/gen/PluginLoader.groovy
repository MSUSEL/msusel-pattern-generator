package edu.montana.gsoc.msusel.pattern.gen

import edu.montana.gsoc.msusel.pattern.gen.current.LanguageProvider
import edu.montana.gsoc.msusel.pattern.gen.plugin.LanguageDescriptor
import org.reflections.Reflections

@Singleton
class PluginLoader {

    static final String PLUGIN_PKG = "edu.montana.gsoc.msusel.pattern.gen"
    static Map<String, LanguageProvider> langMap = [:]

    void loadBuiltInLanguageProviders() {
        Reflections reflections = new Reflections(PLUGIN_PKG)

        Set<Class<? extends LanguageProvider>> plugins = reflections.getSubTypesOf(LanguageProvider.class)

        plugins.each {
            LanguageProvider inst = it.newInstance()
            LanguageDescriptor desc = inst.languageDescriptor()
            langMap[desc.name] = inst
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
