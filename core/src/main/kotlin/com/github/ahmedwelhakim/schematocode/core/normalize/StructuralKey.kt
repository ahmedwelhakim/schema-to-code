package com.github.ahmedwelhakim.schematocode.core.normalize

import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef

/**
 * Immutable structural representation of a type for comparison and deduplication.
 *
 * Unlike [TypeDef], which uses reference identity, [StructuralKey] uses data class
 * equality. Two types with identical structures will have equal structural keys,
 * enabling deduplication during normalization.
 *
 * @see TypeDef.structuralKey
 */
sealed interface StructuralKey {
    /** Structural key for primitive types. */
    data class Primitive(val type: ScalarType) : StructuralKey

    /** Structural key for array types. */
    data class Array(val element: StructuralKey) : StructuralKey

    /** Structural key for object types (fields sorted by name for consistent comparison). */
    data class Object(val fields: List<FieldKey>) : StructuralKey

    /** Structural key for union types. */
    data class Union(val types: Set<StructuralKey>) : StructuralKey

    /** Structural key for the any/unknown type. */
    object Any : StructuralKey
}

/**
 * Structural representation of a field within an object type.
 *
 * @property name The field name.
 * @property type The structural key of the field's type.
 * @property optional Whether the field is optional.
 */
data class FieldKey(
    val name: String,
    val type: StructuralKey,
    val optional: Boolean
)

/**
 * Creates a structural key for this type definition.
 *
 * The structural key can be used to compare types by structure rather than identity.
 * Two types with identical structures will produce equal structural keys.
 *
 * @return The structural key representing this type's structure.
 */
fun TypeDef.structuralKey(): StructuralKey =
    when (this) {
        is TypeDef.PrimitiveT ->
            StructuralKey.Primitive(type)

        is TypeDef.ArrayT ->
            StructuralKey.Array(element.structuralKey())

        is TypeDef.ObjectT ->
            StructuralKey.Object(
                fields
                    .sortedBy { it.name }
                    .map {
                        FieldKey(
                            it.name,
                            it.type.structuralKey(),
                            it.optional
                        )
                    }
            )

        is TypeDef.UnionT ->
            StructuralKey.Union(
                types.map { it.structuralKey() }.toSet()
            )

        TypeDef.AnyT ->
            StructuralKey.Any
    }
