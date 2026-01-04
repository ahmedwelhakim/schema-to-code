package com.github.ahmedwelhakim.schematocode.plugin.language

import com.github.ahmedwelhakim.schematocode.core.config.TargetLanguage
import com.github.ahmedwelhakim.schematocode.core.language.LanguageDescriptor
import java.util.*

object LanguageRegistry {

    private val languages: List<LanguageDescriptor<*>> =
        ServiceLoader.load(LanguageDescriptor::class.java).toList()

    fun getLanguageDescriptor(targetLanguage: TargetLanguage): LanguageDescriptor<*> =
        languages.first { it.targetLanguage == targetLanguage }
}