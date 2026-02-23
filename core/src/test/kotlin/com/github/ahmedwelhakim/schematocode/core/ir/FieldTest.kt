package com.github.ahmedwelhakim.schematocode.core.ir

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class FieldTest {

    @Test
    fun `field holds name and type`() {
        val field = Field("name", TypeDef.PrimitiveT(ScalarType.STRING))
        assertEquals("name", field.name)
        assertTrue(field.type is TypeDef.PrimitiveT)
        assertFalse(field.optional)
    }

    @Test
    fun `field optional defaults to false`() {
        val field = Field("x", TypeDef.PrimitiveT(ScalarType.INT))
        assertFalse(field.optional)
    }

    @Test
    fun `field can be optional`() {
        val field = Field("email", TypeDef.PrimitiveT(ScalarType.STRING), optional = true)
        assertTrue(field.optional)
    }

    @Test
    fun `field data class equality`() {
        val type = TypeDef.PrimitiveT(ScalarType.STRING)
        val a = Field("x", type, false)
        val b = Field("x", type, false)
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun `field data class inequality by name`() {
        val type = TypeDef.PrimitiveT(ScalarType.STRING)
        val a = Field("x", type)
        val b = Field("y", type)
        assertNotEquals(a, b)
    }

    @Test
    fun `field data class inequality by optionality`() {
        val type = TypeDef.PrimitiveT(ScalarType.STRING)
        val a = Field("x", type, optional = false)
        val b = Field("x", type, optional = true)
        assertNotEquals(a, b)
    }

    @Test
    fun `field copy works`() {
        val field = Field("name", TypeDef.PrimitiveT(ScalarType.STRING))
        val copy = field.copy(optional = true)
        assertEquals("name", copy.name)
        assertTrue(copy.optional)
    }
}

