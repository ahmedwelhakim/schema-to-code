package com.github.ahmedwelhakim.schematocode.core.naming

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class IdentityNamingStrategyTest {

    private val strategy = IdentityNamingStrategy()

    @Test
    fun `fieldName preserves original exactly`() {
        assertEquals("user_name", strategy.fieldName("user_name"))
    }

    @Test
    fun `fieldName preserves camelCase`() {
        assertEquals("userName", strategy.fieldName("userName"))
    }

    @Test
    fun `fieldName preserves PascalCase`() {
        assertEquals("UserName", strategy.fieldName("UserName"))
    }

    @Test
    fun `fieldName preserves kebab-case`() {
        assertEquals("user-name", strategy.fieldName("user-name"))
    }

    @Test
    fun `fieldName preserves empty string`() {
        assertEquals("", strategy.fieldName(""))
    }

    @Test
    fun `extractedTypeName defaults to PascalCase`() {
        assertEquals("UserName", strategy.extractedTypeName("user_name"))
    }

    @Test
    fun `extractedTypeName with PRESERVE keeps original`() {
        val s = IdentityNamingStrategy(NameCase.PRESERVE)
        assertEquals("user_name", s.extractedTypeName("user_name"))
    }
}

