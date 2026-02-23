package com.github.ahmedwelhakim.schematocode.core.ir

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class FormatTest {

    @Test
    fun `UUID is a singleton`() {
        assertSame(Format.UUID, Format.UUID)
    }

    @Test
    fun `DateTime is a singleton`() {
        assertSame(Format.DateTime, Format.DateTime)
    }

    @Test
    fun `Custom format holds name`() {
        val format = Format.Custom("iso8601")
        assertEquals("iso8601", format.name)
    }

    @Test
    fun `Custom format equality`() {
        val a = Format.Custom("test")
        val b = Format.Custom("test")
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun `Custom format inequality`() {
        val a = Format.Custom("a")
        val b = Format.Custom("b")
        assertNotEquals(a, b)
    }

    @Test
    fun `all formats implement Format interface`() {
        val formats: List<Format> = listOf(
            Format.UUID,
            Format.DateTime,
            Format.Custom("test")
        )
        formats.forEach { assertTrue(it is Format) }
    }
}

