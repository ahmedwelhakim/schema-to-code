package com.github.ahmedwelhakim.schematocode.core.naming

import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKey
import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKeyHolder

/**
 * Case conventions for transforming extracted type names.
 * Used internally by naming strategies to determine the case of generated type names.
 *
 * @property bundleKey The i18n bundle key for displaying the case name.
 */
enum class NameCase(override val bundleKey: String) : MessageKeyHolder {
    /** Keep the original case unchanged. */
    PRESERVE(MessageKey.PRESERVE.bundleKey),

    /** Convert to PascalCase (e.g., "UserName"). */
    PASCAL(MessageKey.PASCAL.bundleKey),

    /** Convert to camelCase (e.g., "userName"). */
    CAMEL(MessageKey.CAMEL.bundleKey),

    /** Convert to snake_case (e.g., "user_name"). */
    SNAKE(MessageKey.SNAKE.bundleKey)
}