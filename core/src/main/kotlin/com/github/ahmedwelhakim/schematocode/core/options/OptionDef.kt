package com.github.ahmedwelhakim.schematocode.core.options

sealed class OptionDef<T>(
    val key: OptionKey,
    val default: T
)


