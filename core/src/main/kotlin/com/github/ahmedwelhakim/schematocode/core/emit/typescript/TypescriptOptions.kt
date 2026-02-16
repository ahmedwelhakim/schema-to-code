package com.github.ahmedwelhakim.schematocode.core.emit.typescript

import com.github.ahmedwelhakim.schematocode.core.emit.LanguageOptions
import com.github.ahmedwelhakim.schematocode.core.options.OptionKey

/**
 * Configuration options specific to TypeScript code generation.
 *
 * @property modelKind Determines whether to emit interfaces or type aliases.
 *                     Defaults to [TypescriptModelKind.INTERFACE].
 */
data class TypescriptOptions(
    val modelKind: TypescriptModelKind = TypescriptModelKind.INTERFACE,
    val emissionMode: ModelEmissionMode = ModelEmissionMode.SEPARATE
) : LanguageOptions {

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(key: OptionKey): T? =
        when (key) {
            TypescriptOptionKey.MODEL_KIND -> modelKind as T
            else -> null
        }

    override fun with(
        key: OptionKey,
        value: Any
    ): LanguageOptions {
        return when (key) {
            TypescriptOptionKey.MODEL_KIND -> copy(modelKind = value as TypescriptModelKind)
            else -> this
        }
    }

}
