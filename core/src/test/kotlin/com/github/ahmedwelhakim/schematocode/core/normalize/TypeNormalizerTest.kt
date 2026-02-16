package com.github.ahmedwelhakim.schematocode.core.normalize

import com.github.ahmedwelhakim.schematocode.core.ir.Field
import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TypeNormalizerTest {
    @Test
    fun `normalize returns TypeDef for empty object`() {
        val type = TypeDef.ObjectT(emptyList())
        val normalized = TypeNormalizer.normalize(type)
        assertTrue(normalized is TypeDef.ObjectT)
    }

    @Test
    fun `normalize preserves primitive types`() {
        val type = TypeDef.PrimitiveT(ScalarType.STRING)
        val normalized = TypeNormalizer.normalize(type)
        assertEquals(type, normalized)
    }

    @Test
    fun `normalize preserves AnyT`() {
        val type = TypeDef.AnyT
        val normalized = TypeNormalizer.normalize(type)
        assertEquals(type, normalized)
    }

    @Test
    fun `normalize handles array types`() {
        val type = TypeDef.ArrayT(TypeDef.PrimitiveT(ScalarType.INT))
        val normalized = TypeNormalizer.normalize(type)
        assertTrue(normalized is TypeDef.ArrayT)
    }

    @Test
    fun `normalize handles object with fields`() {
        val type = TypeDef.ObjectT(
            listOf(
                Field("name", TypeDef.PrimitiveT(ScalarType.STRING)),
                Field("age", TypeDef.PrimitiveT(ScalarType.INT))
            )
        )
        val normalized = TypeNormalizer.normalize(type)
        assertTrue(normalized is TypeDef.ObjectT)
    }
}
