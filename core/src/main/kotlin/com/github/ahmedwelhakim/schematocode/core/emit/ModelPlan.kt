package com.github.ahmedwelhakim.schematocode.core.emit

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import com.github.ahmedwelhakim.schematocode.core.resolve.SymbolTable

data class ModelPlan(
    val units: List<ModelDeclaration>,
    val root: TypeDef,
    val symbols: SymbolTable
)