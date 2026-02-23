package com.github.ahmedwelhakim.schematocode.core.config

import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKey
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class InputFormatTest {

    @Test
    fun `JSON format has correct bundle key`() {
        assertEquals(MessageKey.JSON.bundleKey, InputFormat.JSON.bundleKey)
    }

    @Test
    fun `JSON is the only input format`() {
        val formats = InputFormat.entries
        assertEquals(1, formats.size)
        assertTrue(formats.contains(InputFormat.JSON))
    }

    @Test
    fun `valueOf returns correct format`() {
        assertEquals(InputFormat.JSON, InputFormat.valueOf("JSON"))
    }

    @Test
    fun `valueOf throws for invalid name`() {
        assertThrows(IllegalArgumentException::class.java) {
            InputFormat.valueOf("XML")
        }
    }
}

