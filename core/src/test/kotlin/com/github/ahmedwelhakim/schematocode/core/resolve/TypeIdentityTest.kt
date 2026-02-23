package com.github.ahmedwelhakim.schematocode.core.resolve

import com.github.ahmedwelhakim.schematocode.core.normalize.StructuralKey
import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TypeIdentityTest {

    @Test
    fun `data class equality with same hint and structure`() {
        val a = TypeIdentity("User", StructuralKey.Primitive(ScalarType.STRING))
        val b = TypeIdentity("User", StructuralKey.Primitive(ScalarType.STRING))
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun `data class inequality with different hint`() {
        val a = TypeIdentity("User", StructuralKey.Primitive(ScalarType.STRING))
        val b = TypeIdentity("Person", StructuralKey.Primitive(ScalarType.STRING))
        assertNotEquals(a, b)
    }

    @Test
    fun `data class inequality with different structure`() {
        val a = TypeIdentity("User", StructuralKey.Primitive(ScalarType.STRING))
        val b = TypeIdentity("User", StructuralKey.Primitive(ScalarType.INT))
        assertNotEquals(a, b)
    }

    @Test
    fun `null hint is allowed`() {
        val id = TypeIdentity(null, StructuralKey.Any)
        assertNull(id.nameHint)
        assertEquals(StructuralKey.Any, id.structure)
    }
}

