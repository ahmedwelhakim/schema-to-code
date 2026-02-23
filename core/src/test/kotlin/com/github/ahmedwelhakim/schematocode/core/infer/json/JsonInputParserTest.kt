package com.github.ahmedwelhakim.schematocode.core.infer.json

import com.github.ahmedwelhakim.schematocode.core.infer.InputParseException
import com.github.ahmedwelhakim.schematocode.core.ir.Field
import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class JsonInputParserTest {

    // ── Primitives ─────────────────────────────────────────────

    @Test
    fun `parse string primitive`() {
        val result = JsonInputParser.parse("\"hello\"", "Root")
        assertTrue(result is TypeDef.PrimitiveT)
        assertEquals(ScalarType.STRING, (result as TypeDef.PrimitiveT).type)
    }

    @Test
    fun `parse integer primitive`() {
        val result = JsonInputParser.parse("42", "Root")
        assertTrue(result is TypeDef.PrimitiveT)
        assertEquals(ScalarType.INT, (result as TypeDef.PrimitiveT).type)
    }

    @Test
    fun `parse double primitive`() {
        val result = JsonInputParser.parse("3.14", "Root")
        assertTrue(result is TypeDef.PrimitiveT)
        assertEquals(ScalarType.DOUBLE, (result as TypeDef.PrimitiveT).type)
    }

    @Test
    fun `parse boolean primitive`() {
        val result = JsonInputParser.parse("true", "Root")
        assertTrue(result is TypeDef.PrimitiveT)
        assertEquals(ScalarType.BOOLEAN, (result as TypeDef.PrimitiveT).type)
    }

    @Test
    fun `parse null primitive`() {
        val result = JsonInputParser.parse("null", "Root")
        assertTrue(result is TypeDef.PrimitiveT)
        assertEquals(ScalarType.NULL, (result as TypeDef.PrimitiveT).type)
    }

    // ── Objects ────────────────────────────────────────────────

    @Test
    fun `parse simple object`() {
        val json = """{"name": "Alice", "age": 30}"""
        val result = JsonInputParser.parse(json, "User")

        assertTrue(result is TypeDef.ObjectT)
        val obj = result as TypeDef.ObjectT
        assertEquals(2, obj.fields.size)

        val nameField = obj.fields.first { it.name == "name" }
        assertTrue(nameField.type is TypeDef.PrimitiveT)
        assertEquals(ScalarType.STRING, (nameField.type as TypeDef.PrimitiveT).type)
        assertFalse(nameField.optional)

        val ageField = obj.fields.first { it.name == "age" }
        assertTrue(ageField.type is TypeDef.PrimitiveT)
        assertEquals(ScalarType.INT, (ageField.type as TypeDef.PrimitiveT).type)
    }

    @Test
    fun `parse nested object`() {
        val json = """{"user": {"name": "Alice"}}"""
        val result = JsonInputParser.parse(json, "Root")

        assertTrue(result is TypeDef.ObjectT)
        val outer = result as TypeDef.ObjectT
        val userField = outer.fields.first { it.name == "user" }
        assertTrue(userField.type is TypeDef.ObjectT)
        val inner = userField.type as TypeDef.ObjectT
        assertEquals(1, inner.fields.size)
        assertEquals("name", inner.fields[0].name)
    }

    @Test
    fun `parse empty object`() {
        val result = JsonInputParser.parse("{}", "Empty")
        assertTrue(result is TypeDef.ObjectT)
        assertEquals(0, (result as TypeDef.ObjectT).fields.size)
    }

    // ── Arrays ─────────────────────────────────────────────────

    @Test
    fun `parse empty array`() {
        val result = JsonInputParser.parse("[]", "Root")
        assertTrue(result is TypeDef.ArrayT)
        val arr = result as TypeDef.ArrayT
        assertSame(TypeDef.AnyT, arr.element)
    }

    @Test
    fun `parse array of strings`() {
        val result = JsonInputParser.parse("""["a", "b", "c"]""", "Root")
        assertTrue(result is TypeDef.ArrayT)
        val arr = result as TypeDef.ArrayT
        assertTrue(arr.element is TypeDef.UnionT)
    }

    @Test
    fun `parse array of mixed types`() {
        val result = JsonInputParser.parse("""[1, "two", true]""", "Root")
        assertTrue(result is TypeDef.ArrayT)
        val arr = result as TypeDef.ArrayT
        assertTrue(arr.element is TypeDef.UnionT)
        val union = arr.element as TypeDef.UnionT
        assertEquals(3, union.types.size)
    }

    @Test
    fun `parse array of objects`() {
        val json = """[{"id": 1}, {"id": 2}]"""
        val result = JsonInputParser.parse(json, "Items")
        assertTrue(result is TypeDef.ArrayT)
        val arr = result as TypeDef.ArrayT
        assertTrue(arr.element is TypeDef.UnionT)
    }

    // ── Complex Structures ─────────────────────────────────────

    @Test
    fun `parse complex nested structure`() {
        val json = """{
            "name": "Alice",
            "address": {
                "street": "Main St",
                "city": "Wonderland"
            },
            "tags": ["admin", "user"],
            "active": true
        }"""
        val result = JsonInputParser.parse(json, "User")

        assertTrue(result is TypeDef.ObjectT)
        val obj = result as TypeDef.ObjectT
        assertEquals(4, obj.fields.size)

        val addressField = obj.fields.first { it.name == "address" }
        assertTrue(addressField.type is TypeDef.ObjectT)

        val tagsField = obj.fields.first { it.name == "tags" }
        assertTrue(tagsField.type is TypeDef.ArrayT)
    }

    // ── Error Handling ─────────────────────────────────────────

    @Test
    fun `parse invalid JSON throws InputParseException`() {
        assertThrows(InputParseException::class.java) {
            JsonInputParser.parse("{invalid json}", "Root")
        }
    }

    @Test
    fun `parse empty string throws InputParseException`() {
        assertThrows(InputParseException::class.java) {
            JsonInputParser.parse("", "Root")
        }
    }
}

