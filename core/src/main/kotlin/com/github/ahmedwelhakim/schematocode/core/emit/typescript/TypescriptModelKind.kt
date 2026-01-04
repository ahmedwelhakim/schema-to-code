package com.github.ahmedwelhakim.schematocode.core.emit.typescript

import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKey
import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKeyHolder


enum class TypescriptModelKind(override val bundleKey: String) : MessageKeyHolder {
    INTERFACE(MessageKey.INTERFACE.bundleKey),
    TYPE_ALIAS(MessageKey.TYPE_ALIAS.bundleKey);

}