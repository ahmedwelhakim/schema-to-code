package com.github.ahmedwelhakim.schematocode.core.emit

import com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig

/**
 * Interface for code emitters that generate source code from a model plan.
 * Implementations handle specific target languages (TypeScript, Kotlin, etc.).
 */
interface CodeEmitter {
    /**
     * Emits source code from the given model plan.
     *
     * @param plan The model plan containing type declarations to emit.
     * @param config The generation configuration (naming strategy, emission mode, etc.).
     * @return The generated source code as a string.
     */
    fun emit(plan: ModelPlan, config: GeneratorConfig): String
}