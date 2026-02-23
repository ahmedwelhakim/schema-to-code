package com.github.ahmedwelhakim.schematocode.core.resolve

import com.github.ahmedwelhakim.schematocode.core.ir.Field
import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TypeNameAllocatorTest {

    @Test
    fun `resolve simple object assigns root name`() {
        val allocator = TypeNameAllocator()
        val obj = TypeDef.ObjectT(
            listOf(Field("name", TypeDef.PrimitiveT(ScalarType.STRING)))
        )
        val resolved = allocator.resolve(obj, "User")

        assertSame(obj, resolved.root)
        assertEquals("User", resolved.symbols.nameOf(obj))
    }

    @Test
    fun `resolve nested objects assigns names from field hints`() {
        val inner = TypeDef.ObjectT(
            listOf(Field("street", TypeDef.PrimitiveT(ScalarType.STRING)))
        )
        val outer = TypeDef.ObjectT(
            listOf(
                Field("name", TypeDef.PrimitiveT(ScalarType.STRING)),
                Field("address", inner)
            )
        )
        val allocator = TypeNameAllocator()
        val resolved = allocator.resolve(outer, "User")

        assertEquals("User", resolved.symbols.nameOf(outer))
        assertEquals("Address", resolved.symbols.nameOf(inner))
    }

    @Test
    fun `resolve array visits element type`() {
        val inner = TypeDef.ObjectT(
            listOf(Field("id", TypeDef.PrimitiveT(ScalarType.INT)))
        )
        val root = TypeDef.ObjectT(
            listOf(Field("items", TypeDef.ArrayT(inner)))
        )
        val allocator = TypeNameAllocator()
        val resolved = allocator.resolve(root, "Root")

        assertEquals("Root", resolved.symbols.nameOf(root))
        assertEquals("ItemsItem", resolved.symbols.nameOf(inner))
    }

    @Test
    fun `resolve union visits all branches`() {
        val objA = TypeDef.ObjectT(
            listOf(Field("a", TypeDef.PrimitiveT(ScalarType.STRING)))
        )
        val objB = TypeDef.ObjectT(
            listOf(Field("b", TypeDef.PrimitiveT(ScalarType.INT)))
        )
        val union = TypeDef.UnionT(setOf(objA, objB))
        val root = TypeDef.ObjectT(
            listOf(Field("data", union))
        )
        val allocator = TypeNameAllocator()
        val resolved = allocator.resolve(root, "Root")

        assertEquals("Root", resolved.symbols.nameOf(root))
        // Both union branches should have been declared
        assertDoesNotThrow { resolved.symbols.nameOf(objA) }
        assertDoesNotThrow { resolved.symbols.nameOf(objB) }
    }

    @Test
    fun `resolve with null rootName uses null hint`() {
        val obj = TypeDef.ObjectT(
            listOf(Field("x", TypeDef.PrimitiveT(ScalarType.INT)))
        )
        val allocator = TypeNameAllocator()
        val resolved = allocator.resolve(obj, null)

        // Should declare with "Anonymous" fallback
        assertDoesNotThrow { resolved.symbols.nameOf(obj) }
    }

    @Test
    fun `resolve primitive root does not throw`() {
        val prim = TypeDef.PrimitiveT(ScalarType.STRING)
        val allocator = TypeNameAllocator()
        val resolved = allocator.resolve(prim, "Root")

        assertSame(prim, resolved.root)
    }
}

