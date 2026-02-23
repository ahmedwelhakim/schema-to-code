package com.github.ahmedwelhakim.schematocode.core.service

import com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig
import com.github.ahmedwelhakim.schematocode.core.config.InputFormat
import com.github.ahmedwelhakim.schematocode.core.emit.typescript.TypescriptEmitter
import com.github.ahmedwelhakim.schematocode.core.emit.typescript.TypescriptOptions
import com.github.ahmedwelhakim.schematocode.core.naming.NamingStrategyType
import com.github.ahmedwelhakim.schematocode.core.result.GenerationResult
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SchemaToCodeServiceTest {

    private val emitter = TypescriptEmitter(TypescriptOptions())
    private val config = GeneratorConfig()

    // ── generateFromJson ───────────────────────────────────────

    @Test
    fun `generateFromJson with simple object`() {
        val json = """{"name": "Alice", "age": 30}"""
        val code = SchemaToCodeService.generateFromJson(json, "User", emitter, config)

        assertTrue(code.contains("export interface User"))
        assertTrue(code.contains("name: string;"))
        assertTrue(code.contains("age: number;"))
    }

    @Test
    fun `generateFromJson with nested object`() {
        val json = """{"user": {"name": "Alice"}}"""
        val code = SchemaToCodeService.generateFromJson(json, "Root", emitter, config)

        assertTrue(code.contains("export interface Root"))
        assertTrue(code.contains("export interface User"))
    }

    @Test
    fun `generateFromJson with array`() {
        val json = """{"tags": ["a", "b"]}"""
        val code = SchemaToCodeService.generateFromJson(json, "Root", emitter, config)

        assertTrue(code.contains("tags: string[];"))
    }

    @Test
    fun `generateFromJson with naming strategy`() {
        val json = """{"user_name": "Alice"}"""
        val camelConfig = GeneratorConfig(namingStrategyType = NamingStrategyType.CAMEL, name = "Root")
        val code = SchemaToCodeService.generateFromJson(json, "Root", emitter, camelConfig)

        assertTrue(code.contains("userName: string;"))
    }

    // ── generateFromJsonSafe ───────────────────────────────────

    @Test
    fun `generateFromJsonSafe success`() {
        val json = """{"name": "Alice"}"""
        val result = SchemaToCodeService.generateFromJsonSafe(json, "User", emitter, config)

        assertTrue(result.isSuccess())
        assertNotNull(result.getOrNull())
        assertTrue(result.getOrNull()!!.contains("export interface User"))
    }

    @Test
    fun `generateFromJsonSafe with invalid JSON returns failure`() {
        val result = SchemaToCodeService.generateFromJsonSafe("{invalid}", "Root", emitter, config)

        assertTrue(result.isFailure())
        assertNull(result.getOrNull())
    }

    // ── generate (format-based) ────────────────────────────────

    @Test
    fun `generate with JSON format`() {
        val json = """{"id": 1}"""
        val result = SchemaToCodeService.generate(json, InputFormat.JSON, "Root", emitter, config)

        assertTrue(result.isSuccess())
        assertTrue(result.getOrNull()!!.contains("id: number;"))
    }

    @Test
    fun `generate with invalid input returns failure`() {
        val result = SchemaToCodeService.generate("not json", InputFormat.JSON, "Root", emitter, config)

        assertTrue(result.isFailure())
    }

    // ── End-to-end complex scenarios ───────────────────────────

    @Test
    fun `end-to-end complex JSON generates correct TypeScript`() {
        val json = """{
            "id": 1,
            "name": "Alice",
            "address": {
                "street": "123 Main St",
                "city": "Wonderland",
                "zip": 12345
            },
            "hobbies": ["reading", "coding"],
            "active": true
        }"""
        val code = SchemaToCodeService.generateFromJson(json, "User", emitter, config)

        assertTrue(code.contains("export interface User"))
        assertTrue(code.contains("export interface Address"))
        assertTrue(code.contains("id: number;"))
        assertTrue(code.contains("name: string;"))
        assertTrue(code.contains("address: Address;"))
        assertTrue(code.contains("hobbies: string[];"))
        assertTrue(code.contains("active: boolean;"))
        assertTrue(code.contains("street: string;"))
        assertTrue(code.contains("city: string;"))
        assertTrue(code.contains("zip: number;"))
    }

    @Test
    fun `end-to-end array of objects`() {
        val json = """[
            {"id": 1, "name": "Alice"},
            {"id": 2, "name": "Bob"}
        ]"""
        val result = SchemaToCodeService.generate(json, InputFormat.JSON, "User", emitter, config)

        assertTrue(result.isSuccess())
        val code = result.getOrNull()!!
        assertTrue(code.contains("export type User = UserItem[]") || code.contains("UserItem"))
    }
}

