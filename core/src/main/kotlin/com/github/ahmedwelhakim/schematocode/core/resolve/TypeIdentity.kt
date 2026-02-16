package com.github.ahmedwelhakim.schematocode.core.resolve

import com.github.ahmedwelhakim.schematocode.core.normalize.StructuralKey

/**
 * Semantic identity of a type, combining a name hint with structural information.
 *
 * Used to uniquely identify types during name resolution. Two types with the same
 * identity will share the same generated name.
 *
 * @property nameHint The suggested name for this type (derived from field name or root name).
 * @property structure The structural key representing the type's shape.
 */
data class TypeIdentity(
    val nameHint: String?,
    val structure: StructuralKey
)