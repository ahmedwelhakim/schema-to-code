package com.github.ahmedwelhakim.schematocode.core.language

import com.github.ahmedwelhakim.schematocode.core.config.Language
import com.github.ahmedwelhakim.schematocode.core.emit.CodeEmitter
import com.github.ahmedwelhakim.schematocode.core.emit.typescript.TypescriptEmitter
import com.github.ahmedwelhakim.schematocode.core.emit.typescript.TypescriptModelKind
import com.github.ahmedwelhakim.schematocode.core.emit.typescript.TypescriptOptions
import com.github.ahmedwelhakim.schematocode.core.options.EnumOption
import com.github.ahmedwelhakim.schematocode.core.options.OptionDef
import com.github.ahmedwelhakim.schematocode.core.options.OptionKeys
import kotlin.reflect.KClass

object TypescriptLanguage : LanguageDescriptor<TypescriptOptions> {
    override val language: Language = Language.TYPESCRIPT
    override val optionsClass: KClass<TypescriptOptions> = TypescriptOptions::class
    override fun defaultOptions(): TypescriptOptions = TypescriptOptions()
    override fun createEmitter(options: TypescriptOptions): CodeEmitter = TypescriptEmitter(options)
    override fun optionDefs(): List<OptionDef<*>> = listOf(
        EnumOption(OptionKeys.MODEL_KIND, TypescriptModelKind.INTERFACE, TypescriptModelKind.values()),
    )
}