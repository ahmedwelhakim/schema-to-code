package com.github.ahmedwelhakim.schematocode.core.naming

import com.github.ahmedwelhakim.schematocode.plugin.naming.internal.*


interface NamingStrategy {
    fun fieldName(raw: String): String
    fun extractedTypeName(rawKey: String): String
}

abstract class AbstractNamingStrategy(
    private val extractedTypeCase: NameCase
) : NamingStrategy {
    final override fun extractedTypeName(rawKey: String): String =
        when (extractedTypeCase) {
            NameCase.PRESERVE -> rawKey
            NameCase.PASCAL -> rawKey.toPascalCase()
            NameCase.CAMEL -> rawKey.toCamelCase()
            NameCase.SNAKE -> rawKey.toSnakeCase()
        }
}