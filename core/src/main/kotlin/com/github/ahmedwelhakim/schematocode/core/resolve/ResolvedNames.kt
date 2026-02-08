package com.github.ahmedwelhakim.schematocode.core.resolve

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef

// core/resolve/ResolvedNames.kt
data class ResolvedNames(
    val root: TypeDef,
    val symbols: SymbolTable
)
