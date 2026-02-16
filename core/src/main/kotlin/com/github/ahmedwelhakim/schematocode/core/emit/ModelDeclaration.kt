package com.github.ahmedwelhakim.schematocode.core.emit

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import com.github.ahmedwelhakim.schematocode.core.resolve.TypeIdentity

/**
 * Represents a single type declaration to be emitted.
 * This is the output of the planning phase, ready for code generation.
 *
 * @property name The resolved unique name for this type (e.g., "User", "Address").
 * @property type The object type definition containing the fields.
 * @property typeIdentity The semantic identity used for type resolution and deduplication.
 */
data class ModelDeclaration(
    val name: String,
    val type: TypeDef.ObjectT,
    val typeIdentity: TypeIdentity
)
