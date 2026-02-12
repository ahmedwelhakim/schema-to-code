package com.github.ahmedwelhakim.schematocode.core.emit

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import com.github.ahmedwelhakim.schematocode.core.resolve.NameResolver
import com.github.ahmedwelhakim.schematocode.core.resolve.SemanticKey
import com.github.ahmedwelhakim.schematocode.core.resolve.structuralKey

class EmissionPlanner(
    private val resolver: NameResolver
) {

    fun plan(ir: TypeDef, rootName: String): EmissionPlan {
        val resolved = resolver.resolve(ir, rootName)
        val symbols = resolved.symbols
        val visited = mutableSetOf<SemanticKey>()
        val units = mutableListOf<EmissionUnit>()

        fun collect(type: TypeDef, nameHint: String? = null) {
            when (type) {
                is TypeDef.ObjectT -> {
                    val typeName = nameHint ?: "Anonymous"
                    val semanticKey = SemanticKey(
                        nameHint = typeName,
                        structure = type.structuralKey()
                    )

                    if (visited.add(semanticKey)) {
                        type.fields.forEach { collect(it.type, it.name) }
                        units += EmissionUnit(symbols.nameOf(semanticKey), type, semanticKey)
                    }

                }

                is TypeDef.ArrayT -> collect(type.element, nameHint?.plus("Item"))
                is TypeDef.UnionT -> type.types.forEach { collect(it, nameHint) }
                else -> Unit
            }
        }

        collect(ir, rootName)

        return EmissionPlan(units = units)
    }
}
