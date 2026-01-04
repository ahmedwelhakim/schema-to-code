package com.github.ahmedwelhakim.schematocode.core.options

import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKey
import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKeyHolder

enum class OptionKeys(override val bundleKey: String) : MessageKeyHolder {
    MODEL_KIND(MessageKey.MODEL_KIND.bundleKey),
    NAMING_STRATEGY(MessageKey.NAMING_STRATEGY.bundleKey);
}