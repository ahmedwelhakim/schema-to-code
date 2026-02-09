package com.github.ahmedwelhakim.schematocode.core.resolve

import com.github.ahmedwelhakim.schematocode.core.ir.Field
import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class SymbolTableTest {
    @Test
    fun `declare and nameOf returns correct name`() {
        val table = SymbolTable()
        val obj = TypeDef.ObjectT("Test", listOf(Field("a", TypeDef.PrimitiveT(ScalarType.STRING), false)))
        table.declare(obj, "Test")
        assertEquals("Test", table.nameOf(obj))
    }

    @Test
    fun `declare with duplicate name appends number`() {
        val table = SymbolTable()
        val obj1 = TypeDef.ObjectT("Test", listOf(Field("a", TypeDef.PrimitiveT(ScalarType.STRING), false)))
        val obj2 = TypeDef.ObjectT("Test", listOf(Field("b", TypeDef.PrimitiveT(ScalarType.NUMBER), false)))
        table.declare(obj1, "Test")
        table.declare(obj2, "Test")
        // The second should be Test2
        assertEquals("Test2", table.nameOf(obj2))
    }

    @Test
    fun `nameOf throws if not declared`() {
        val table = SymbolTable()
        val obj = TypeDef.ObjectT("Test", emptyList())
        assertThrows(IllegalStateException::class.java) {
            table.nameOf(obj)
        }
    }
}
