package com.github.ahmedwelhakim.schematocode.core.emit

import com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import com.github.ahmedwelhakim.schematocode.core.resolve.SymbolTable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CodeEmitterTest {
    @Test
    fun `interface is implemented by mock`() {
        val emitter = object : CodeEmitter {
            override fun emit(
                plan: ModelPlan,
                config: GeneratorConfig
            ): String = "ok"
        }

        val plan = ModelPlan(
            units = emptyList(),
            root = TypeDef.AnyT,
            symbols = SymbolTable()
        )

        assertTrue(
            emitter.emit(plan, GeneratorConfig()).isNotEmpty()
        )
    }

    @Test
    fun `emitter receives correct plan`() {
        var receivedPlan: ModelPlan? = null

        val emitter = object : CodeEmitter {
            override fun emit(plan: ModelPlan, config: GeneratorConfig): String {
                receivedPlan = plan
                return "generated code"
            }
        }

        val plan = ModelPlan(
            units = emptyList(),
            root = TypeDef.ObjectT(emptyList()),
            symbols = SymbolTable()
        )

        val result = emitter.emit(plan, GeneratorConfig())

        assertEquals("generated code", result)
        assertEquals(plan, receivedPlan)
    }
}
