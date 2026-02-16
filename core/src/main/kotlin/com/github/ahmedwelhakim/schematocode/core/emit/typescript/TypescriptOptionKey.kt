package com.github.ahmedwelhakim.schematocode.core.emit.typescript

import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKey
import com.github.ahmedwelhakim.schematocode.core.options.OptionKey

/**
 * Option keys for TypeScript-specific configuration.
 * Used to retrieve and set values in [TypescriptOptions].
 *
 * @property bundleKey The i18n bundle key for displaying the option name.
 */
enum class TypescriptOptionKey(override val bundleKey: String) : OptionKey {
    /** Option key for selecting between interface and type alias generation. */
    MODEL_KIND(MessageKey.MODEL_KIND.bundleKey),
    EMISSION_MODE(MessageKey.MODEL_EMISSION_MODE.bundleKey)
}