package com.github.ahmedwelhakim.schematocode.core.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class StringUtilsTest {

    // ── splitWords ─────────────────────────────────────────────

    @Test
    fun `splitWords with camelCase`() {
        assertEquals(listOf("user", "Name"), "userName".splitWords())
    }

    @Test
    fun `splitWords with snake_case`() {
        assertEquals(listOf("user", "name"), "user_name".splitWords())
    }

    @Test
    fun `splitWords with acronym`() {
        assertEquals(listOf("XMLParser"), "XMLParser".splitWords())
    }

    @Test
    fun `splitWords with acronym followed by lowercase`() {
        assertEquals(listOf("user", "ID"), "userID".splitWords())
    }

    @Test
    fun `splitWords with kebab-case`() {
        assertEquals(listOf("first", "name"), "first-name".splitWords())
    }

    @Test
    fun `splitWords with single word`() {
        assertEquals(listOf("hello"), "hello".splitWords())
    }

    @Test
    fun `splitWords with empty string`() {
        assertTrue("".splitWords().isEmpty())
    }

    @Test
    fun `splitWords with mixed separators`() {
        assertEquals(listOf("one", "two", "Three"), "one_two-Three".splitWords())
    }

    // ── isValidIdentifier ──────────────────────────────────────

    @Test
    fun `isValidIdentifier with valid identifier`() {
        assertTrue("valid_name_123".isValidIdentifier())
    }

    @Test
    fun `isValidIdentifier starts with letter`() {
        assertTrue("name".isValidIdentifier())
    }

    @Test
    fun `isValidIdentifier starts with underscore`() {
        assertTrue("_private".isValidIdentifier())
    }

    @Test
    fun `isValidIdentifier with digits not at start`() {
        assertTrue("x1".isValidIdentifier())
    }

    @Test
    fun `isValidIdentifier invalid - starts with digit`() {
        assertFalse("123invalid".isValidIdentifier())
    }

    @Test
    fun `isValidIdentifier invalid - contains hyphen`() {
        assertFalse("invalid-name".isValidIdentifier())
    }

    @Test
    fun `isValidIdentifier invalid - contains space`() {
        assertFalse("invalid name".isValidIdentifier())
    }

    @Test
    fun `isValidIdentifier invalid - empty string`() {
        assertFalse("".isValidIdentifier())
    }

    // ── indent ─────────────────────────────────────────────────

    @Test
    fun `indent with 4 spaces`() {
        assertEquals("    ", indent(4))
    }

    @Test
    fun `indent with 0 spaces`() {
        assertEquals("", indent(0))
    }

    @Test
    fun `indent with 1 space`() {
        assertEquals(" ", indent(1))
    }

    @Test
    fun `indent with 8 spaces`() {
        assertEquals("        ", indent(8))
    }
}
