//package com.github.ahmedwelhakim.schematocode.core.emit
//
//import com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig
//import org.junit.jupiter.api.Assertions.assertTrue
//import org.junit.jupiter.api.Test
//
//class CodeEmitterTest {
//    @Test
//    fun `interface is implemented by mock`() {
//        val emitter = object : CodeEmitter {
//            override fun emit(
//                plan: ModelPlan,
//                config: GeneratorConfig
//            ): String = "ok"
//        }
//        assertTrue(
//            emitter.emit(
//                ModelPlan(emptyList()),
//                GeneratorConfig()
//            ).isNotEmpty()
//        )
//    }
//}
//
