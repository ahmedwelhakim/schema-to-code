package com.github.ahmedwelhakim.schematocode.core.naming

import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKey
import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKeyHolder

enum class NamingStrategyType(override val bundleKey: String) : MessageKeyHolder {
    IDENTITY(MessageKey.IDENTITY.bundleKey),
    PASCAL(MessageKey.PASCAL.bundleKey),
    CAMEL(MessageKey.CAMEL.bundleKey),
    SNAKE(MessageKey.SNAKE.bundleKey);
}

fun NamingStrategyType.create(): NamingStrategy = when (this) {
    NamingStrategyType.IDENTITY -> IdentityNamingStrategy()
    NamingStrategyType.PASCAL -> PascalCaseStrategy()
    NamingStrategyType.CAMEL -> CamelCaseStrategy()
    NamingStrategyType.SNAKE -> SnakeCaseStrategy()
}
