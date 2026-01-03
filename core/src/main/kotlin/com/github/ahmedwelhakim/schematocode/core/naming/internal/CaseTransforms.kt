package com.github.ahmedwelhakim.schematocode.core.naming.internal

import com.github.ahmedwelhakim.schematocode.core.util.splitWords

internal fun String.toPascalCase(): String =
    splitWords()
        .joinToString("") { it.lowercase().replaceFirstChar(Char::uppercase) }

internal fun String.toCamelCase(): String {
    val parts = splitWords()
    if (parts.isEmpty()) return ""

    return parts.first().lowercase() +
            parts.drop(1).joinToString("") {
                it.lowercase().replaceFirstChar(Char::uppercase)
            }
}

internal fun String.toSnakeCase(): String =
    splitWords()
        .joinToString("_") { it.lowercase() }