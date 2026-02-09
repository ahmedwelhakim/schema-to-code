package com.github.ahmedwelhakim.schematocode.core.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StringUtilsTest {
    @Test
    fun `capitalize works`() {
        assertEquals("Hello", "hello".replaceFirstChar { it.uppercase() })
    }
}

