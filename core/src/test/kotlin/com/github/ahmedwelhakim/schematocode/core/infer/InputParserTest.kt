package com.github.ahmedwelhakim.schematocode.core.infer

import com.github.ahmedwelhakim.schematocode.core.config.InputFormat
import com.github.ahmedwelhakim.schematocode.core.infer.json.JsonInputParser
import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InputParserTest {

    @Test
    fun `JsonInputParser parses simple object`() {
        val json = """{"name": "test", "count": 42}"""

        val result = JsonInputParser.parse(json, "Root")

        assertTrue(result is TypeDef.ObjectT)
        val obj = result as TypeDef.ObjectT
        assertEquals(2, obj.fields.size)
    }

    @Test
    fun `JsonInputParser throws InputParseException for invalid JSON`() {
        val invalidJson = "not valid json"

        assertThrows<InputParseException> {
            JsonInputParser.parse(invalidJson, "Root")
        }
    }

    @Test
    fun `JsonInputParser parses array`() {
        val json = """[1, 2, 3]"""

        val result = JsonInputParser.parse(json, "Root")

        assertTrue(result is TypeDef.ArrayT)
    }

    @Test
    fun `JsonInputParser parses primitive string`() {
        val json = """"hello""""

        val result = JsonInputParser.parse(json, "Root")

        assertTrue(result is TypeDef.PrimitiveT)
        assertEquals(ScalarType.STRING, (result as TypeDef.PrimitiveT).type)
    }

    @Test
    fun `InputParserRegistry returns JSON parser`() {
        val parser = InputParserRegistry.getParser(InputFormat.JSON)

        assertSame(JsonInputParser, parser)
    }

    @Test
    fun `InputParserRegistry supportedFormats includes JSON`() {
        val formats = InputParserRegistry.supportedFormats()

        assertTrue(formats.contains(InputFormat.JSON))
    }
}

