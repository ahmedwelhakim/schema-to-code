package com.github.ahmedwelhakim.schematocode.core.normalize

import com.github.ahmedwelhakim.schematocode.core.ir.Field
import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ObjectMergerTest {
    @Test
    fun `mergeObjects merges fields with same name and marks optional correctly`() {
        val obj1 = TypeDef.ObjectT("Test", listOf(Field("a", TypeDef.PrimitiveT(ScalarType.STRING), false)))
        val obj2 = TypeDef.ObjectT(
            "Test",
            listOf(
                Field("a", TypeDef.PrimitiveT(ScalarType.STRING), false),
                Field("b", TypeDef.PrimitiveT(ScalarType.NUMBER), false)
            )
        )
        val merged = mergeObjects(listOf(obj1, obj2))
        assertEquals("Test", merged.name)
        assertEquals(2, merged.fields.size)
        val fieldA = merged.fields.find { it.name == "a" }!!
        val fieldB = merged.fields.find { it.name == "b" }!!
        assertEquals(false, fieldA.optional)
        assertEquals(true, fieldB.optional)
    }

    @Test
    fun `mergeObjects with single object returns same fields`() {
        val obj = TypeDef.ObjectT("Test", listOf(Field("a", TypeDef.PrimitiveT(ScalarType.STRING), false)))
        val merged = mergeObjects(listOf(obj))
        assertEquals(obj.fields, merged.fields)
    }
}
