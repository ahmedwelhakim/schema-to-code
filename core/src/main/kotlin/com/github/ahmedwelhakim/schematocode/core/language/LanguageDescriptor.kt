package com.github.ahmedwelhakim.schematocode.core.language

import com.github.ahmedwelhakim.schematocode.core.config.TargetLanguage
import com.github.ahmedwelhakim.schematocode.core.emit.CodeEmitter
import com.github.ahmedwelhakim.schematocode.core.emit.LanguageOptions
import com.github.ahmedwelhakim.schematocode.core.options.OptionDef
import com.github.ahmedwelhakim.schematocode.core.options.OptionKey

interface LanguageDescriptor<O : LanguageOptions> {
    val targetLanguage: TargetLanguage
    fun defaultOptions(): O
    fun createEmitter(options: O): CodeEmitter
    fun optionDefs(): List<OptionDef<*>>
    fun parseOptionKey(name: String?): OptionKey?
    fun parseOptionValue(key: OptionKey, value: String?): Any?
    fun parseOptionFromMap(map: Map<String, String>): O
}