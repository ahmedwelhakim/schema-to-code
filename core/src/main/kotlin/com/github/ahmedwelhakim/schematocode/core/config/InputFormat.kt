package com.github.ahmedwelhakim.schematocode.core.config

import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKey
import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKeyHolder

/**
 * Supported input formats for schema parsing.
 * Each format has an associated parser that converts the input to the intermediate representation.
 *
 * @property bundleKey The i18n bundle key for displaying the format name.
 */
enum class InputFormat(override val bundleKey: String) : MessageKeyHolder {
    /** JSON input format - parses JSON data to infer type structures. */
    JSON(MessageKey.JSON.bundleKey);
}