package com.github.ahmedwelhakim.schematocode.core.naming

import com.github.ahmedwelhakim.schematocode.core.naming.internal.toCamelCase
import com.github.ahmedwelhakim.schematocode.core.naming.internal.toPascalCase
import com.github.ahmedwelhakim.schematocode.core.naming.internal.toSnakeCase

/**
 * Strategy for transforming names during code generation.
 * Implementations handle different naming conventions (camelCase, snake_case, etc.).
 */
interface NamingStrategy {
    /**
     * Transforms a raw field name according to this strategy.
     * @param raw The original field name from the input.
     * @return The transformed field name.
     */
    fun fieldName(raw: String): String

    /**
     * Transforms a raw key into an extracted type name.
     * @param rawKey The original key or hint for the type name.
     * @return The transformed type name.
     */
    fun extractedTypeName(rawKey: String): String
}

/**
 * Base class for naming strategies that use a specific case for extracted type names.
 *
 * @param extractedTypeCase The case to use for extracted type names.
 */
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