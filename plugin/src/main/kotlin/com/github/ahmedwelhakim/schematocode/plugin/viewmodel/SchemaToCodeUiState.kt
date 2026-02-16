package com.github.ahmedwelhakim.schematocode.plugin.viewmodel

import com.github.ahmedwelhakim.schematocode.core.config.TargetLanguage
import com.github.ahmedwelhakim.schematocode.core.language.LanguageDescriptor
import com.github.ahmedwelhakim.schematocode.core.naming.NamingStrategyType
import com.github.ahmedwelhakim.schematocode.core.options.OptionDef
import com.github.ahmedwelhakim.schematocode.core.options.OptionKey
import com.github.ahmedwelhakim.schematocode.plugin.language.LanguageRegistry

/**
 * Immutable UI state for the Schema to Code tool window.
 * All state changes should be done via copy().
 */
data class SchemaToCodeUiState(
    val jsonInput: String = "",
    val output: String = "",
    val targetLanguage: TargetLanguage = TargetLanguage.TYPESCRIPT,
    val namingStrategy: NamingStrategyType = NamingStrategyType.PASCAL,
    val isLoading: Boolean = false,
    val error: String? = null,
    val languageOptions: Map<String, String> = emptyMap(),
) {
    val descriptor: LanguageDescriptor<*>
        get() = LanguageRegistry.getLanguageDescriptor(targetLanguage)

    val optionDefs: List<OptionDef<*>>
        get() = descriptor.optionDefs()

    /**
     * Creates a new state with the specified language option set.
     */
    fun withLanguageOption(key: OptionKey, value: Enum<*>): SchemaToCodeUiState =
        copy(languageOptions = languageOptions + ("${targetLanguage.name}:$key" to value.name))

    /**
     * Gets the value of a language option.
     */
    fun getLanguageOption(key: OptionKey): String? =
        languageOptions["${targetLanguage.name}:$key"]
}
