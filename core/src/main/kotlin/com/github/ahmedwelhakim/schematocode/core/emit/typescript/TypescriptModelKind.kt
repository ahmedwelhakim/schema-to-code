package com.github.ahmedwelhakim.schematocode.core.emit.typescript

import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKey
import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKeyHolder

/**
 * Defines the TypeScript construct used for generated types.
 *
 * @property bundleKey The i18n bundle key for displaying the option name.
 */
enum class TypescriptModelKind(override val bundleKey: String) : MessageKeyHolder {
    /**
     * Generates TypeScript interfaces.
     * Example: `export interface User { name: string; }`
     */
    INTERFACE(MessageKey.INTERFACE.bundleKey),

    /**
     * Generates TypeScript type aliases.
     * Example: `export type User = { name: string; }`
     */
    TYPE_ALIAS(MessageKey.TYPE_ALIAS.bundleKey)
}