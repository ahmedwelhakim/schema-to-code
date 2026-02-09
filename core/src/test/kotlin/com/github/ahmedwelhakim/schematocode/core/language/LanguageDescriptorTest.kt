package com.github.ahmedwelhakim.schematocode.core.language

import com.github.ahmedwelhakim.schematocode.core.config.TargetLanguage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LanguageDescriptorTest {
    @Test
    fun `targetLanguage is correct`() {
        assertEquals(TargetLanguage.TYPESCRIPT, TypescriptLanguage.targetLanguage)
    }
}

