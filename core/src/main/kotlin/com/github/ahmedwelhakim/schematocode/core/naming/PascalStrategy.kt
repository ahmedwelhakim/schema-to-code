package com.github.ahmedwelhakim.schematocode.core.naming

import com.github.ahmedwelhakim.schematocode.plugin.naming.internal.toPascalCase

class PascalCaseStrategy(
    extractedTypeCase: NameCase = NameCase.PASCAL
) : AbstractNamingStrategy(extractedTypeCase) {

    override fun fieldName(raw: String): String =
        raw.toPascalCase()
}