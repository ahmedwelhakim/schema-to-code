package com.github.ahmedwelhakim.schematocode.core.options

/**
 * Base class for configuration option definitions.
 *
 * Option definitions describe the available configuration options for a language,
 * including their key identifier, default value, and type constraints.
 *
 * @param T The type of the option value.
 * @property key The unique key identifying this option.
 * @property default The default value for this option.
 *
 * @see BooleanOption
 * @see EnumOption
 */
sealed class OptionDef<T>(
    val key: OptionKey,
    val default: T
)


