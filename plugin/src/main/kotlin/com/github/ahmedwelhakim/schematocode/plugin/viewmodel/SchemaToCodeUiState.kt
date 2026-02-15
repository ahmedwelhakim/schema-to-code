package com.github.ahmedwelhakim.schematocode.plugin.viewmodel

import com.github.ahmedwelhakim.schematocode.core.config.ModelEmissionMode
import com.github.ahmedwelhakim.schematocode.core.config.TargetLanguage
import com.github.ahmedwelhakim.schematocode.core.language.LanguageDescriptor
import com.github.ahmedwelhakim.schematocode.core.naming.NamingStrategyType
import com.github.ahmedwelhakim.schematocode.core.options.OptionDef
import com.github.ahmedwelhakim.schematocode.core.options.OptionKey
import com.github.ahmedwelhakim.schematocode.plugin.language.LanguageRegistry

data class SchemaToCodeUiState(
    val jsonInput: String = "",
    val output: String = "",
    val targetLanguage: TargetLanguage = TargetLanguage.TYPESCRIPT,
    val namingStrategy: NamingStrategyType = NamingStrategyType.PASCAL,

    val emissionMode: ModelEmissionMode = ModelEmissionMode.SEPARATE,
    val isLoading: Boolean = false,
    val error: String? = null,
    private var _languageOptions: MutableMap<String, String> = mutableMapOf(),
) {
    var languageOptions: Map<String, String>
        get() = _languageOptions.toMap()
        set(value) {
            _languageOptions = value.toMutableMap()
        }
    val descriptor
        get():LanguageDescriptor<*>
        = LanguageRegistry.getLanguageDescriptor(targetLanguage)

    val optionDefs
        get(): List<OptionDef<*>>
        = descriptor.optionDefs()

    fun setLanguageOption(key: OptionKey, value: Enum<*>) {
        _languageOptions["${targetLanguage.name}:$key"] = value.name
    }

    fun getLanguageOption(key: OptionKey): String? {
        return _languageOptions["${targetLanguage.name}:$key"]
    }
}
