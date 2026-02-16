package com.github.ahmedwelhakim.schematocode.core.emit

import com.github.ahmedwelhakim.schematocode.core.options.OptionKey

/**
 * Interface for language-specific configuration options.
 * Each target language implements this to provide its own set of configurable options.
 *
 * Options are accessed using type-safe [OptionKey] identifiers.
 */
interface LanguageOptions {
    /**
     * Retrieves an option value by its key.
     *
     * @param T The expected type of the option value.
     * @param key The option key to look up.
     * @return The option value, or null if not set.
     */
    fun <T> get(key: OptionKey): T?

    /**
     * Creates a new options instance with the specified key-value pair.
     * This is an immutable operation - the original instance is not modified.
     *
     * @param key The option key to set.
     * @param value The option value.
     * @return A new [LanguageOptions] instance with the updated value.
     */
    fun with(key: OptionKey, value: Any): LanguageOptions
}