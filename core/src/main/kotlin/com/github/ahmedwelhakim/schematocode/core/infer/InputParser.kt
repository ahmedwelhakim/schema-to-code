package com.github.ahmedwelhakim.schematocode.core.infer

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef

/**
 * Interface for parsing input data into the intermediate representation.
 * Implementations handle specific input formats (JSON, YAML, etc.).
 */
interface InputParser {
    /**
     * Parses the input string into a TypeDef intermediate representation.
     *
     * @param input The raw input string to parse.
     * @param rootName The name hint for the root type.
     * @return The parsed type definition.
     * @throws InputParseException if parsing fails.
     */
    fun parse(input: String, rootName: String): TypeDef
}

/**
 * Exception thrown when input parsing fails.
 */
class InputParseException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)

