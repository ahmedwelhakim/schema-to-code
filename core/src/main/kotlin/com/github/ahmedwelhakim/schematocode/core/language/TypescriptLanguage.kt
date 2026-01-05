package com.github.ahmedwelhakim.schematocode.core.language

import com.github.ahmedwelhakim.schematocode.core.config.TargetLanguage
import com.github.ahmedwelhakim.schematocode.core.emit.CodeEmitter
import com.github.ahmedwelhakim.schematocode.core.emit.typescript.TypescriptEmitter
import com.github.ahmedwelhakim.schematocode.core.emit.typescript.TypescriptModelKind
import com.github.ahmedwelhakim.schematocode.core.emit.typescript.TypescriptOptionKey
import com.github.ahmedwelhakim.schematocode.core.emit.typescript.TypescriptOptions
import com.github.ahmedwelhakim.schematocode.core.options.EnumOption
import com.github.ahmedwelhakim.schematocode.core.options.OptionDef

object TypescriptLanguage : LanguageDescriptor<TypescriptOptions> {
    override val targetLanguage: TargetLanguage = TargetLanguage.TYPESCRIPT
    override fun defaultOptions(): TypescriptOptions = TypescriptOptions()
    override fun createEmitter(options: TypescriptOptions): CodeEmitter = TypescriptEmitter(options)
    override fun optionDefs(): List<OptionDef<*>> = listOf(
        EnumOption(
            TypescriptOptionKey.MODEL_KIND,
            TypescriptModelKind.INTERFACE,
            TypescriptModelKind.entries.toTypedArray()
        ),
    )
}