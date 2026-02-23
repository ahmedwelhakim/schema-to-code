package com.github.ahmedwelhakim.schematocode.core.naming

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PascalCaseStrategyTest {

    private val strategy = PascalCaseStrategy()

    @Test
    fun `snake_case to PascalCase`() {
        assertEquals("UserName", strategy.fieldName("user_name"))
    }

    @Test
    fun `kebab-case to PascalCase`() {
        assertEquals("FirstName", strategy.fieldName("first-name"))
    }

    @Test
    fun `camelCase to PascalCase`() {
        assertEquals("UserName", strategy.fieldName("userName"))
    }

    @Test
    fun `single lowercase word capitalized`() {
        assertEquals("Id", strategy.fieldName("id"))
    }

    @Test
    fun `already PascalCase stays the same`() {
        assertEquals("UserName", strategy.fieldName("UserName"))
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

