package com.github.ahmedwelhakim.schematocode.core.emit.typescript

import com.github.ahmedwelhakim.schematocode.core.emit.LanguageOptions
import com.github.ahmedwelhakim.schematocode.core.options.OptionKey

data class TypescriptOptions(
    val modelKind: TypescriptModelKind = TypescriptModelKind.INTERFACE
) : LanguageOptions {
    override fun <T> get(key: OptionKey): T? =
        @Suppress("UNCHECKED_CAST")
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
