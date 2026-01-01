package com.github.ahmedwelhakim.schematocode.core.naming

class IdentityNamingStrategy(
    extractedTypeCase: NameCase = NameCase.PASCAL
) : AbstractNamingStrategy(extractedTypeCase) {

    override fun fieldName(raw: String): String = raw
}