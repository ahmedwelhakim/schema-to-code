package com.github.ahmedwelhakim.schematocode.plugin.language

import com.github.ahmedwelhakim.schematocode.core.config.TargetLanguage
import com.github.ahmedwelhakim.schematocode.core.language.LanguageDescriptor
import com.github.ahmedwelhakim.schematocode.core.language.LanguageList

object LanguageRegistry {

    private val languages: List<LanguageDescriptor<*>> =
        LanguageList.languages

    fun getLanguageDescriptor(targetLanguage: TargetLanguage): LanguageDescriptor<*> =
        languages.first { it.targetLanguage == targetLanguage }
}