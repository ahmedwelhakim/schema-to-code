package com.github.ahmedwelhakim.schematocode.core.emit

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import com.github.ahmedwelhakim.schematocode.core.resolve.TypeIdentity
import com.github.ahmedwelhakim.schematocode.core.resolve.TypeNameAllocator

class ModelPlanner(
    private val resolver: TypeNameAllocator
) {

    fun plan(ir: TypeDef, rootName: String): ModelPlan {
        val resolved = resolver.resolve(ir, rootName)
        val symbols = resolved.symbols
        val visited = mutableSetOf<TypeIdentity>()
        val units = mutableListOf<ModelDeclaration>()

        fun collect(type: TypeDef) {
            when (type) {
                is TypeDef.ObjectT -> {
                    val semanticKey = symbols.semanticKeyOf(type)
                    if (visited.add(semanticKey)) {
                        type.fields.forEach { collect(it.type) }
                        units += ModelDeclaration(symbols.nameOf(semanticKey), type, semanticKey)
                    }

                }

                is TypeDef.ArrayT -> collect(type.element)
                is TypeDef.UnionT -> type.types.forEach { collect(it) }
                else -> Unit
            }
        }

        collect(ir)

        return ModelPlan(units = units, symbols = symbols, root = ir)
    }
}
