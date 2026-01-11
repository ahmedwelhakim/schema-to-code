package com.github.ahmedwelhakim.schematocode.core.resolve

import com.github.ahmedwelhakim.schematocode.core.ir.Field
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef

internal class NameResolver {
    private val symbolTable = SymbolTable()


    fun resolve(type: TypeDef, name: String): TypeDef {
        symbolTable.declare(type, name)
        return rename(type)
    }

    private fun rename(type: TypeDef): TypeDef {
        return when (type) {
            is TypeDef.ObjectT -> type.copy(
                name = symbolTable.nameOf(type),
                fields = type.fields.map { renameField(it) })

            is TypeDef.ArrayT -> type.copy(element = rename(type.element))
            is TypeDef.UnionT -> type.copy(types = type.types.map { rename(it) }.toSet())
            else -> type
        }
    }

    private fun renameField(field: Field): Field {
        return field.copy(type = rename(field.type))
    }
}