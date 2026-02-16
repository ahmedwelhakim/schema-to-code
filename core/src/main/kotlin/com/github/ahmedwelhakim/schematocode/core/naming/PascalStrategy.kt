package com.github.ahmedwelhakim.schematocode.core.naming

import com.github.ahmedwelhakim.schematocode.core.naming.internal.toPascalCase

/**
 * Naming strategy that converts field names to PascalCase.
 *
 * Examples:
 * - "user_name" → "UserName"
 * - "first-name" → "FirstName"
 * - "id" → "Id"
 *
 * @param extractedTypeCase The case to use for extracted type names. Defaults to [NameCase.PASCAL].
 */
class PascalCaseStrategy(
    extractedTypeCase: NameCase = NameCase.PASCAL
) : AbstractNamingStrategy(extractedTypeCase) {

    override fun fieldName(raw: String): String =
        raw.toPascalCase()
}