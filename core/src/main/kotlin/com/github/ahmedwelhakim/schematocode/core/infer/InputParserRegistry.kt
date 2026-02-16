package com.github.ahmedwelhakim.schematocode.core.infer

import com.github.ahmedwelhakim.schematocode.core.config.InputFormat
import com.github.ahmedwelhakim.schematocode.core.infer.json.JsonInputParser

/**
 * Registry for input parsers.
 * Maps input formats to their corresponding parser implementations.
 */
object InputParserRegistry {
    private val parsers: Map<InputFormat, InputParser> = mapOf(
        InputFormat.JSON to JsonInputParser
    )

    /**
     * Gets the parser for the specified input format.
     *
     * @param format The input format to get the parser for.
     * @return The input parser for the format.
     * @throws IllegalArgumentException if no parser is registered for the format.
     */
    fun getParser(format: InputFormat): InputParser =
        parsers[format]
            ?: throw IllegalArgumentException("No parser registered for format: $format")

    /**
     * Returns all supported input formats.
     */
    fun supportedFormats(): Set<InputFormat> = parsers.keys
}

