package com.github.ahmedwelhakim.schematocode.core.options

import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKeyHolder

/**
 * Marker interface for option keys.
 *
 * Option keys uniquely identify configuration options within a language.
 * They extend [MessageKeyHolder] to support localized display names.
 *
 * Typically implemented as an enum for each target language.
 *
 * @see com.github.ahmedwelhakim.schematocode.core.emit.typescript.TypescriptOptionKey
 */
interface OptionKey : MessageKeyHolder