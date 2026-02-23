package com.github.ahmedwelhakim.schematocode.core.resolve

import com.github.ahmedwelhakim.schematocode.core.ir.Field
import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import com.github.ahmedwelhakim.schematocode.core.normalize.StructuralKey
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SymbolTableTest {

    @Test
    fun `declare and nameOf returns declared name`() {
        val table = SymbolTable()
        val obj = TypeDef.ObjectT(listOf(Field("x", TypeDef.PrimitiveT(ScalarType.INT))))
        val identity = TypeIdentity("User", StructuralKey.Primitive(ScalarType.INT))

        table.declare(obj, identity, "User")
        assertEquals("User", table.nameOf(obj))
    }

    @Test
    fun `declare duplicate name appends suffix`() {
        val table = SymbolTable()

        val obj1 = TypeDef.ObjectT(listOf(Field("a", TypeDef.PrimitiveT(ScalarType.STRING))))
        val id1 = TypeIdentity("Item", StructuralKey.Primitive(ScalarType.STRING))

        val obj2 = TypeDef.ObjectT(listOf(Field("b", TypeDef.PrimitiveT(ScalarType.INT))))
        val id2 = TypeIdentity("Item2", StructuralKey.Primitive(ScalarType.INT))

        table.declare(obj1, id1, "Item")
        table.declare(obj2, id2, "Item")

        assertEquals("Item", table.nameOf(obj1))
        assertEquals("Item2", table.nameOf(obj2))
    }

    @Test
    fun `typeIdentityOf returns declared identity`() {
        val table = SymbolTable()
        val obj = TypeDef.ObjectT(listOf(Field("x", TypeDef.PrimitiveT(ScalarType.INT))))
        val identity = TypeIdentity("Test", StructuralKey.Primitive(ScalarType.INT))

        table.declare(obj, identity, "Test")
        assertEquals(identity, table.typeIdentityOf(obj))
    }

    @Test
    fun `nameOf with TypeIdentity returns declared name`() {
        val table = SymbolTable()
        val obj = TypeDef.ObjectT(listOf(Field("x", TypeDef.PrimitiveT(ScalarType.INT))))
        val identity = TypeIdentity("Test", StructuralKey.Primitive(ScalarType.INT))

        table.declare(obj, identity, "Test")
        assertEquals("Test", table.nameOf(identity))
    }

    @Test
    fun `nameOf for undeclared type throws error`() {
        val table = SymbolTable()
        val obj = TypeDef.ObjectT(emptyList())

        assertThrows(IllegalStateException::class.java) {
            table.nameOf(obj)
        }
    }

    @Test
    fun `typeIdentityOf for undeclared type throws error`() {
        val table = SymbolTable()
        val obj = TypeDef.ObjectT(emptyList())

        assertThrows(IllegalStateException::class.java) {
            table.typeIdentityOf(obj)
        }
    }

    @Test
    fun `nameOf with undeclared TypeIdentity throws error`() {
        val table = SymbolTable()
        val identity = TypeIdentity("Missing", StructuralKey.Any)

        assertThrows(IllegalStateException::class.java) {
            table.nameOf(identity)
        }
    }

    @Test
    fun `declared names are PascalCased`() {
        val table = SymbolTable()
        val obj = TypeDef.ObjectT(listOf(Field("x", TypeDef.PrimitiveT(ScalarType.INT))))
        val identity = TypeIdentity("my_type", StructuralKey.Primitive(ScalarType.INT))

        table.declare(obj, identity, "my_type")
        assertEquals("MyType", table.nameOf(obj))
    }
}

