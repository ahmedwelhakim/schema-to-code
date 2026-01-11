package com.github.ahmedwelhakim.schematocode.core.normalize

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef

private const val MAX_UNION_SIZE = 12

fun mergeTypes(
    types: List<TypeDef>,
): TypeDef {

    if (types.isEmpty()) return TypeDef.AnyT


    val flattened = types.flatMap {
        when (it) {
            is TypeDef.UnionT -> it.types
            is TypeDef.ArrayT -> listOf(TypeDef.ArrayT(mergeTypes(listOf(it.element))))
            is TypeDef.ObjectT -> listOf(mergeObjects(listOf(it)))
            else -> listOf(it)
        }
    }


    val unique = flattened
        .distinctBy { it.structuralKey() }


    if (unique.size == 1) return unique.first()
    if (unique.all { it is TypeDef.ArrayT }) {
        val mergedElement = mergeTypes(
            unique.map { (it as TypeDef.ArrayT).element },

            )
        return TypeDef.ArrayT(mergedElement)
    }

    if (unique.all { it is TypeDef.ObjectT }) {
        return mergeObjects(unique.map { it as TypeDef.ObjectT })
    }


    if (unique.size > MAX_UNION_SIZE) {
        return TypeDef.AnyT
    }

    return TypeDef.UnionT(unique.toSet())
}
