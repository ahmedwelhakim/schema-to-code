package com.github.ahmedwelhakim.schematocode.core.emit

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import com.github.ahmedwelhakim.schematocode.core.resolve.TypeIdentity
import com.github.ahmedwelhakim.schematocode.core.resolve.TypeNameAllocator

/**
 * Plans the model declarations to be emitted from a type definition.
 * Traverses the type tree and collects all object types that need to be declared.
 *
 * @param resolver The type name allocator for resolving unique type names.
 */
class ModelPlanner(
    private val resolver: TypeNameAllocator
) {

    /**
     * Creates a model plan from the given type definition.
     *
     * @param ir The root type definition to plan.
     * @param rootName The name hint for the root type.
     * @return A [ModelPlan] containing all type declarations needed.
     */
    fun plan(ir: TypeDef, rootName: String): ModelPlan {
        val resolved = resolver.resolve(ir, rootName)
        val symbols = resolved.symbols
        val visited = mutableSetOf<TypeIdentity>()
        val units = mutableListOf<ModelDeclaration>()

        fun collect(type: TypeDef) {
            when (type) {
                is TypeDef.ObjectT -> {
                    val typeIdentity = symbols.typeIdentityOf(type)
                    if (visited.add(typeIdentity)) {
                        type.fields.forEach { collect(it.type) }
                        units += ModelDeclaration(symbols.nameOf(typeIdentity), type, typeIdentity)
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
