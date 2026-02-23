package com.github.ahmedwelhakim.schematocode.core.i18n

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MessageKeyTest {

    @Test
    fun `all message keys have non-blank bundle keys`() {
        MessageKey.entries.forEach { key ->
            assertTrue(key.bundleKey.isNotBlank(), "bundleKey for $key should not be blank")
        }
    }

    @Test
    fun `all message keys implement MessageKeyHolder`() {
        MessageKey.entries.forEach { key ->
            assertTrue(key is MessageKeyHolder)
        }
    }

    @Test
    fun `specific keys have expected prefixes`() {
        assertTrue(MessageKey.TYPESCRIPT.bundleKey.startsWith("languages."))
        assertTrue(MessageKey.JSON.bundleKey.startsWith("languages."))
        assertTrue(MessageKey.INTERFACE.bundleKey.startsWith("modelKinds."))
        assertTrue(MessageKey.TYPE_ALIAS.bundleKey.startsWith("modelKinds."))
        assertTrue(MessageKey.PASCAL.bundleKey.startsWith("namingStrategies."))
        assertTrue(MessageKey.CAMEL.bundleKey.startsWith("namingStrategies."))
        assertTrue(MessageKey.SNAKE.bundleKey.startsWith("namingStrategies."))
        assertTrue(MessageKey.IDENTITY.bundleKey.startsWith("namingStrategies."))
    }

    @Test
    fun `MODEL_KIND has languageOptions prefix`() {
        assertTrue(MessageKey.MODEL_KIND.bundleKey.startsWith("languageOptions."))
    }

    @Test
    fun `MODEL_EMISSION_MODE has languageOptions prefix`() {
        assertTrue(MessageKey.MODEL_EMISSION_MODE.bundleKey.startsWith("languageOptions."))
    }

    @Test
    fun `valueOf round-trips correctly`() {
        MessageKey.entries.forEach { key ->
            assertEquals(key, MessageKey.valueOf(key.name))
        }
    }

    @Test
    fun `all bundle keys are unique`() {
        val keys = MessageKey.entries.map { it.bundleKey }
        assertEquals(keys.size, keys.toSet().size, "All bundle keys should be unique")
    }
}

