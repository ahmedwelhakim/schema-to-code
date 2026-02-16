package com.github.ahmedwelhakim.schematocode.core.service

import com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig
import com.github.ahmedwelhakim.schematocode.core.config.InputFormat
import com.github.ahmedwelhakim.schematocode.core.emit.CodeEmitter
import com.github.ahmedwelhakim.schematocode.core.emit.ModelPlan
import com.github.ahmedwelhakim.schematocode.core.result.GenerationResult
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SchemaToCodeServiceTest {

    private val mockEmitter = object : CodeEmitter {
        override fun emit(plan: ModelPlan, config: GeneratorConfig): String {
            return "// Generated code for ${plan.units.size} types"
        }
    }

    @Test
    fun `generateFromJson returns string`() {
        val result = SchemaToCodeService.generateFromJson("{}", "Root", mockEmitter, GeneratorConfig())
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun `generateFromJson produces output for valid JSON object`() {
        val json = """{"name": "test"}"""

        val result = SchemaToCodeService.generateFromJson(
            json = json,
            rootName = "Root",
            emitter = mockEmitter,
            config = GeneratorConfig()
        )

        assertTrue(result.contains("Generated code"))
    }

    @Test
    fun `generateFromJsonSafe returns Success for valid JSON`() {
        val json = """{"name": "test"}"""

        val result = SchemaToCodeService.generateFromJsonSafe(
            json = json,
            rootName = "Root",
            emitter = mockEmitter,
            config = GeneratorConfig()
        )

        assertTrue(result is GenerationResult.Success)
    }

    @Test
    fun `generateFromJsonSafe returns Failure for invalid JSON`() {
        val json = "invalid json"

        val result = SchemaToCodeService.generateFromJsonSafe(
            json = json,
            rootName = "Root",
            emitter = mockEmitter,
            config = GeneratorConfig()
        )

        assertTrue(result is GenerationResult.Failure)
    }

    @Test
    fun `generate with InputFormat works for JSON`() {
        val json = """{"id": 1, "active": true}"""

        val result = SchemaToCodeService.generate(
            input = json,
            format = InputFormat.JSON,
            rootName = "Root",
            emitter = mockEmitter,
            config = GeneratorConfig()
        )

        assertTrue(result is GenerationResult.Success)
    }

    @Test
    fun `generate handles nested objects`() {
        val json = """
            {
                "user": {
                    "name": "John",
                    "address": {
                        "city": "NYC"
                    }
                }
            }
        """.trimIndent()

        val result = SchemaToCodeService.generate(
            input = json,
            format = InputFormat.JSON,
            rootName = "Root",
            emitter = mockEmitter,
            config = GeneratorConfig()
        )

        assertTrue(result is GenerationResult.Success)
    }

    @Test
    fun `generate handles arrays`() {
        val json = """
            {
                "items": [1, 2, 3],
                "names": ["a", "b"]
            }
        """.trimIndent()

        val result = SchemaToCodeService.generate(
            input = json,
            format = InputFormat.JSON,
            rootName = "Root",
            emitter = mockEmitter,
            config = GeneratorConfig()
        )

        assertTrue(result is GenerationResult.Success)
    }
}

