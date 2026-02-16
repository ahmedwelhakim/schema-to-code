package com.github.ahmedwelhakim.schematocode.core.language

import com.github.ahmedwelhakim.schematocode.core.config.TargetLanguage
import com.github.ahmedwelhakim.schematocode.core.emit.CodeEmitter
import com.github.ahmedwelhakim.schematocode.core.emit.LanguageOptions
import com.github.ahmedwelhakim.schematocode.core.options.OptionDef
import com.github.ahmedwelhakim.schematocode.core.options.OptionKey

/**
 * Descriptor for a target language, providing metadata and factory methods.
 * Each supported language implements this interface to define its configuration options
 * and code emitter creation.
 *
 * @param O The type of language-specific options.
 */
interface LanguageDescriptor<O : LanguageOptions> {
    /** The target language this descriptor represents. */
    val targetLanguage: TargetLanguage

    /** Creates the default options for this language. */
    fun defaultOptions(): O

    /** Creates a code emitter configured with the given options. */
    fun createEmitter(options: O): CodeEmitter

    /** Returns the list of configurable options for this language. */
    fun optionDefs(): List<OptionDef<*>>

    /** Parses an option key from its string name. */
    fun parseOptionKey(name: String?): OptionKey?

    /** Parses an option value from its string representation. */
    fun parseOptionValue(key: OptionKey, value: String?): Any?

    /** Parses language options from a string map. */
    fun parseOptionFromMap(map: Map<String, String>): O
}