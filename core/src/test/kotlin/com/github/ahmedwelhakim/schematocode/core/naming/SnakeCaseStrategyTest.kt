package com.github.ahmedwelhakim.schematocode.core.naming

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SnakeCaseStrategyTest {

    private val strategy = SnakeCaseStrategy()

    @Test
    fun `camelCase to snake_case`() {
        assertEquals("user_name", strategy.fieldName("userName"))
    }

    @Test
    fun `PascalCase to snake_case`() {
        assertEquals("first_name", strategy.fieldName("FirstName"))
    }

    @Test
    fun `already snake_case stays the same`() {
        assertEquals("user_name", strategy.fieldName("user_name"))
    }

    @Test
    fun `single uppercase word lowered`() {
        assertEquals("id", strategy.fieldName("ID"))
    }

    @Test
    fun `single lowercase word stays the same`() {
        assertEquals("id", strategy.fieldName("id"))
    }

    @Test
    fun `empty string returns empty`() {
        assertEquals("", strategy.fieldName(""))
    }

    @Test
    fun `extractedTypeName defaults to PascalCase`() {
        assertEquals("UserName", strategy.extractedTypeName("user_name"))
    }
}

