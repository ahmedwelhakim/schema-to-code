package com.github.ahmedwelhakim.schematocode.core.emit

import com.github.ahmedwelhakim.schematocode.core.ir.Field
import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import com.github.ahmedwelhakim.schematocode.core.resolve.TypeNameAllocator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ModelPlannerTest {

    @Test
    fun `plan simple object produces single declaration`() {
        val obj = TypeDef.ObjectT(
            listOf(Field("name", TypeDef.PrimitiveT(ScalarType.STRING)))
        )
        val planner = ModelPlanner(TypeNameAllocator())
        val plan = planner.plan(obj, "User")

        assertEquals(1, plan.units.size)
        assertEquals("User", plan.units[0].name)
        assertSame(obj, plan.root)
    }

    @Test
    fun `plan nested objects produces multiple declarations`() {
        val inner = TypeDef.ObjectT(
            listOf(Field("street", TypeDef.PrimitiveT(ScalarType.STRING)))
        )
        val outer = TypeDef.ObjectT(
            listOf(
                Field("name", TypeDef.PrimitiveT(ScalarType.STRING)),
                Field("address", inner)
            )
        )
        val planner = ModelPlanner(TypeNameAllocator())
        val plan = planner.plan(outer, "User")

        assertEquals(2, plan.units.size)
        val names = plan.units.map { it.name }.toSet()
        assertTrue(names.contains("User"))
        assertTrue(names.contains("Address"))
    }

    @Test
    fun `plan deduplicates identical object types`() {
        val shared = TypeDef.ObjectT(
            listOf(Field("x", TypeDef.PrimitiveT(ScalarType.INT)))
        )
        val root = TypeDef.ObjectT(
            listOf(
                Field("a", shared),
                Field("b", shared) // same instance
            )
        )
        val planner = ModelPlanner(TypeNameAllocator())
        val plan = planner.plan(root, "Root")

        // shared is the same instance so typeIdentity will be the same
        // root + shared = 2 declarations
        assertEquals(2, plan.units.size)
    }

    @Test
    fun `plan with array element visits element type`() {
        val elem = TypeDef.ObjectT(
            listOf(Field("id", TypeDef.PrimitiveT(ScalarType.INT)))
        )
        val root = TypeDef.ObjectT(
            listOf(Field("items", TypeDef.ArrayT(elem)))
        )
        val planner = ModelPlanner(TypeNameAllocator())
        val plan = planner.plan(root, "Root")

        val names = plan.units.map { it.name }.toSet()
        assertTrue(names.contains("Root"))
        assertTrue(names.contains("ItemsItem"))
    }

    @Test
    fun `plan with union visits all branches`() {
        val objA = TypeDef.ObjectT(
            listOf(Field("a", TypeDef.PrimitiveT(ScalarType.STRING)))
        )
        val objB = TypeDef.ObjectT(
            listOf(Field("b", TypeDef.PrimitiveT(ScalarType.INT)))
        )
        val root = TypeDef.ObjectT(
            listOf(Field("data", TypeDef.UnionT(setOf(objA, objB))))
        )
        val planner = ModelPlanner(TypeNameAllocator())
        val plan = planner.plan(root, "Root")

        // root + objA + objB = 3 declarations
        assertEquals(3, plan.units.size)
    }

    @Test
    fun `plan primitive root produces no declarations`() {
        val prim = TypeDef.PrimitiveT(ScalarType.STRING)
        val planner = ModelPlanner(TypeNameAllocator())
        val plan = planner.plan(prim, "Root")

        assertTrue(plan.units.isEmpty())
        assertSame(prim, plan.root)
    }

    @Test
    fun `plan empty object produces single empty declaration`() {
        val obj = TypeDef.ObjectT(emptyList())
        val planner = ModelPlanner(TypeNameAllocator())
        val plan = planner.plan(obj, "Empty")

        assertEquals(1, plan.units.size)
        assertEquals("Empty", plan.units[0].name)
        assertTrue(plan.units[0].type.fields.isEmpty())
    }
}

