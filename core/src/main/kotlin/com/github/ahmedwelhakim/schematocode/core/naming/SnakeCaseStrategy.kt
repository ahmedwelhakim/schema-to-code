package com.github.ahmedwelhakim.schematocode.core.naming

import com.github.ahmedwelhakim.schematocode.core.naming.internal.toSnakeCase

/**
 * Naming strategy that converts field names to snake_case.
 *
 * Examples:
 * - "userName" → "user_name"
 * - "firstName" → "first_name"
 * - "ID" → "id"
 *
 * @param extractedTypeCase The case to use for extracted type names. Defaults to [NameCase.PASCAL].
 */
class SnakeCaseStrategy(
    extractedTypeCase: NameCase = NameCase.PASCAL
) : AbstractNamingStrategy(extractedTypeCase) {

    override fun fieldName(raw: String): String =
        raw.toSnakeCase()
}