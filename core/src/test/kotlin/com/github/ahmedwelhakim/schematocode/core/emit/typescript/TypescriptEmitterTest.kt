package com.github.ahmedwelhakim.schematocode.core.emit.typescript

import com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig
import com.github.ahmedwelhakim.schematocode.core.emit.ModelPlanner
import com.github.ahmedwelhakim.schematocode.core.ir.Field
import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import com.github.ahmedwelhakim.schematocode.core.naming.NamingStrategyType
import com.github.ahmedwelhakim.schematocode.core.normalize.TypeNormalizer
import com.github.ahmedwelhakim.schematocode.core.resolve.TypeNameAllocator
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TypescriptEmitterTest {

    private fun emit(
        ir: TypeDef,
        rootName: String = "Root",
        modelKind: TypescriptModelKind = TypescriptModelKind.INTERFACE,
        emissionMode: ModelEmissionMode = ModelEmissionMode.SEPARATE,
        namingStrategy: NamingStrategyType = NamingStrategyType.IDENTITY
    ): String {
        val normalized = TypeNormalizer.normalize(ir)
        val options = TypescriptOptions(modelKind = modelKind, emissionMode = emissionMode)
        val emitter = TypescriptEmitter(options)
        val config = GeneratorConfig(namingStrategyType = namingStrategy, name = rootName)
        val plan = ModelPlanner(TypeNameAllocator()).plan(normalized, rootName)
        return emitter.emit(plan, config)
    }

    // ── Interface Mode (Separate) ──────────────────────────────

    @Test
    fun `emit simple interface`() {
        val obj = TypeDef.ObjectT(
            listOf(
                Field("name", TypeDef.PrimitiveT(ScalarType.STRING)),
                Field("age", TypeDef.PrimitiveT(ScalarType.INT))
            )
        )
        val code = emit(obj, "User")

        assertTrue(code.contains("export interface User"))
        assertTrue(code.contains("name: string;"))
        assertTrue(code.contains("age: number;"))
    }

    @Test
    fun `emit boolean field`() {
        val obj = TypeDef.ObjectT(
            listOf(Field("active", TypeDef.PrimitiveT(ScalarType.BOOLEAN)))
        )
        val code = emit(obj, "User")
        assertTrue(code.contains("active: boolean;"))
    }

    @Test
    fun `emit null field`() {
        val obj = TypeDef.ObjectT(
            listOf(Field("value", TypeDef.PrimitiveT(ScalarType.NULL)))
        )
        val code = emit(obj, "Root")
        assertTrue(code.contains("value: null;"))
    }

    @Test
    fun `emit double field as number`() {
        val obj = TypeDef.ObjectT(
            listOf(Field("price", TypeDef.PrimitiveT(ScalarType.DOUBLE)))
        )
        val code = emit(obj, "Product")
        assertTrue(code.contains("price: number;"))
    }

    @Test
    fun `emit any type`() {
        val obj = TypeDef.ObjectT(
            listOf(Field("data", TypeDef.AnyT))
        )
        val code = emit(obj, "Root")
        assertTrue(code.contains("data: any;"))
    }

    @Test
    fun `emit array type`() {
        val obj = TypeDef.ObjectT(
            listOf(Field("tags", TypeDef.ArrayT(TypeDef.PrimitiveT(ScalarType.STRING))))
        )
        val code = emit(obj, "Root")
        assertTrue(code.contains("tags: string[];"))
    }

    @Test
    fun `emit optional field`() {
        // Build via JSON array with heterogeneous objects so merging creates optional fields
        val json = """[{"name": "Alice", "email": "a@b.com"}, {"name": "Bob"}]"""
        val ir = com.github.ahmedwelhakim.schematocode.core.infer.json.inferFromJson("User", json)
        val normalized = TypeNormalizer.normalize(ir)
        val options = TypescriptOptions(
            modelKind = TypescriptModelKind.INTERFACE,
            emissionMode = ModelEmissionMode.SEPARATE
        )
        val emitter = TypescriptEmitter(options)
        val config = GeneratorConfig(namingStrategyType = NamingStrategyType.IDENTITY, name = "User")
        val plan = ModelPlanner(TypeNameAllocator()).plan(normalized, "User")
        val code = emitter.emit(plan, config)
        assertTrue(code.contains("email?: string;"))
    }

    @Test
    fun `emit nested objects as separate interfaces`() {
        val inner = TypeDef.ObjectT(
            listOf(Field("street", TypeDef.PrimitiveT(ScalarType.STRING)))
        )
        val outer = TypeDef.ObjectT(
            listOf(
                Field("name", TypeDef.PrimitiveT(ScalarType.STRING)),
                Field("address", inner)
            )
        )
        val code = emit(outer, "User")

        assertTrue(code.contains("export interface User"))
        assertTrue(code.contains("export interface Address"))
        assertTrue(code.contains("address: Address;"))
    }

    // ── Type Alias Mode ────────────────────────────────────────

    @Test
    fun `emit type alias instead of interface`() {
        val obj = TypeDef.ObjectT(
            listOf(Field("name", TypeDef.PrimitiveT(ScalarType.STRING)))
        )
        val code = emit(obj, "User", modelKind = TypescriptModelKind.TYPE_ALIAS)
        assertTrue(code.contains("export type User ="))
    }

    // ── Nested (Inline) Mode ───────────────────────────────────

    @Test
    fun `emit nested mode inlines nested objects`() {
        val inner = TypeDef.ObjectT(
            listOf(Field("street", TypeDef.PrimitiveT(ScalarType.STRING)))
        )
        val outer = TypeDef.ObjectT(
            listOf(
                Field("name", TypeDef.PrimitiveT(ScalarType.STRING)),
                Field("address", inner)
            )
        )
        val code = emit(outer, "User", emissionMode = ModelEmissionMode.NESTED)

        assertTrue(code.contains("export interface User"))
        // In nested mode, there should be no separate Address interface
        assertFalse(code.contains("export interface Address"))
    }

    @Test
    fun `emit nested mode with type alias`() {
        val obj = TypeDef.ObjectT(
            listOf(Field("x", TypeDef.PrimitiveT(ScalarType.INT)))
        )
        val code = emit(
            obj, "Point",
            modelKind = TypescriptModelKind.TYPE_ALIAS,
            emissionMode = ModelEmissionMode.NESTED
        )
        assertTrue(code.contains("export type Point ="))
    }

    @Test
    fun `emit nested mode with primitive root`() {
        val prim = TypeDef.PrimitiveT(ScalarType.STRING)
        val code = emit(prim, "Root", emissionMode = ModelEmissionMode.NESTED)
        assertTrue(code.contains("export type Root = string"))
    }

    // ── Naming Strategies ──────────────────────────────────────

    @Test
    fun `emit with camelCase naming strategy`() {
        val obj = TypeDef.ObjectT(
            listOf(Field("user_name", TypeDef.PrimitiveT(ScalarType.STRING)))
        )
        val code = emit(obj, "User", namingStrategy = NamingStrategyType.CAMEL)
        assertTrue(code.contains("userName: string;"))
    }

    @Test
    fun `emit with snake_case naming strategy`() {
        val obj = TypeDef.ObjectT(
            listOf(Field("userName", TypeDef.PrimitiveT(ScalarType.STRING)))
        )
        val code = emit(obj, "User", namingStrategy = NamingStrategyType.SNAKE)
        assertTrue(code.contains("user_name: string;"))
    }

    @Test
    fun `emit with identity naming preserves field names`() {
        val obj = TypeDef.ObjectT(
            listOf(Field("user_name", TypeDef.PrimitiveT(ScalarType.STRING)))
        )
        val code = emit(obj, "User", namingStrategy = NamingStrategyType.IDENTITY)
        assertTrue(code.contains("user_name: string;"))
    }

    // ── Invalid identifiers ────────────────────────────────────

    @Test
    fun `emit field with invalid identifier wraps in quotes`() {
        val obj = TypeDef.ObjectT(
            listOf(Field("my-field", TypeDef.PrimitiveT(ScalarType.STRING)))
        )
        val code = emit(obj, "Root", namingStrategy = NamingStrategyType.IDENTITY)
        assertTrue(code.contains("'my-field': string;"))
    }

    // ── Union types ────────────────────────────────────────────

    @Test
    fun `emit union type with pipe separator`() {
        val obj = TypeDef.ObjectT(
            listOf(
                Field(
                    "value",
                    TypeDef.UnionT(
                        setOf(
                            TypeDef.PrimitiveT(ScalarType.STRING),
                            TypeDef.PrimitiveT(ScalarType.INT)
                        )
                    )
                )
            )
        )
        val code = emit(obj, "Root")
        assertTrue(code.contains("string | number") || code.contains("number | string"))
    }

    // ── Non-object root in separate mode ───────────────────────

    @Test
    fun `emit primitive root in separate mode creates type alias`() {
        val arr = TypeDef.ArrayT(TypeDef.PrimitiveT(ScalarType.STRING))
        val code = emit(arr, "StringList")
        assertTrue(code.contains("export type StringList = string[]"))
    }

    // ── Empty object ───────────────────────────────────────────

    @Test
    fun `emit empty object produces empty body`() {
        val obj = TypeDef.ObjectT(emptyList())
        val code = emit(obj, "Empty")
        assertTrue(code.contains("export interface Empty"))
        assertTrue(code.contains("{"))
        assertTrue(code.contains("}"))
    }
}
