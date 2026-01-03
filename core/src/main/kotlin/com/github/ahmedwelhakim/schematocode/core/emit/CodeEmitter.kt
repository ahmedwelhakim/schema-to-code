package com.github.ahmedwelhakim.schematocode.core.emit

import com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef

interface CodeEmitter {
    fun emit(ir: TypeDef, config: GeneratorConfig): String
}