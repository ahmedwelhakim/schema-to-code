package com.github.ahmedwelhakim.schematocode.plugin.language

import com.github.ahmedwelhakim.schematocode.core.config.TargetLanguage

fun getLanguageID(language: TargetLanguage): LanguageId = when (language) {
    TargetLanguage.TYPESCRIPT -> LanguageId.TYPESCRIPT
}