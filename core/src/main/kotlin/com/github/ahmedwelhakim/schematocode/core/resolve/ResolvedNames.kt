package com.github.ahmedwelhakim.schematocode.core.resolve

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef

/**
 * Result of name resolution, containing the root type and symbol table.
 *
 * @property root The root type definition that was resolved.
 * @property symbols The symbol table mapping types to their unique names.
 */
data class ResolvedNames(
    val root: TypeDef,
    val symbols: SymbolTable
)
