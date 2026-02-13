package com.github.ahmedwelhakim.schematocode.core.service

import com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig
import com.github.ahmedwelhakim.schematocode.core.emit.CodeEmitter
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SchemaToCodeServiceTest {
    @Test
    fun `generateFromJson returns string`() {
        val emitter = object : CodeEmitter {
            override fun emit(
                plan: com.github.ahmedwelhakim.schematocode.core.emit.ModelPlan,
                config: GeneratorConfig
            ): String = "code"
        }
        val result = SchemaToCodeService.generateFromJson("{}", "Root", emitter, GeneratorConfig())
        assertTrue(result.isNotEmpty())
    }
}

