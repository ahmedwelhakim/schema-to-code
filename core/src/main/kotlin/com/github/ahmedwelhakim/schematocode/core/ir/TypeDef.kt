package com.github.ahmedwelhakim.schematocode.core.ir

/**
 * Intermediate representation of a type in the schema.
 * This is language-agnostic and used during the transformation pipeline.
 *
 * Note: These are intentionally regular classes (not data classes) to preserve
 * instance identity. Two objects with the same structure but from different
 * field names should be treated as distinct types for separate interface generation.
 */
sealed interface TypeDef {

    /** Represents an unknown or dynamic type (e.g., `any` in TypeScript). */
    object AnyT : TypeDef

    /** Represents a primitive/scalar type like string, number, boolean. */
    class PrimitiveT(
        val type: ScalarType,
        val format: Format? = null
    ) : TypeDef

    /** Represents an array/list type containing elements of another type. */
    class ArrayT(val element: TypeDef) : TypeDef

    /** Represents an object/record type with named fields. */
    class ObjectT(val fields: List<Field>) : TypeDef

    /** Represents a union of multiple possible types. */
    class UnionT(val types: Set<TypeDef>) : TypeDef

}

