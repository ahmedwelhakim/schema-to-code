package com.github.ahmedwelhakim.schematocode.core.language

import com.github.ahmedwelhakim.schematocode.core.language.LanguageList.languages


/**
 * Registry of all available language descriptors.
 *
 * Add new language implementations to the [languages] list to make them available
 * for code generation.
 */
object LanguageList {
    /** All registered language descriptors. */
    val languages: List<LanguageDescriptor<*>> = listOf(
        TypescriptLanguage
    )
}