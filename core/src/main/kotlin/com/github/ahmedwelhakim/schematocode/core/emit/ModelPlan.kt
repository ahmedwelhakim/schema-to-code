package com.github.ahmedwelhakim.schematocode.core.emit

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import com.github.ahmedwelhakim.schematocode.core.resolve.SymbolTable

/**
 * Represents a plan for emitting model declarations.
 *
 * @property units The list of model declarations to emit.
 * @property root The root type definition.
 * @property symbols The symbol table mapping types to their names.
 */
data class ModelPlan(
    val units: List<ModelDeclaration>,
    val root: TypeDef,
    val symbols: SymbolTable
)