package com.github.ahmedwelhakim.schematocode.core.normalize

import com.github.ahmedwelhakim.schematocode.core.ir.Field
import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ObjectMergerTest {

    @Test
    fun `merge single object returns same fields`() {
        val obj = TypeDef.ObjectT(
            listOf(
                Field("name", TypeDef.PrimitiveT(ScalarType.STRING)),
                Field("age", TypeDef.PrimitiveT(ScalarType.INT))
            )
        )
        val merged = mergeObjects(listOf(obj))
        assertEquals(2, merged.fields.size)
        assertFalse(merged.fields[0].optional)
        assertFalse(merged.fields[1].optional)
    }

    @Test
    fun `merge two objects marks missing fields as optional`() {
        val obj1 = TypeDef.ObjectT(
            listOf(
                Field("name", TypeDef.PrimitiveT(ScalarType.STRING)),
                Field("age", TypeDef.PrimitiveT(ScalarType.INT))
            )
        )
        val obj2 = TypeDef.ObjectT(
            listOf(
                Field("name", TypeDef.PrimitiveT(ScalarType.STRING)),
                Field("email", TypeDef.PrimitiveT(ScalarType.STRING))
            )
        )
        val merged = mergeObjects(listOf(obj1, obj2))

        assertEquals(3, merged.fields.size)

        val nameField = merged.fields.first { it.name == "name" }
        val ageField = merged.fields.first { it.name == "age" }
        val emailField = merged.fields.first { it.name == "email" }

        assertFalse(nameField.optional, "name appears in both, should not be optional")
        assertTrue(ageField.optional, "age appears in only obj1, should be optional")
        assertTrue(emailField.optional, "email appears in only obj2, should be optional")
    }

    @Test
    fun `merge objects with conflicting field types creates union`() {
        val obj1 = TypeDef.ObjectT(
            listOf(Field("value", TypeDef.PrimitiveT(ScalarType.STRING)))
        )
        val obj2 = TypeDef.ObjectT(
            listOf(Field("value", TypeDef.PrimitiveT(ScalarType.INT)))
        )
        val merged = mergeObjects(listOf(obj1, obj2))

        assertEquals(1, merged.fields.size)
        val valueField = merged.fields.first()
        assertEquals("value", valueField.name)
        assertFalse(valueField.optional)
        // The merged type should be a union of STRING and INT
        assertTrue(valueField.type is TypeDef.UnionT)
    }

    @Test
    fun `merge objects with identical field types deduplicates`() {
        val obj1 = TypeDef.ObjectT(
            listOf(Field("id", TypeDef.PrimitiveT(ScalarType.INT)))
        )
        val obj2 = TypeDef.ObjectT(
            listOf(Field("id", TypeDef.PrimitiveT(ScalarType.INT)))
        )
        val merged = mergeObjects(listOf(obj1, obj2))

        assertEquals(1, merged.fields.size)
        val idField = merged.fields.first()
        // Since both are structurally identical, should merge to a single type
        assertTrue(idField.type is TypeDef.PrimitiveT)
        assertEquals(ScalarType.INT, (idField.type as TypeDef.PrimitiveT).type)
    }

    @Test
    fun `merge empty list of objects returns object with no fields`() {
        val merged = mergeObjects(emptyList())
        assertTrue(merged.fields.isEmpty())
    }
}

