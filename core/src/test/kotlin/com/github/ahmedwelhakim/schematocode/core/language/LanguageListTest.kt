package com.github.ahmedwelhakim.schematocode.core.language

import com.github.ahmedwelhakim.schematocode.core.config.TargetLanguage
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LanguageListTest {

    @Test
    fun `languages list is not empty`() {
        assertTrue(LanguageList.languages.isNotEmpty())
    }

    @Test
    fun `languages list contains TypeScript`() {
        val tsDescriptor = LanguageList.languages.find {
            it.targetLanguage == TargetLanguage.TYPESCRIPT
        }
        assertNotNull(tsDescriptor)
    }

    @Test
    fun `all languages have valid target language`() {
        LanguageList.languages.forEach { descriptor ->
            assertNotNull(descriptor.targetLanguage)
        }
    }

    @Test
    fun `all languages can create default options`() {
        LanguageList.languages.forEach { descriptor ->
            assertNotNull(descriptor.defaultOptions())
        }
    }

    @Test
    fun `all languages return non-empty optionDefs`() {
        LanguageList.languages.forEach { descriptor ->
            assertNotNull(descriptor.optionDefs())
        }
    }
}

