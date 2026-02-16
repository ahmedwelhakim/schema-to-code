package com.github.ahmedwelhakim.schematocode.core.naming

import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKey
import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKeyHolder

/**
 * Available naming strategy types for transforming field and type names.
 *
 * @property bundleKey The i18n bundle key for displaying the strategy name.
 */
enum class NamingStrategyType(override val bundleKey: String) : MessageKeyHolder {
    /** Preserves names exactly as they appear in the input. */
    IDENTITY(MessageKey.IDENTITY.bundleKey),

    /** Converts names to PascalCase (e.g., "user_name" → "UserName"). */
    PASCAL(MessageKey.PASCAL.bundleKey),

    /** Converts names to camelCase (e.g., "user_name" → "userName"). */
    CAMEL(MessageKey.CAMEL.bundleKey),

    /** Converts names to snake_case (e.g., "userName" → "user_name"). */
    SNAKE(MessageKey.SNAKE.bundleKey)
}

/**
 * Creates a [NamingStrategy] instance for this strategy type.
 *
 * @return A new naming strategy instance.
 */
fun NamingStrategyType.create(): NamingStrategy = when (this) {
    NamingStrategyType.IDENTITY -> IdentityNamingStrategy()
    NamingStrategyType.PASCAL -> PascalCaseStrategy()
    NamingStrategyType.CAMEL -> CamelCaseStrategy()
    NamingStrategyType.SNAKE -> SnakeCaseStrategy()
}
