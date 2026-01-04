package com.github.ahmedwelhakim.schematocode.core.naming

import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKey
import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKeyHolder

enum class NameCase(override val bundleKey: String) : MessageKeyHolder {
    PRESERVE(MessageKey.PRESERVE.bundleKey),
    PASCAL(MessageKey.PASCAL.bundleKey),
    CAMEL(MessageKey.CAMEL.bundleKey),
    SNAKE(MessageKey.SNAKE.bundleKey),
}