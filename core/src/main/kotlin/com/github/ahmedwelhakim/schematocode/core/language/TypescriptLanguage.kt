package com.github.ahmedwelhakim.schematocode.core.language

import com.github.ahmedwelhakim.schematocode.core.config.TargetLanguage
import com.github.ahmedwelhakim.schematocode.core.emit.CodeEmitter
import com.github.ahmedwelhakim.schematocode.core.emit.typescript.TypescriptEmitter
import com.github.ahmedwelhakim.schematocode.core.emit.typescript.TypescriptModelKind
import com.github.ahmedwelhakim.schematocode.core.emit.typescript.TypescriptOptionKey
import com.github.ahmedwelhakim.schematocode.core.emit.typescript.TypescriptOptions
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
            TypescriptOptionKey.MODEL_KIND,
            TypescriptModelKind.INTERFACE,
            TypescriptModelKind.entries.toTypedArray()
        ),
    )

    override fun parseOptionKey(name: String?): OptionKey? =
        if (name == null) name else
            runCatching { TypescriptOptionKey.valueOf(name) }.getOrNull()

    override fun parseOptionValue(key: OptionKey, value: String?): Any? =
        if (value == null) null
        else when (key) {
            TypescriptOptionKey.MODEL_KIND ->
                runCatching { TypescriptModelKind.valueOf(value) }.getOrNull()

            else -> null
        }

    override fun parseOptionFromMap(map: Map<String, String>): TypescriptOptions {
        val modelKindStringValue = map["${TargetLanguage.TYPESCRIPT.name}:${TypescriptOptionKey.MODEL_KIND.name}"]
        return if (modelKindStringValue != null)
            TypescriptOptions(
                modelKind = parseOptionValue(
                    TypescriptOptionKey.MODEL_KIND,
                    modelKindStringValue
                ) as TypescriptModelKind
            ) else defaultOptions()
    }
}