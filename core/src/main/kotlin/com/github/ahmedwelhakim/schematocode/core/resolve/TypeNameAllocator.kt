package com.github.ahmedwelhakim.schematocode.core.resolve

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import com.github.ahmedwelhakim.schematocode.core.normalize.structuralKey

/**
 * Allocates unique names for types by traversing the type tree.
 * Creates a symbol table mapping types to their resolved names.
 */
class TypeNameAllocator {

    private val symbols = SymbolTable()

    /**
     * Resolves names for all types in the type tree.
     *
     * @param root The root type definition to resolve.
     * @param rootName The name hint for the root type.
     * @return A [ResolvedNames] containing the root and symbol table.
     */
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