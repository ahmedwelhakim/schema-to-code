package com.github.ahmedwelhakim.schematocode.core.resolve

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef

data class SemanticKey(
    val nameHint: String,
    val structure: TypeDef
)