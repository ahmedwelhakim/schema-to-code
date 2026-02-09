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

        fun collect(type: TypeDef) {
            when (type) {
                is TypeDef.ObjectT -> {
                    val semanticKey = SemanticKey(
                        nameHint = type.name,
                        structure = type.structuralKey()
                    )

                    if (visited.add(semanticKey)) {
                        type.fields.forEach { collect(it.type) }
                        units += EmissionUnit(symbols.nameOf(type), type)
                    }

                }

                is TypeDef.ArrayT -> collect(type.element)
                is TypeDef.UnionT -> type.types.forEach(::collect)
                else -> Unit
            }
        }

        collect(ir)

        return EmissionPlan(units = units)
    }
}
