package com.github.ahmedwelhakim.schematocode.core.normalize

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef

/**
 * Normalizes type definitions by merging structurally equivalent types.
 * This reduces redundancy in the generated code by consolidating duplicate type structures.
 */
object TypeNormalizer {

    /**
     * Normalizes a type definition by recursively merging equivalent types.
     *
     * @param type The type to normalize.
     * @return The normalized type definition.
     */
    fun normalize(type: TypeDef): TypeDef {
        return when (type) {

            is TypeDef.ArrayT ->
                TypeDef.ArrayT(normalize(type.element))

            is TypeDef.ObjectT ->
                mergeObjects(listOf(type))

            is TypeDef.UnionT ->
                mergeTypes(type.types.toList())

            else -> type
        }
    }
}