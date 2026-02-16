package com.github.ahmedwelhakim.schematocode.core.ir

/**
 * Represents a field within an object type.
 *
 * @property name The original field name from the input schema.
 * @property type The type definition of this field.
 * @property optional Whether this field is optional (nullable).
 */
data class Field(val name: String, val type: TypeDef, val optional: Boolean = false)
