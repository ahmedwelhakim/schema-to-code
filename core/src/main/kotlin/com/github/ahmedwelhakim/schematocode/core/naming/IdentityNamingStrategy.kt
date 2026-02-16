package com.github.ahmedwelhakim.schematocode.core.naming

/**
 * Naming strategy that preserves field names exactly as they appear in the input.
 * No transformation is applied to field names.
 *
 * Examples:
 * - "user_name" → "user_name"
 * - "firstName" → "firstName"
 * - "ID" → "ID"
 *
 * @param extractedTypeCase The case to use for extracted type names. Defaults to [NameCase.PASCAL].
 */
class IdentityNamingStrategy(
    extractedTypeCase: NameCase = NameCase.PASCAL
) : AbstractNamingStrategy(extractedTypeCase) {

    override fun fieldName(raw: String): String = raw
}