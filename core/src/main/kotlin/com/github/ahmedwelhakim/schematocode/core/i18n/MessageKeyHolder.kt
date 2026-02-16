package com.github.ahmedwelhakim.schematocode.core.i18n

/**
 * Interface for types that have an associated i18n bundle key.
 * Implemented by enums and other types that need localized display names.
 *
 * @property bundleKey The key used to look up the localized string in the resource bundle.
 */
interface MessageKeyHolder {
    val bundleKey: String
}