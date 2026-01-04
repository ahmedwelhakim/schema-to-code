package com.github.ahmedwelhakim.schematocode.core.language

import com.github.ahmedwelhakim.schematocode.core.config.TargetLanguage
import com.github.ahmedwelhakim.schematocode.core.emit.CodeEmitter
import com.github.ahmedwelhakim.schematocode.core.emit.LanguageOptions
import com.github.ahmedwelhakim.schematocode.core.options.OptionDef

interface LanguageDescriptor<O : LanguageOptions> {
    val targetLanguage: TargetLanguage
    fun defaultOptions(): O
    fun createEmitter(options: O): CodeEmitter
    fun optionDefs(): List<OptionDef<*>>
}