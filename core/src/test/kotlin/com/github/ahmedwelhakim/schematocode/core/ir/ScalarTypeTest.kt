package com.github.ahmedwelhakim.schematocode.core.ir

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ScalarTypeTest {

    @Test
    fun `all scalar types exist`() {
        val types = ScalarType.entries
        assertEquals(5, types.size)
        assertTrue(types.contains(ScalarType.STRING))
        assertTrue(types.contains(ScalarType.INT))
        assertTrue(types.contains(ScalarType.DOUBLE))
        assertTrue(types.contains(ScalarType.BOOLEAN))
        assertTrue(types.contains(ScalarType.NULL))
    }

    @Test
    fun `valueOf round trips`() {
        ScalarType.entries.forEach { type ->
            assertEquals(type, ScalarType.valueOf(type.name))
        }
    }

    @Test
    fun `valueOf throws for invalid name`() {
        assertThrows(IllegalArgumentException::class.java) {
            ScalarType.valueOf("UNKNOWN")
        }
    }
}

