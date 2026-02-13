package com.github.ahmedwelhakim.schematocode.core.config

import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKey
import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKeyHolder

enum class ModelEmissionMode(override val bundleKey: String) : MessageKeyHolder {
    SEPARATE(MessageKey.SEPARATE.bundleKey),
    NESTED(MessageKey.NESTED.bundleKey)
}