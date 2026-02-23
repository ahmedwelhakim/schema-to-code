package com.github.ahmedwelhakim.schematocode.core.normalize

import com.github.ahmedwelhakim.schematocode.core.ir.Field
import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TypeMergerTest {

    @Test
    fun `merge empty list returns AnyT`() {
        val result = mergeTypes(emptyList())
        assertSame(TypeDef.AnyT, result)
    }

    @Test
    fun `merge single type returns that type`() {
        val t = TypeDef.PrimitiveT(ScalarType.STRING)
        val result = mergeTypes(listOf(t))
        assertTrue(result is TypeDef.PrimitiveT)
        assertEquals(ScalarType.STRING, (result as TypeDef.PrimitiveT).type)
    }

    @Test
    fun `merge identical primitives returns single primitive`() {
        val a = TypeDef.PrimitiveT(ScalarType.INT)
        val b = TypeDef.PrimitiveT(ScalarType.INT)
        val result = mergeTypes(listOf(a, b))
        assertTrue(result is TypeDef.PrimitiveT)
        assertEquals(ScalarType.INT, (result as TypeDef.PrimitiveT).type)
    }

    @Test
    fun `merge different primitives returns union`() {
        val a = TypeDef.PrimitiveT(ScalarType.STRING)
        val b = TypeDef.PrimitiveT(ScalarType.INT)
        val result = mergeTypes(listOf(a, b))
        assertTrue(result is TypeDef.UnionT)
        assertEquals(2, (result as TypeDef.UnionT).types.size)
    }

    @Test
    fun `merge flattens nested unions`() {
        val inner = TypeDef.UnionT(
            setOf(
                TypeDef.PrimitiveT(ScalarType.STRING),
                TypeDef.PrimitiveT(ScalarType.INT)
            )
        )
        val extra = TypeDef.PrimitiveT(ScalarType.BOOLEAN)
        val result = mergeTypes(listOf(inner, extra))
        assertTrue(result is TypeDef.UnionT)
        assertEquals(3, (result as TypeDef.UnionT).types.size)
    }

    @Test
    fun `merge all arrays produces single merged array`() {
        val a = TypeDef.ArrayT(TypeDef.PrimitiveT(ScalarType.STRING))
        val b = TypeDef.ArrayT(TypeDef.PrimitiveT(ScalarType.INT))
        val result = mergeTypes(listOf(a, b))
        assertTrue(result is TypeDef.ArrayT)
        // Element should be a union of STRING and INT
        val element = (result as TypeDef.ArrayT).element
        assertTrue(element is TypeDef.UnionT)
    }

    @Test
    fun `merge all objects produces single merged object`() {
        val obj1 = TypeDef.ObjectT(
            listOf(Field("name", TypeDef.PrimitiveT(ScalarType.STRING)))
        )
        val obj2 = TypeDef.ObjectT(
            listOf(
                Field("name", TypeDef.PrimitiveT(ScalarType.STRING)),
                Field("age", TypeDef.PrimitiveT(ScalarType.INT))
            )
        )
        val result = mergeTypes(listOf(obj1, obj2))
        assertTrue(result is TypeDef.ObjectT)
        val merged = result as TypeDef.ObjectT
        assertEquals(2, merged.fields.size)

        val ageField = merged.fields.first { it.name == "age" }
        assertTrue(ageField.optional)
    }

    @Test
    fun `merge more than 12 unique types collapses to AnyT`() {
        // Create 13 unique primitive-like types by wrapping in arrays with different elements
        val types = (0 until 13).map { i ->
            // Use different combinations to ensure structural uniqueness
            when (i) {
                0 -> TypeDef.PrimitiveT(ScalarType.STRING)
                1 -> TypeDef.PrimitiveT(ScalarType.INT)
                2 -> TypeDef.PrimitiveT(ScalarType.DOUBLE)
                3 -> TypeDef.PrimitiveT(ScalarType.BOOLEAN)
                4 -> TypeDef.PrimitiveT(ScalarType.NULL)
                5 -> TypeDef.AnyT
                6 -> TypeDef.ArrayT(TypeDef.PrimitiveT(ScalarType.STRING))
                7 -> TypeDef.ArrayT(TypeDef.PrimitiveT(ScalarType.INT))
                8 -> TypeDef.ArrayT(TypeDef.PrimitiveT(ScalarType.BOOLEAN))
                9 -> TypeDef.ArrayT(TypeDef.PrimitiveT(ScalarType.DOUBLE))
                10 -> TypeDef.ArrayT(TypeDef.PrimitiveT(ScalarType.NULL))
                11 -> TypeDef.ArrayT(TypeDef.AnyT)
                else -> TypeDef.ArrayT(TypeDef.ArrayT(TypeDef.PrimitiveT(ScalarType.STRING)))
            }
        }
        val result = mergeTypes(types)
        assertSame(TypeDef.AnyT, result)
    }

    @Test
    fun `merge mixed arrays and primitives returns union`() {
        val arr = TypeDef.ArrayT(TypeDef.PrimitiveT(ScalarType.STRING))
        val prim = TypeDef.PrimitiveT(ScalarType.INT)
        val result = mergeTypes(listOf(arr, prim))
        assertTrue(result is TypeDef.UnionT)
    }
}

