package com.github.ahmedwelhakim.schematocode.core.emit

import com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef

interface CodeEmitter<O : Any> {
    fun emit(ir: com.github.ahmedwelhakim.schematocode.core.ir.TypeDef, config: com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig, options: O): String
}