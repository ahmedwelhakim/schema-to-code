package com.github.ahmedwelhakim.schematocode.core.resolve

import com.github.ahmedwelhakim.schematocode.core.ir.Field
import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StructuralKeyTest {
    @Test
    fun `structuralKey for primitive type`() {
        val t = TypeDef.PrimitiveT(ScalarType.STRING)
        val key = t.structuralKey()
        assertEquals(StructuralKey.Primitive(ScalarType.STRING), key)
    }

    @Test
    fun `structuralKey for array type`() {
        val t = TypeDef.ArrayT(TypeDef.PrimitiveT(ScalarType.NUMBER))
        val key = t.structuralKey()
        assertEquals(StructuralKey.Array(StructuralKey.Primitive(ScalarType.NUMBER)), key)
    }

    @Test
    fun `structuralKey for object type`() {
        val t = TypeDef.ObjectT("Obj", listOf(Field("f", TypeDef.PrimitiveT(ScalarType.STRING), false)))
        val key = t.structuralKey()
        val expected = StructuralKey.Object(
            listOf(FieldKey("f", StructuralKey.Primitive(ScalarType.STRING), false))
        )
        assertEquals(expected, key)
    }

    @Test
    fun `structuralKey for union type`() {
        val t = TypeDef.UnionT(setOf(TypeDef.PrimitiveT(ScalarType.STRING), TypeDef.PrimitiveT(ScalarType.NUMBER)))
        val key = t.structuralKey()
        val expected = StructuralKey.Union(
            setOf(
                StructuralKey.Primitive(ScalarType.STRING),
                StructuralKey.Primitive(ScalarType.NUMBER)
            )
        )
        assertEquals(expected, key)
    }

    @Test
    fun `structuralKey for AnyT`() {
        val key = TypeDef.AnyT.structuralKey()
        assertEquals(StructuralKey.Any, key)
    }
}
