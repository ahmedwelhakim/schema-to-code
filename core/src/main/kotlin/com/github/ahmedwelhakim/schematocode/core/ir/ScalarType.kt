package com.github.ahmedwelhakim.schematocode.core.ir

/**
 * Enumeration of primitive/scalar types supported in the intermediate representation.
 * These map to language-specific primitive types during code emission.
 */
enum class ScalarType {
    /** String/text type (e.g., `string` in TypeScript, `String` in Java). */
    STRING,

    /** Integer number type (e.g., `number` in TypeScript, `int` in Java). */
    INT,

    /** Floating-point number type (e.g., `number` in TypeScript, `double` in Java). */
    DOUBLE,

    /** Boolean type (e.g., `boolean` in TypeScript/Java). */
    BOOLEAN,

    /** Null/absent value type (e.g., `null` in TypeScript). */
    NULL
}