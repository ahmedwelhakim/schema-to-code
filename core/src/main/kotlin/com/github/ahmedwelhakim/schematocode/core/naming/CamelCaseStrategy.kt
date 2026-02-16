package com.github.ahmedwelhakim.schematocode.core.naming

import com.github.ahmedwelhakim.schematocode.core.naming.internal.toCamelCase

/**
 * Naming strategy that converts field names to camelCase.
 *
 * Examples:
 * - "user_name" → "userName"
 * - "first-name" → "firstName"
 * - "ID" → "id"
 *
 * @param extractedTypeCase The case to use for extracted type names. Defaults to [NameCase.PASCAL].
 */
class CamelCaseStrategy(
    extractedTypeCase: NameCase = NameCase.PASCAL
) : AbstractNamingStrategy(extractedTypeCase) {

    override fun fieldName(raw: String): String =
        raw.toCamelCase()
}