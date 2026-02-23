package com.github.ahmedwelhakim.schematocode.core.ir

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TypeDefTest {

    @Test
    fun `AnyT is singleton`() {
        assertSame(TypeDef.AnyT, TypeDef.AnyT)
    }

    @Test
    fun `PrimitiveT holds scalar type`() {
        val p = TypeDef.PrimitiveT(ScalarType.STRING)
        assertEquals(ScalarType.STRING, p.type)
        assertNull(p.format)
    }

    @Test
    fun `PrimitiveT with format`() {
        val p = TypeDef.PrimitiveT(ScalarType.STRING, Format.UUID)
        assertEquals(ScalarType.STRING, p.type)
        assertEquals(Format.UUID, p.format)
    }

    @Test
    fun `PrimitiveT instances are not data classes - different identity`() {
        val a = TypeDef.PrimitiveT(ScalarType.STRING)
        val b = TypeDef.PrimitiveT(ScalarType.STRING)
        // They are not data classes, so equals uses reference identity
        assertNotEquals(a, b)
    }

    @Test
    fun `ArrayT holds element type`() {
        val element = TypeDef.PrimitiveT(ScalarType.INT)
        val arr = TypeDef.ArrayT(element)
        assertSame(element, arr.element)
    }

    @Test
    fun `ObjectT holds fields`() {
        val fields = listOf(
            Field("name", TypeDef.PrimitiveT(ScalarType.STRING)),
            Field("age", TypeDef.PrimitiveT(ScalarType.INT))
        )
        val obj = TypeDef.ObjectT(fields)
        assertEquals(2, obj.fields.size)
        assertEquals("name", obj.fields[0].name)
        assertEquals("age", obj.fields[1].name)
    }

    @Test
    fun `UnionT holds set of types`() {
        val types = setOf(
            TypeDef.PrimitiveT(ScalarType.STRING),
            TypeDef.PrimitiveT(ScalarType.INT)
        )
        val union = TypeDef.UnionT(types)
        assertEquals(2, union.types.size)
    }

    @Test
    fun `all TypeDef variants are instances of TypeDef`() {
        val variants: List<TypeDef> = listOf(
            TypeDef.AnyT,
            TypeDef.PrimitiveT(ScalarType.STRING),
            TypeDef.ArrayT(TypeDef.AnyT),
            TypeDef.ObjectT(emptyList()),
            TypeDef.UnionT(emptySet())
        )
        variants.forEach { assertTrue(it is TypeDef) }
    }
}

