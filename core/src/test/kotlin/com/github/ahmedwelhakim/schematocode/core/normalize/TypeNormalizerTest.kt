package com.github.ahmedwelhakim.schematocode.core.normalize

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TypeNormalizerTest {
    @Test
    fun `normalize returns TypeDef`() {
        val type = TypeDef.ObjectT("Test", emptyList())
        val normalized = TypeNormalizer.normalize(type)
        assertTrue(normalized is TypeDef)
    }
}

