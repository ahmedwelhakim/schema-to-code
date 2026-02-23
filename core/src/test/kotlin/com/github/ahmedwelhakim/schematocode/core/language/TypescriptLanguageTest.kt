package com.github.ahmedwelhakim.schematocode.core.language

import com.github.ahmedwelhakim.schematocode.core.config.TargetLanguage
import com.github.ahmedwelhakim.schematocode.core.emit.typescript.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TypescriptLanguageTest {

    @Test
    fun `target language is TYPESCRIPT`() {
        assertEquals(TargetLanguage.TYPESCRIPT, TypescriptLanguage.targetLanguage)
    }

    @Test
    fun `defaultOptions returns TypescriptOptions with defaults`() {
        val opts = TypescriptLanguage.defaultOptions()
        assertEquals(TypescriptModelKind.INTERFACE, opts.modelKind)
        assertEquals(ModelEmissionMode.SEPARATE, opts.emissionMode)
    }

    @Test
    fun `createEmitter returns non-null emitter`() {
        val emitter = TypescriptLanguage.createEmitter(TypescriptOptions())
        assertNotNull(emitter)
    }

    @Test
    fun `optionDefs returns non-empty list`() {
        val defs = TypescriptLanguage.optionDefs()
        assertTrue(defs.isNotEmpty())
    }

    @Test
    fun `parseOptionKey with valid MODEL_KIND`() {
        val key = TypescriptLanguage.parseOptionKey("MODEL_KIND")
        assertEquals(TypescriptOptionKey.MODEL_KIND, key)
    }

    @Test
    fun `parseOptionKey with valid EMISSION_MODE`() {
        val key = TypescriptLanguage.parseOptionKey("EMISSION_MODE")
        assertEquals(TypescriptOptionKey.EMISSION_MODE, key)
    }

    @Test
    fun `parseOptionKey with null returns null`() {
        assertNull(TypescriptLanguage.parseOptionKey(null))
    }

    @Test
    fun `parseOptionKey with invalid name returns null`() {
        assertNull(TypescriptLanguage.parseOptionKey("INVALID_KEY"))
    }

    @Test
    fun `parseOptionValue for MODEL_KIND`() {
        val value = TypescriptLanguage.parseOptionValue(
            TypescriptOptionKey.MODEL_KIND, "TYPE_ALIAS"
        )
        assertEquals(TypescriptModelKind.TYPE_ALIAS, value)
    }

    @Test
    fun `parseOptionValue for EMISSION_MODE`() {
        val value = TypescriptLanguage.parseOptionValue(
            TypescriptOptionKey.EMISSION_MODE, "NESTED"
        )
        assertEquals(ModelEmissionMode.NESTED, value)
    }

    @Test
    fun `parseOptionValue with null value returns null`() {
        assertNull(TypescriptLanguage.parseOptionValue(TypescriptOptionKey.MODEL_KIND, null))
    }

    @Test
    fun `parseOptionValue with invalid value returns null`() {
        assertNull(TypescriptLanguage.parseOptionValue(TypescriptOptionKey.MODEL_KIND, "INVALID"))
    }

    @Test
    fun `parseOptionFromMap with model kind`() {
        val map = mapOf("TYPESCRIPT:MODEL_KIND" to "TYPE_ALIAS")
        val opts = TypescriptLanguage.parseOptionFromMap(map)
        assertEquals(TypescriptModelKind.TYPE_ALIAS, opts.modelKind)
    }

    @Test
    fun `parseOptionFromMap with emission mode`() {
        val map = mapOf("TYPESCRIPT:EMISSION_MODE" to "NESTED")
        val opts = TypescriptLanguage.parseOptionFromMap(map)
        assertEquals(ModelEmissionMode.NESTED, opts.emissionMode)
    }

    @Test
    fun `parseOptionFromMap with empty map returns defaults`() {
        val opts = TypescriptLanguage.parseOptionFromMap(emptyMap())
        assertEquals(TypescriptModelKind.INTERFACE, opts.modelKind)
        assertEquals(ModelEmissionMode.SEPARATE, opts.emissionMode)
    }

    @Test
    fun `parseOptionFromMap ignores invalid values`() {
        val map = mapOf("TYPESCRIPT:MODEL_KIND" to "INVALID")
        val opts = TypescriptLanguage.parseOptionFromMap(map)
        assertEquals(TypescriptModelKind.INTERFACE, opts.modelKind) // default
    }

    @Test
    fun `parseOptionFromMap with both options`() {
        val map = mapOf(
            "TYPESCRIPT:MODEL_KIND" to "TYPE_ALIAS",
            "TYPESCRIPT:EMISSION_MODE" to "NESTED"
        )
        val opts = TypescriptLanguage.parseOptionFromMap(map)
        assertEquals(TypescriptModelKind.TYPE_ALIAS, opts.modelKind)
        assertEquals(ModelEmissionMode.NESTED, opts.emissionMode)
    }
}

