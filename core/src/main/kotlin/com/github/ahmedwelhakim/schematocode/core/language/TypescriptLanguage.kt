package com.github.ahmedwelhakim.schematocode.core.language

import com.github.ahmedwelhakim.schematocode.core.config.TargetLanguage
import com.github.ahmedwelhakim.schematocode.core.emit.CodeEmitter
import com.github.ahmedwelhakim.schematocode.core.emit.typescript.*
import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKey
import com.github.ahmedwelhakim.schematocode.core.options.EnumOption
import com.github.ahmedwelhakim.schematocode.core.options.OptionDef
import com.github.ahmedwelhakim.schematocode.core.options.OptionKey

/**
 * Language descriptor for TypeScript.
 *
 * Provides configuration and factory methods for generating TypeScript code.
 * Supports the following options:
 * - [TypescriptOptionKey.MODEL_KIND]: Generate interfaces or type aliases.
 *
 * @see TypescriptEmitter
 * @see TypescriptOptions
 */
object TypescriptLanguage : LanguageDescriptor<TypescriptOptions> {
    override val targetLanguage: TargetLanguage = TargetLanguage.TYPESCRIPT

    override fun defaultOptions(): TypescriptOptions = TypescriptOptions()

    override fun createEmitter(options: TypescriptOptions): CodeEmitter = TypescriptEmitter(options)

    override fun optionDefs(): List<OptionDef<*>> = listOf(
        EnumOption(
            MessageKey.MODEL_KIND.bundleKey,
            TypescriptOptionKey.MODEL_KIND,
            TypescriptModelKind.INTERFACE,
            TypescriptModelKind.entries.toTypedArray()
        ),
        EnumOption(
            MessageKey.MODEL_EMISSION_MODE.bundleKey,
            TypescriptOptionKey.EMISSION_MODE,
            ModelEmissionMode.SEPARATE,
            ModelEmissionMode.entries.toTypedArray()
        ),
    )

    override fun parseOptionKey(name: String?): OptionKey? =
        if (name == null) name else
            runCatching { TypescriptOptionKey.valueOf(name) }.getOrNull()

    override fun parseOptionValue(key: OptionKey, value: String?): Any? {
        // to be exhaustive, we need to cast the key type before parsing the value
        val typeScriptKey: TypescriptOptionKey = key as TypescriptOptionKey
        return if (value == null) null
        else when (typeScriptKey) {
            TypescriptOptionKey.MODEL_KIND ->
                runCatching { TypescriptModelKind.valueOf(value) }.getOrNull()

            TypescriptOptionKey.EMISSION_MODE ->
                runCatching { ModelEmissionMode.valueOf(value) }.getOrNull()
        }
    }

    override fun parseOptionFromMap(map: Map<String, String>): TypescriptOptions {
        val modelKindStringValue = map["${TargetLanguage.TYPESCRIPT.name}:${TypescriptOptionKey.MODEL_KIND.name}"]
        val emissionModeStringValue = map["${TargetLanguage.TYPESCRIPT.name}:${TypescriptOptionKey.EMISSION_MODE.name}"]
        var options = defaultOptions()
        if (modelKindStringValue != null) {
            val modelKind = runCatching { TypescriptModelKind.valueOf(modelKindStringValue) }.getOrNull()
            if (modelKind != null) {
                options = options.copy(modelKind = modelKind)
            }
        }
        if (emissionModeStringValue != null) {
            val emissionMode = runCatching { ModelEmissionMode.valueOf(emissionModeStringValue) }.getOrNull()
            if (emissionMode != null) {
                options = options.copy(emissionMode = emissionMode)
            }
        }
        return options
    }
}