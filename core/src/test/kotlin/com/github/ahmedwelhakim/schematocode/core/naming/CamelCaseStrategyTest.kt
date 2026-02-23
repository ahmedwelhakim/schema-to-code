package com.github.ahmedwelhakim.schematocode.core.naming

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CamelCaseStrategyTest {

    private val strategy = CamelCaseStrategy()

    // ── fieldName ──────────────────────────────────────────────

    @Test
    fun `snake_case to camelCase`() {
        assertEquals("userName", strategy.fieldName("user_name"))
    }

    @Test
    fun `kebab-case to camelCase`() {
        assertEquals("firstName", strategy.fieldName("first-name"))
    }

    @Test
    fun `PascalCase to camelCase`() {
        assertEquals("userName", strategy.fieldName("UserName"))
    }

    @Test
    fun `already camelCase stays the same`() {
        assertEquals("userName", strategy.fieldName("userName"))
    }

    @Test
    fun `single word is lowered`() {
        assertEquals("id", strategy.fieldName("ID"))
    }

    @Test
    fun `empty string returns empty`() {
        assertEquals("", strategy.fieldName(""))
    }

    // ── extractedTypeName ──────────────────────────────────────

    @Test
    fun `extractedTypeName defaults to PascalCase`() {
        assertEquals("UserName", strategy.extractedTypeName("user_name"))
    }

    @Test
    fun `extractedTypeName with CAMEL case`() {
        val s = CamelCaseStrategy(NameCase.CAMEL)
        assertEquals("userName", s.extractedTypeName("user_name"))
    }

    @Test
    fun `extractedTypeName with SNAKE case`() {
        val s = CamelCaseStrategy(NameCase.SNAKE)
        assertEquals("user_name", s.extractedTypeName("UserName"))
    }

    @Test
    fun `extractedTypeName with PRESERVE case`() {
        val s = CamelCaseStrategy(NameCase.PRESERVE)
        assertEquals("user_name", s.extractedTypeName("user_name"))
    }
}

