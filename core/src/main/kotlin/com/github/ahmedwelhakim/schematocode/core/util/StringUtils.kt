package com.github.ahmedwelhakim.schematocode.core.util

private val WORD_SEPARATOR_REGEX = Regex("[^A-Za-z0-9]+")
private val CAMEL_CASE_BOUNDARY =
    Regex("(?<=[a-z0-9])(?=[A-Z])")

fun String.splitWords(): List<String> =
    this.replace(CAMEL_CASE_BOUNDARY, " ")
        .split(WORD_SEPARATOR_REGEX)
        .filter { it.isNotBlank() }

fun String.isValidIdentifier(): Boolean =
    matches(Regex("^[a-zA-Z_][a-zA-Z0-9_]*$"))

fun indent(size: Int): String = " ".repeat(size)