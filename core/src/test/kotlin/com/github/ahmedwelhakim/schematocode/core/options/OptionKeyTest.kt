package com.github.ahmedwelhakim.schematocode.core.options

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OptionKeyTest {
    @Test
    fun `OptionKey is interface`() {
        val key = object : OptionKey {
            override val bundleKey: String
                get() = "test"
        }
        assertEquals("test", key.bundleKey)
    }
}

