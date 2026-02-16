package com.github.ahmedwelhakim.schematocode.core.naming.internal

import com.github.ahmedwelhakim.schematocode.core.util.splitWords

/**
 * Converts a string to PascalCase (UpperCamelCase).
 *
 * Example: "user_name" → "UserName", "first-name" → "FirstName"
 */
internal fun String.toPascalCase(): String =
    splitWords()
        .joinToString("") { it.lowercase().replaceFirstChar(Char::uppercase) }

/**
 * Converts a string to camelCase (lowerCamelCase).
 *
 * Example: "user_name" → "userName", "first-name" → "firstName"
 */
internal fun String.toCamelCase(): String {
    val parts = splitWords()
    if (parts.isEmpty()) return ""

    return parts.first().lowercase() +
            parts.drop(1).joinToString("") {
                it.lowercase().replaceFirstChar(Char::uppercase)
            }
}

/**
 * Converts a string to snake_case.
 *
 * Example: "userName" → "user_name", "FirstName" → "first_name"
 */
internal fun String.toSnakeCase(): String =
    splitWords()
        .joinToString("_") { it.lowercase() }