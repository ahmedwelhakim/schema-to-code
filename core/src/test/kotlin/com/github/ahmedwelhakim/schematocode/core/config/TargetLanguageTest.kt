package com.github.ahmedwelhakim.schematocode.core.config

import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKey
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TargetLanguageTest {

    @Test
    fun `TYPESCRIPT has correct bundle key`() {
        assertEquals(MessageKey.TYPESCRIPT.bundleKey, TargetLanguage.TYPESCRIPT.bundleKey)
    }

    @Test
    fun `TYPESCRIPT is the only target language`() {
        val languages = TargetLanguage.entries
        assertEquals(1, languages.size)
        assertTrue(languages.contains(TargetLanguage.TYPESCRIPT))
    }

    @Test
    fun `valueOf returns correct language`() {
        assertEquals(TargetLanguage.TYPESCRIPT, TargetLanguage.valueOf("TYPESCRIPT"))
    }

    @Test
    fun `valueOf throws for invalid name`() {
        assertThrows(IllegalArgumentException::class.java) {
            TargetLanguage.valueOf("JAVA")
        }
    }
}

