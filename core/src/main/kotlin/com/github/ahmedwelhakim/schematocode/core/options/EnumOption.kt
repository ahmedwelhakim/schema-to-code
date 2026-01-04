package com.github.ahmedwelhakim.schematocode.core.options

import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKeyHolder

class EnumOption<T : MessageKeyHolder>(
    key: OptionKeys,
    default: T,
    val values: Array<T>
) : OptionDef<T>(key, default)
