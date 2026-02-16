package com.github.ahmedwelhakim.schematocode.core.options

import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKeyHolder

/**
 * Option definition for enum-based configuration values.
 *
 * Allows selection from a predefined set of values, typically displayed
 * as a dropdown in the UI.
 *
 * @param T The enum type, must implement [MessageKeyHolder] for localization.
 * @param key The unique key identifying this option.
 * @param default The default enum value.
 * @param values All possible values for this option (typically `EnumClass.entries.toTypedArray()`).
 */
class EnumOption<T : MessageKeyHolder>(
    i18nName: String,
    key: OptionKey,
    default: T,
    val values: Array<T>
) : OptionDef<T>(i18nName, key, default)
