package com.github.ahmedwelhakim.schematocode.core.resolve

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef

class TypeNameAllocator {

    private val symbols = SymbolTable()

    fun resolve(root: TypeDef, rootName: String?): ResolvedNames {
        fun visit(type: TypeDef, hint: String?) {
            when (type) {
                is TypeDef.ObjectT -> {
                    val base = hint ?: "Anonymous"
                    val typeIdentity = TypeIdentity(
                        nameHint = base,
                        structure = type
                    )
                    symbols.declare(type, typeIdentity, base)

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