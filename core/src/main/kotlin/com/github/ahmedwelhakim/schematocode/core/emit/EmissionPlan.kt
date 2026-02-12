package com.github.ahmedwelhakim.schematocode.core.emit

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import com.github.ahmedwelhakim.schematocode.core.resolve.SymbolTable

data class EmissionPlan(
    val units: List<EmissionUnit>,
    val root: TypeDef,
    val symbols: SymbolTable
)