package com.github.ahmedwelhakim.schematocode.core.util

/** Regex to match word separators (non-alphanumeric characters). */
private val WORD_SEPARATOR_REGEX = Regex("[^A-Za-z0-9]+")

/** Regex to find boundaries between lowercase and uppercase characters (for camelCase splitting). */
private val CAMEL_CASE_BOUNDARY = Regex("(?<=[a-z0-9])(?=[A-Z])")

/**
 * Splits a string into individual words based on camelCase boundaries and separators.
 *
 * Examples:
 * - "userName" → ["user", "Name"]
 * - "user_name" → ["user", "name"]
 * - "XMLParser" → ["XML", "Parser"]
 *
 * @return A list of words extracted from the string.
 */
fun String.splitWords(): List<String> =
    this.replace(CAMEL_CASE_BOUNDARY, " ")
        .split(WORD_SEPARATOR_REGEX)
        .filter { it.isNotBlank() }

/**
 * Checks if the string is a valid identifier in most programming languages.
 * Valid identifiers start with a letter or underscore, followed by letters, digits, or underscores.
 *
 * @return `true` if the string is a valid identifier, `false` otherwise.
 */
fun String.isValidIdentifier(): Boolean =
    matches(Regex("^[a-zA-Z_][a-zA-Z0-9_]*$"))

/**
 * Creates an indentation string of the specified size.
 *
 * @param size The number of spaces for indentation.
 * @return A string containing [size] space characters.
 */
fun indent(size: Int): String = " ".repeat(size)