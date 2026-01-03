package com.github.ahmedwelhakim.schematocode.plugin.language

import com.github.ahmedwelhakim.schematocode.core.config.Language
import com.github.ahmedwelhakim.schematocode.core.language.LanguageDescriptor
import java.util.*

object LanguageRegistry {

    private val languages: List<LanguageDescriptor<*>> =
        ServiceLoader.load(LanguageDescriptor::class.java).toList()

    fun getLanguageDescriptor(language: Language): LanguageDescriptor<*> =
        languages.first { it.language == language }
}