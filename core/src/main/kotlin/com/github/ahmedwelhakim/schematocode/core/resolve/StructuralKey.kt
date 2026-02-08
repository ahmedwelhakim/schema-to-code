package com.github.ahmedwelhakim.schematocode.core.resolve

import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef

sealed interface StructuralKey {

    data class Object(
        val fields: List<FieldKey>
    ) : StructuralKey

    data class Array(
        val element: StructuralKey
    ) : StructuralKey

    data class Primitive(
        val type: ScalarType
    ) : StructuralKey

    data class Union(
        val options: Set<StructuralKey>
    ) : StructuralKey

    object Any : StructuralKey
}

data class FieldKey(
    val name: String,
    val type: StructuralKey,
    val optional: Boolean
)

fun TypeDef.structuralKey(): StructuralKey =
    when (this) {
        is TypeDef.ObjectT ->
            StructuralKey.Object(
                fields.map {
                    FieldKey(
                        it.name,
                        it.type.structuralKey(),
                        it.optional
                    )
                }
            )

        is TypeDef.ArrayT ->
            StructuralKey.Array(element.structuralKey())

        is TypeDef.PrimitiveT ->
            StructuralKey.Primitive(type)

        is TypeDef.UnionT ->
            StructuralKey.Union(types.map { it.structuralKey() }.toSet())

        TypeDef.AnyT ->
            StructuralKey.Any
    }
