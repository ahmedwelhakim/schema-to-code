package com.github.ahmedwelhakim.schematocode.core.emit

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CodeEmitterTest {
    @Test
    fun `interface is implemented by mock`() {
        val emitter = object : CodeEmitter {
            override fun emit(
                plan: EmissionPlan,
                config: com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig
            ): String = "ok"
        }
        assertTrue(
            emitter.emit(
                EmissionPlan(emptyList()),
                com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig()
            ).isNotEmpty()
        )
    }
}

