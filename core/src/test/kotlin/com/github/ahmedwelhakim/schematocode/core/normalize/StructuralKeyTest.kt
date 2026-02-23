package com.github.ahmedwelhakim.schematocode.core.normalize

import com.github.ahmedwelhakim.schematocode.core.ir.Field
import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class StructuralKeyTest {

    @Test
    fun `primitive types produce equal keys for same scalar`() {
        val a = TypeDef.PrimitiveT(ScalarType.STRING)
        val b = TypeDef.PrimitiveT(ScalarType.STRING)
        assertEquals(a.structuralKey(), b.structuralKey())
    }

    @Test
    fun `different primitives produce different keys`() {
        val a = TypeDef.PrimitiveT(ScalarType.STRING)
        val b = TypeDef.PrimitiveT(ScalarType.INT)
        assertNotEquals(a.structuralKey(), b.structuralKey())
    }

    @Test
    fun `AnyT produces Any structural key`() {
        val key = TypeDef.AnyT.structuralKey()
        assertEquals(StructuralKey.Any, key)
    }

    @Test
    fun `array keys match when element type matches`() {
        val a = TypeDef.ArrayT(TypeDef.PrimitiveT(ScalarType.STRING))
        val b = TypeDef.ArrayT(TypeDef.PrimitiveT(ScalarType.STRING))
        assertEquals(a.structuralKey(), b.structuralKey())
    }

    @Test
    fun `array keys differ when element type differs`() {
        val a = TypeDef.ArrayT(TypeDef.PrimitiveT(ScalarType.STRING))
        val b = TypeDef.ArrayT(TypeDef.PrimitiveT(ScalarType.INT))
        assertNotEquals(a.structuralKey(), b.structuralKey())
    }

    @Test
    fun `objects with same fields produce equal keys regardless of field order`() {
        val obj1 = TypeDef.ObjectT(
            listOf(
                Field("a", TypeDef.PrimitiveT(ScalarType.STRING)),
                Field("b", TypeDef.PrimitiveT(ScalarType.INT))
            )
        )
        val obj2 = TypeDef.ObjectT(
            listOf(
                Field("b", TypeDef.PrimitiveT(ScalarType.INT)),
                Field("a", TypeDef.PrimitiveT(ScalarType.STRING))
            )
        )
        assertEquals(obj1.structuralKey(), obj2.structuralKey())
    }

    @Test
    fun `objects with different fields produce different keys`() {
        val obj1 = TypeDef.ObjectT(
            listOf(Field("a", TypeDef.PrimitiveT(ScalarType.STRING)))
        )
        val obj2 = TypeDef.ObjectT(
            listOf(Field("b", TypeDef.PrimitiveT(ScalarType.STRING)))
        )
        assertNotEquals(obj1.structuralKey(), obj2.structuralKey())
    }

    @Test
    fun `objects with same fields but different optionality produce different keys`() {
        val obj1 = TypeDef.ObjectT(
            listOf(Field("a", TypeDef.PrimitiveT(ScalarType.STRING), optional = false))
        )
        val obj2 = TypeDef.ObjectT(
            listOf(Field("a", TypeDef.PrimitiveT(ScalarType.STRING), optional = true))
        )
        assertNotEquals(obj1.structuralKey(), obj2.structuralKey())
    }

    @Test
    fun `union keys match when member types match`() {
        val u1 = TypeDef.UnionT(
            setOf(TypeDef.PrimitiveT(ScalarType.STRING), TypeDef.PrimitiveT(ScalarType.INT))
        )
        val u2 = TypeDef.UnionT(
            setOf(TypeDef.PrimitiveT(ScalarType.INT), TypeDef.PrimitiveT(ScalarType.STRING))
        )
        assertEquals(u1.structuralKey(), u2.structuralKey())
    }

    @Test
    fun `nested object structural key works`() {
        val inner = TypeDef.ObjectT(
            listOf(Field("x", TypeDef.PrimitiveT(ScalarType.INT)))
        )
        val outer1 = TypeDef.ObjectT(listOf(Field("inner", inner)))
        val outer2 = TypeDef.ObjectT(
            listOf(
                Field(
                    "inner",
                    TypeDef.ObjectT(listOf(Field("x", TypeDef.PrimitiveT(ScalarType.INT))))
                )
            )
        )
        assertEquals(outer1.structuralKey(), outer2.structuralKey())
    }
}

