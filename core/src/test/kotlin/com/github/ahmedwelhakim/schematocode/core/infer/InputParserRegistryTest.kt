package com.github.ahmedwelhakim.schematocode.core.infer

import com.github.ahmedwelhakim.schematocode.core.config.InputFormat
import com.github.ahmedwelhakim.schematocode.core.infer.json.JsonInputParser
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class InputParserRegistryTest {

    @Test
    fun `getParser returns JsonInputParser for JSON format`() {
        val parser = InputParserRegistry.getParser(InputFormat.JSON)
        assertSame(JsonInputParser, parser)
    }

    @Test
    fun `supportedFormats includes JSON`() {
        val formats = InputParserRegistry.supportedFormats()
        assertTrue(formats.contains(InputFormat.JSON))
    }

    @Test
    fun `supportedFormats returns non-empty set`() {
        val formats = InputParserRegistry.supportedFormats()
        assertTrue(formats.isNotEmpty())
    }
}

