package com.github.ahmedwelhakim.schematocode.core.util

private val WORD_SEPARATOR_REGEX = Regex("[^A-Za-z0-9]+")

fun String.splitWords(): List<String> =
    split(WORD_SEPARATOR_REGEX).filter { it.isNotBlank() }

fun String.isValidIdentifier(): Boolean =
    matches(Regex("^[a-zA-Z_][a-zA-Z0-9_]*$"))
