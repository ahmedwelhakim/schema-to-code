package com.github.ahmedwelhakim.schematocode.core.i18n

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MessageKeyTest {
    @Test
    fun `enum has correct bundleKey`() {
        assertEquals("languageOptions.modelKind", MessageKey.MODEL_KIND.bundleKey)
    }
}

