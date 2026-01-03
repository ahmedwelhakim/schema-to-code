package com.github.ahmedwelhakim.schematocode.core.options

class EnumOption<T : Enum<T>>(
    key: OptionKeys,
    default: T,
    val values: Array<T>
) : OptionDef<T>(key, default)
