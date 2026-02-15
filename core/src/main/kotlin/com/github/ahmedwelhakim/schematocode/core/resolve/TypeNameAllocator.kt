package com.github.ahmedwelhakim.schematocode.core.resolve

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import com.github.ahmedwelhakim.schematocode.core.normalize.structuralKey

class TypeNameAllocator {

    private val symbols = SymbolTable()

    fun resolve(root: TypeDef, rootName: String?): ResolvedNames {
        fun visit(type: TypeDef, hint: String?) {
            when (type) {
                is TypeDef.ObjectT -> {

                    val typeIdentity = TypeIdentity(
                        nameHint = hint,
                        structure = type.structuralKey()
                    )
                    symbols.declare(type, typeIdentity, hint ?: "Anonymous")

                    type.fields.forEach {
                        visit(it.type, it.name)
                    }
                }

                is TypeDef.ArrayT ->
                    visit(type.element, hint?.plus("Item"))

                is TypeDef.UnionT ->
                    type.types.forEach { visit(it, hint) }

                else -> Unit
            }
        }

        visit(root, rootName)

        return ResolvedNames(root, symbols)
    }
}