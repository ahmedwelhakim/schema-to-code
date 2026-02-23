package com.github.ahmedwelhakim.schematocode.core.emit.typescript

import com.github.ahmedwelhakim.schematocode.core.options.OptionKey
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TypescriptOptionsTest {

    @Test
    fun `default options have INTERFACE model kind`() {
        val opts = TypescriptOptions()
        assertEquals(TypescriptModelKind.INTERFACE, opts.modelKind)
    }

    @Test
    fun `default options have SEPARATE emission mode`() {
        val opts = TypescriptOptions()
        assertEquals(ModelEmissionMode.SEPARATE, opts.emissionMode)
    }

    @Test
    fun `get MODEL_KIND returns model kind`() {
        val opts = TypescriptOptions(modelKind = TypescriptModelKind.TYPE_ALIAS)
        assertEquals(TypescriptModelKind.TYPE_ALIAS, opts.get<TypescriptModelKind>(TypescriptOptionKey.MODEL_KIND))
    }

    @Test
    fun `get unknown key returns null`() {
        val opts = TypescriptOptions()
        val unknownKey = object : OptionKey {
            override val bundleKey: String = "unknown"
        }
        assertNull(opts.get<Any>(unknownKey))
    }

    @Test
    fun `with MODEL_KIND returns new options instance`() {
        val original = TypescriptOptions(modelKind = TypescriptModelKind.INTERFACE)
        val updated = original.with(TypescriptOptionKey.MODEL_KIND, TypescriptModelKind.TYPE_ALIAS)

        assertTrue(updated is TypescriptOptions)
        assertEquals(TypescriptModelKind.TYPE_ALIAS, (updated as TypescriptOptions).modelKind)
        // Original should be unchanged
        assertEquals(TypescriptModelKind.INTERFACE, original.modelKind)
    }

    @Test
    fun `with unknown key returns same options`() {
        val original = TypescriptOptions()
        val unknownKey = object : OptionKey {
            override val bundleKey: String = "unknown"
        }
        val result = original.with(unknownKey, "value")
        assertSame(original, result)
    }

    @Test
    fun `copy works correctly`() {
        val opts = TypescriptOptions(
            modelKind = TypescriptModelKind.INTERFACE,
            emissionMode = ModelEmissionMode.SEPARATE
        )
        val copied = opts.copy(modelKind = TypescriptModelKind.TYPE_ALIAS)
        assertEquals(TypescriptModelKind.TYPE_ALIAS, copied.modelKind)
        assertEquals(ModelEmissionMode.SEPARATE, copied.emissionMode)
    }
}

