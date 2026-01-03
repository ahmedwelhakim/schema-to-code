package com.github.ahmedwelhakim.schematocode.core.emit.typescript

import com.github.ahmedwelhakim.schematocode.core.emit.LanguageOptions

data class TypescriptOptions(
    val modelKind: TypescriptModelKind = TypescriptModelKind.INTERFACE
) : LanguageOptions
