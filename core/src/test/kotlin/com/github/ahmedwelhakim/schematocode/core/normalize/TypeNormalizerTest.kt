package com.github.ahmedwelhakim.schematocode.core.normalize

import com.github.ahmedwelhakim.schematocode.core.ir.Field
import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TypeNormalizerTest {

    @Test
    fun `normalize primitive returns same primitive`() {
        val prim = TypeDef.PrimitiveT(ScalarType.STRING)
        val result = TypeNormalizer.normalize(prim)
        assertTrue(result is TypeDef.PrimitiveT)
        assertEquals(ScalarType.STRING, (result as TypeDef.PrimitiveT).type)
    }

    @Test
    fun `normalize AnyT returns AnyT`() {
        val result = TypeNormalizer.normalize(TypeDef.AnyT)
        assertSame(TypeDef.AnyT, result)
    }

    @Test
    fun `normalize array recursively normalizes element`() {
        val nested = TypeDef.ArrayT(
            TypeDef.UnionT(
                setOf(
                    TypeDef.PrimitiveT(ScalarType.STRING),
                    TypeDef.PrimitiveT(ScalarType.STRING) // duplicate
                )
            )
        )
        val result = TypeNormalizer.normalize(nested)
        assertTrue(result is TypeDef.ArrayT)
        val element = (result as TypeDef.ArrayT).element
        // Two structurally identical strings should merge to one
        assertTrue(element is TypeDef.PrimitiveT)
    }

    @Test
    fun `normalize object produces merged object`() {
        val obj = TypeDef.ObjectT(
            listOf(
                Field("name", TypeDef.PrimitiveT(ScalarType.STRING)),
                Field("age", TypeDef.PrimitiveT(ScalarType.INT))
            )
        )
        val result = TypeNormalizer.normalize(obj)
        assertTrue(result is TypeDef.ObjectT)
        assertEquals(2, (result as TypeDef.ObjectT).fields.size)
    }

    @Test
    fun `normalize union merges types`() {
        val union = TypeDef.UnionT(
            setOf(
                TypeDef.PrimitiveT(ScalarType.STRING),
                TypeDef.PrimitiveT(ScalarType.INT)
            )
        )
        val result = TypeNormalizer.normalize(union)
        assertTrue(result is TypeDef.UnionT)
        assertEquals(2, (result as TypeDef.UnionT).types.size)
    }

    @Test
    fun `normalize union with single type collapses`() {
        val union = TypeDef.UnionT(
            setOf(TypeDef.PrimitiveT(ScalarType.STRING))
        )
        val result = TypeNormalizer.normalize(union)
        assertTrue(result is TypeDef.PrimitiveT)
    }
}

