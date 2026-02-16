package com.github.ahmedwelhakim.schematocode.core.options

/**
 * Option definition for boolean (true/false) configuration values.
 *
 * @param key The unique key identifying this option.
 * @param default The default boolean value.
 */
class BooleanOption(
    key: OptionKey,
    default: Boolean
) : OptionDef<Boolean>(key, default)