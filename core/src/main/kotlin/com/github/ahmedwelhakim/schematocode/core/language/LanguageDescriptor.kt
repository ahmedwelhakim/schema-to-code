package com.github.ahmedwelhakim.schematocode.core.language

import com.github.ahmedwelhakim.schematocode.core.config.Language
import com.github.ahmedwelhakim.schematocode.core.emit.CodeEmitter
import com.github.ahmedwelhakim.schematocode.core.emit.LanguageOptions
import com.github.ahmedwelhakim.schematocode.core.options.OptionDef
import kotlin.reflect.KClass

interface LanguageDescriptor<O : LanguageOptions> {
    val language: Language
    val optionsClass: KClass<O>
    fun defaultOptions(): O
    fun createEmitter(options: O): CodeEmitter
    fun optionDefs(): List<OptionDef<*>>
}