package com.github.ahmedwelhakim.schematocode.core.emit

import com.github.ahmedwelhakim.schematocode.core.ir.Field
import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import com.github.ahmedwelhakim.schematocode.core.resolve.NameResolver
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class EmissionPlannerTest {

    @Test
    fun `plan with simple object returns single unit`() {
        val planner = EmissionPlanner(NameResolver())
        val obj = TypeDef.ObjectT(
            "User",
            listOf(
                Field("name", TypeDef.PrimitiveT(ScalarType.STRING), false),
                Field("age", TypeDef.PrimitiveT(ScalarType.NUMBER), false)
            )
        )

        val plan = planner.plan(obj, "User")

        assertEquals(1, plan.units.size)
        assertEquals("User", plan.units[0].name)
        assertEquals(obj, plan.units[0].type)
    }

    @Test
    fun `plan with nested objects creates multiple units`() {
        val planner = EmissionPlanner(NameResolver())
        val addressObj = TypeDef.ObjectT(
            "Address",
            listOf(
                Field("street", TypeDef.PrimitiveT(ScalarType.STRING), false),
                Field("city", TypeDef.PrimitiveT(ScalarType.STRING), false)
            )
        )
        val userObj = TypeDef.ObjectT(
            "User",
            listOf(
                Field("name", TypeDef.PrimitiveT(ScalarType.STRING), false),
                Field("address", addressObj, false)
            )
        )

        val plan = planner.plan(userObj, "User")

        assertEquals(2, plan.units.size)
        // Units are collected in depth-first order, so Address comes before User
        assertTrue(plan.units.any { it.name == "Address" })
        assertTrue(plan.units.any { it.name == "User" })
    }

    @Test
    fun `plan with array of objects includes nested object`() {
        val planner = EmissionPlanner(NameResolver())
        val itemObj = TypeDef.ObjectT(
            "Item",
            listOf(
                Field("id", TypeDef.PrimitiveT(ScalarType.NUMBER), false),
                Field("name", TypeDef.PrimitiveT(ScalarType.STRING), false)
            )
        )
        val listObj = TypeDef.ObjectT(
            "ItemList",
            listOf(
                Field("items", TypeDef.ArrayT(itemObj), false)
            )
        )

        val plan = planner.plan(listObj, "ItemList")

        assertEquals(2, plan.units.size)
        assertTrue(plan.units.any { it.name == "ItemsItem" })
        assertTrue(plan.units.any { it.name == "ItemList" })
    }

    @Test
    fun `plan with array of primitives returns single unit`() {
        val planner = EmissionPlanner(NameResolver())
        val obj = TypeDef.ObjectT(
            "TagList",
            listOf(
                Field("tags", TypeDef.ArrayT(TypeDef.PrimitiveT(ScalarType.STRING)), false)
            )
        )

        val plan = planner.plan(obj, "TagList")

        assertEquals(1, plan.units.size)
        assertEquals("TagList", plan.units[0].name)
    }

    @Test
    fun `plan with union of objects includes all union members`() {
        val planner = EmissionPlanner(NameResolver())
        val catObj = TypeDef.ObjectT(
            "cat",
            listOf(
                Field("meow", TypeDef.PrimitiveT(ScalarType.STRING), false)
            )
        )
        val dogObj = TypeDef.ObjectT(
            "dog",
            listOf(
                Field("bark", TypeDef.PrimitiveT(ScalarType.STRING), false)
            )
        )
        val petUnion = TypeDef.UnionT(setOf(catObj, dogObj))
        val ownerObj = TypeDef.ObjectT(
            "owner",
            listOf(
                Field("pet", petUnion, false)
            )
        )

        val plan = planner.plan(ownerObj, "Owner")

        assertEquals(3, plan.units.size)
        assertTrue(plan.units.any { it.name == "Pet" })
        assertTrue(plan.units.any { it.name == "Pet2" })
        assertTrue(plan.units.any { it.name == "Owner" })
    }

    @Test
    fun `plan with duplicate objects only includes them once`() {
        val planner = EmissionPlanner(NameResolver())
        val addressObj = TypeDef.ObjectT(
            "Address",
            listOf(
                Field("street", TypeDef.PrimitiveT(ScalarType.STRING), false)
            )
        )
        val userObj = TypeDef.ObjectT(
            "User",
            listOf(
                Field("homeAddress", addressObj, false),
                Field("workAddress", addressObj, false)
            )
        )

        val plan = planner.plan(userObj, "User")

        // Address should only appear once despite being referenced twice
        assertEquals(2, plan.units.size)
        assertEquals(1, plan.units.count { it.name == "Address" })
    }

    @Test
    fun `plan with deeply nested objects collects all levels`() {
        val planner = EmissionPlanner(NameResolver())
        val level3 = TypeDef.ObjectT(
            "Level3",
            listOf(Field("value", TypeDef.PrimitiveT(ScalarType.NUMBER), false))
        )
        val level2 = TypeDef.ObjectT(
            "Level2",
            listOf(Field("nested", level3, false))
        )
        val level1 = TypeDef.ObjectT(
            "Level1",
            listOf(Field("nested", level2, false))
        )

        val plan = planner.plan(level1, "Level1")

        assertEquals(3, plan.units.size)
        assertTrue(plan.units.any { it.name == "Level3" })
        assertTrue(plan.units.any { it.name == "Level2" })
        assertTrue(plan.units.any { it.name == "Level1" })
    }

    @Test
    fun `plan with only primitive fields returns single unit`() {
        val planner = EmissionPlanner(NameResolver())
        val obj = TypeDef.ObjectT(
            "Simple",
            listOf(
                Field("a", TypeDef.PrimitiveT(ScalarType.STRING), false),
                Field("b", TypeDef.PrimitiveT(ScalarType.NUMBER), false),
                Field("c", TypeDef.PrimitiveT(ScalarType.BOOLEAN), false)
            )
        )

        val plan = planner.plan(obj, "Simple")

        assertEquals(1, plan.units.size)
        assertEquals("Simple", plan.units[0].name)
    }

    @Test
    fun `plan with empty object returns single unit`() {
        val planner = EmissionPlanner(NameResolver())
        val obj = TypeDef.ObjectT("Empty", emptyList())

        val plan = planner.plan(obj, "Empty")

        assertEquals(1, plan.units.size)
        assertEquals("Empty", plan.units[0].name)
        assertEquals(0, plan.units[0].type.fields.size)
    }

    @Test
    fun `plan with primitive type returns empty plan`() {
        val planner = EmissionPlanner(NameResolver())
        val primitive = TypeDef.PrimitiveT(ScalarType.STRING)

        val plan = planner.plan(primitive, "Root")

        assertEquals(0, plan.units.size)
    }

    @Test
    fun `plan with array of primitives at root returns empty plan`() {
        val planner = EmissionPlanner(NameResolver())
        val arrayType = TypeDef.ArrayT(TypeDef.PrimitiveT(ScalarType.NUMBER))

        val plan = planner.plan(arrayType, "Root")

        assertEquals(0, plan.units.size)
    }

    @Test
    fun `plan with union of primitives returns empty plan`() {
        val planner = EmissionPlanner(NameResolver())
        val union = TypeDef.UnionT(
            setOf(
                TypeDef.PrimitiveT(ScalarType.STRING),
                TypeDef.PrimitiveT(ScalarType.NUMBER)
            )
        )

        val plan = planner.plan(union, "Root")

        assertEquals(0, plan.units.size)
    }

    @Test
    fun `plan with complex nested structure collects all objects`() {
        val planner = EmissionPlanner(NameResolver())

        // Create a complex structure with multiple levels and types
        val metadataObj = TypeDef.ObjectT(
            "Metadata",
            listOf(
                Field("created", TypeDef.PrimitiveT(ScalarType.STRING), false),
                Field("modified", TypeDef.PrimitiveT(ScalarType.STRING), false)
            )
        )

        val tagObj = TypeDef.ObjectT(
            "Tag",
            listOf(
                Field("name", TypeDef.PrimitiveT(ScalarType.STRING), false),
                Field("metadata", metadataObj, false)
            )
        )

        val authorObj = TypeDef.ObjectT(
            "Author",
            listOf(
                Field("name", TypeDef.PrimitiveT(ScalarType.STRING), false),
                Field("email", TypeDef.PrimitiveT(ScalarType.STRING), false)
            )
        )

        val postObj = TypeDef.ObjectT(
            "Post",
            listOf(
                Field("title", TypeDef.PrimitiveT(ScalarType.STRING), false),
                Field("author", authorObj, false),
                Field("tags", TypeDef.ArrayT(tagObj), false),
                Field("metadata", metadataObj, false)
            )
        )

        val plan = planner.plan(postObj, "Post")

        // Should have Post, Author, Tag, and Metadata (but Metadata only once)
        assertEquals(4, plan.units.size)
        assertTrue(plan.units.any { it.name == "Post" })
        assertTrue(plan.units.any { it.name == "Author" })
        assertTrue(plan.units.any { it.name == "Tag" })
        assertTrue(plan.units.any { it.name == "Metadata" })
        assertEquals(1, plan.units.count { it.name == "Metadata" })
    }

    @Test
    fun `plan with recursive-like structure deduplicates correctly`() {
        val planner = EmissionPlanner(NameResolver())

        val nodeObj = TypeDef.ObjectT(
            "Node",
            listOf(
                Field("value", TypeDef.PrimitiveT(ScalarType.NUMBER), false)
            )
        )

        val treeObj = TypeDef.ObjectT(
            "Tree",
            listOf(
                Field("left", nodeObj, false),
                Field("right", nodeObj, false),
                Field("root", nodeObj, false)
            )
        )

        val plan = planner.plan(treeObj, "Tree")

        assertEquals(2, plan.units.size)
        assertEquals(1, plan.units.count { it.name == "Node" })
        assertEquals(1, plan.units.count { it.name == "Tree" })
    }

    @Test
    fun `plan preserves unit order depth-first`() {
        val planner = EmissionPlanner(NameResolver())

        val innerObj = TypeDef.ObjectT(
            "Inner",
            listOf(Field("value", TypeDef.PrimitiveT(ScalarType.STRING), false))
        )

        val middleObj = TypeDef.ObjectT(
            "Middle",
            listOf(Field("inner", innerObj, false))
        )

        val outerObj = TypeDef.ObjectT(
            "Outer",
            listOf(Field("middle", middleObj, false))
        )

        val plan = planner.plan(outerObj, "Outer")

        // Units should be in depth-first order: Inner, Middle, Outer
        assertEquals(3, plan.units.size)
        assertEquals("Inner", plan.units[0].name)
        assertEquals("Middle", plan.units[1].name)
        assertEquals("Outer", plan.units[2].name)
    }

    @Test
    fun `plan with AnyT type returns empty plan`() {
        val planner = EmissionPlanner(NameResolver())

        val plan = planner.plan(TypeDef.AnyT, "Root")

        assertEquals(0, plan.units.size)
    }

    @Test
    fun `plan with object containing AnyT field still emits object`() {
        val planner = EmissionPlanner(NameResolver())
        val obj = TypeDef.ObjectT(
            "Container",
            listOf(
                Field("data", TypeDef.AnyT, false),
                Field("name", TypeDef.PrimitiveT(ScalarType.STRING), false)
            )
        )

        val plan = planner.plan(obj, "Container")

        assertEquals(1, plan.units.size)
        assertEquals("Container", plan.units[0].name)
    }

    @Test
    fun `plan uses NameResolver to assign unique names`() {
        val planner = EmissionPlanner(NameResolver())

        // Create two structurally different objects with the same name hint
        val obj1 = TypeDef.ObjectT(
            "Data",
            listOf(Field("a", TypeDef.PrimitiveT(ScalarType.STRING), false))
        )

        val obj2 = TypeDef.ObjectT(
            "Data",
            listOf(Field("b", TypeDef.PrimitiveT(ScalarType.NUMBER), false))
        )

        val containerObj = TypeDef.ObjectT(
            "Container",
            listOf(
                Field("first", obj1, false),
                Field("second", obj2, false)
            )
        )

        val plan = planner.plan(containerObj, "Container")

        assertEquals(3, plan.units.size)
        // Both Data objects should be present with different names
        val dataUnits = plan.units.filter { it.name.startsWith("Data") }
        assertEquals(2, dataUnits.size)
    }
}

