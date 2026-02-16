package com.github.ahmedwelhakim.schematocode.core.config

import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKey
import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKeyHolder

/**
 * Supported target languages for code generation.
 * Each language has a corresponding [LanguageDescriptor] that defines its emitter and options.
 *
 * @property bundleKey The i18n bundle key for displaying the language name.
 */
enum class TargetLanguage(override val bundleKey: String) : MessageKeyHolder {
    /** TypeScript language - generates TypeScript interfaces or type aliases. */
    TYPESCRIPT(MessageKey.TYPESCRIPT.bundleKey)
}