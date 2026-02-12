package com.github.ahmedwelhakim.schematocode.core.resolve

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef


data class ResolvedNames(
    val root: TypeDef,
    val symbols: SymbolTable
)
