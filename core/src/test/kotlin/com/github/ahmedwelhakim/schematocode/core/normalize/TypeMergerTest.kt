//package com.github.ahmedwelhakim.schematocode.core.normalize
//
//import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
//import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
//import org.junit.jupiter.api.Assertions.assertTrue
//import org.junit.jupiter.api.Test
//
//class TypeMergerTest {
//    @Test
//    fun `mergeTypes returns AnyT for empty list`() {
//        val merged = mergeTypes(emptyList())
//        assertTrue(merged is TypeDef.AnyT)
//    }
//
//    @Test
//    fun `mergeTypes flattens unions`() {
//        val t1 = TypeDef.PrimitiveT(ScalarType.STRING)
//        val t2 = TypeDef.PrimitiveT(ScalarType.NUMBER)
//        val union = TypeDef.UnionT(setOf(t1, t2))
//        val merged = mergeTypes(listOf(union))
//        assertTrue(merged is TypeDef.UnionT)
//        assertTrue((merged as TypeDef.UnionT).types.containsAll(setOf(t1, t2)))
//    }
//
//    @Test
//    fun `mergeTypes merges arrays`() {
//        val arr1 = TypeDef.ArrayT(TypeDef.PrimitiveT(ScalarType.STRING))
//        val arr2 = TypeDef.ArrayT(TypeDef.PrimitiveT(ScalarType.NUMBER))
//        val merged = mergeTypes(listOf(arr1, arr2))
//        assertTrue(merged is TypeDef.ArrayT)
//    }
//
//    @Test
//    fun `mergeTypes merges objects`() {
//        val obj1 = TypeDef.ObjectT("Test", emptyList())
//        val obj2 = TypeDef.ObjectT("Test", emptyList())
//        val merged = mergeTypes(listOf(obj1, obj2))
//        assertTrue(merged is TypeDef.ObjectT)
//    }
//}
