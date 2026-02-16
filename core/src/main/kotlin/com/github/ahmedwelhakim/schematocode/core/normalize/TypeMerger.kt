package com.github.ahmedwelhakim.schematocode.core.normalize

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef

/** Maximum number of types in a union before collapsing to AnyT. */
private const val MAX_UNION_SIZE = 12

/**
 * Merges multiple types into a single unified type.
 *
 * This function handles:
 * - Flattening nested unions
 * - Merging arrays with the same element structure
 * - Merging objects with the same field names
 * - Deduplicating structurally identical types
 * - Collapsing large unions (> [MAX_UNION_SIZE]) to [TypeDef.AnyT]
 *
 * @param types The list of types to merge.
 * @return A single merged type representing all input types.
 */
fun mergeTypes(
    types: List<TypeDef>,
): TypeDef {

    if (types.isEmpty()) return TypeDef.AnyT

    // Flatten unions and recursively process arrays/objects
    val flattened = types.flatMap {
        when (it) {
            is TypeDef.UnionT -> it.types
            is TypeDef.ArrayT -> listOf(TypeDef.ArrayT(mergeTypes(listOf(it.element))))
            is TypeDef.ObjectT -> listOf(mergeObjects(listOf(it)))
            else -> listOf(it)
        }
    }

    // Deduplicate by structural key
    val unique = flattened
        .distinctBy { it.structuralKey() }

    // Single type - return as-is
    if (unique.size == 1) return unique.first()

    // All arrays - merge into single array with merged element type
    if (unique.all { it is TypeDef.ArrayT }) {
        val mergedElement = mergeTypes(
            unique.map { (it as TypeDef.ArrayT).element },
        )
        return TypeDef.ArrayT(mergedElement)
    }

    // All objects - merge into single object
    if (unique.all { it is TypeDef.ObjectT }) {
        return mergeObjects(unique.map { it as TypeDef.ObjectT })
    }

    // Too many types - collapse to any
    if (unique.size > MAX_UNION_SIZE) {
        return TypeDef.AnyT
    }

    // Return as union
    return TypeDef.UnionT(unique.toSet())
}
