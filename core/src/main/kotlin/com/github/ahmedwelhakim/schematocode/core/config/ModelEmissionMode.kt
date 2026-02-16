package com.github.ahmedwelhakim.schematocode.core.config

import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKey
import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKeyHolder

/**
 * Defines how generated type declarations are structured in the output.
 *
 * @property bundleKey The i18n bundle key for displaying the mode name.
 */
enum class ModelEmissionMode(override val bundleKey: String) : MessageKeyHolder {
    /**
     * Each type is emitted as a separate, top-level declaration.
     * Example: `interface User { ... }` and `interface Address { ... }` as separate types.
     */
    SEPARATE(MessageKey.SEPARATE.bundleKey),

    /**
     * Nested types are inlined within their parent type.
     * Example: `interface User { address: { street: string; city: string; } }`
     */
    NESTED(MessageKey.NESTED.bundleKey)
}