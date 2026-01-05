package com.github.ahmedwelhakim.schematocode.core.emit.typescript

import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKey
import com.github.ahmedwelhakim.schematocode.core.options.OptionKey

enum class TypescriptOptionKey(override val bundleKey: String) : OptionKey {
    MODEL_KIND(MessageKey.MODEL_KIND.bundleKey)
}