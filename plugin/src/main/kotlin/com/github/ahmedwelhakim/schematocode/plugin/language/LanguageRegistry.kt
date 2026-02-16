package com.github.ahmedwelhakim.schematocode.plugin.language

import com.github.ahmedwelhakim.schematocode.core.config.TargetLanguage
import com.github.ahmedwelhakim.schematocode.core.language.LanguageDescriptor
import com.github.ahmedwelhakim.schematocode.core.language.LanguageList

/**
 * Registry for accessing language descriptors in the plugin.
 *
 * Wraps the core [LanguageList] and provides lookup functionality
 * for finding descriptors by target language.
 */
object LanguageRegistry {

    private val languages: List<LanguageDescriptor<*>> =
        LanguageList.languages

    /**
     * Gets the language descriptor for the specified target language.
     *
     * @param targetLanguage The target language to find the descriptor for.
     * @return The language descriptor for the specified language.
     * @throws NoSuchElementException if no descriptor is registered for the language.
     */
    fun getLanguageDescriptor(targetLanguage: TargetLanguage): LanguageDescriptor<*> =
        languages.first { it.targetLanguage == targetLanguage }
}