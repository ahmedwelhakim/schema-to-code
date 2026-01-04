package com.github.ahmedwelhakim.schematocode.core.config

import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKey
import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKeyHolder

enum class TargetLanguage(override val bundleKey: String) : MessageKeyHolder {
    TYPESCRIPT(MessageKey.TYPESCRIPT.bundleKey);
}