package com.github.ahmedwelhakim.schematocode.core.config

import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKey
import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKeyHolder

enum class InputFormat(override val bundleKey: String) : MessageKeyHolder {
    JSON(MessageKey.JSON.bundleKey);
}