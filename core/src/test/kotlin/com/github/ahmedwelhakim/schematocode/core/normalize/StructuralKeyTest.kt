//package com.github.ahmedwelhakim.schematocode.core.normalize
//
//import com.github.ahmedwelhakim.schematocode.core.ir.Field
//import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
//import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Test
//
//class StructuralKeyTest {
//    @Test
//    fun `structuralKey for primitive`() {
//        val t = TypeDef.PrimitiveT(ScalarType.STRING)
//        assertEquals("P:STRING", t.structuralKey())
//    }
//
//    @Test
//    fun `structuralKey for array`() {
//        val t = TypeDef.ArrayT(TypeDef.PrimitiveT(ScalarType.NUMBER))
//        assertEquals("A:P:NUMBER", t.structuralKey())
//    }
//
//    @Test
//    fun `structuralKey for object`() {
//        val t = TypeDef.ObjectT("Obj", listOf(Field("f", TypeDef.PrimitiveT(ScalarType.STRING), false)))
//        assertEquals("O:f:P:STRING:false", t.structuralKey())
//    }
//
//    @Test
//    fun `structuralKey for union`() {
//        val t = TypeDef.UnionT(setOf(TypeDef.PrimitiveT(ScalarType.STRING), TypeDef.PrimitiveT(ScalarType.NUMBER)))
//        assertEquals("U:P:NUMBER|P:STRING", t.structuralKey())
//    }
//
//    @Test
//    fun `structuralKey for AnyT`() {
//        assertEquals("ANY", TypeDef.AnyT.structuralKey())
//    }
//}
