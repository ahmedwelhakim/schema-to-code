package com.github.ahmedwelhakim.schematocode.core.emit

import com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig

interface CodeEmitter {
    fun emit(plan: EmissionPlan, config: GeneratorConfig): String
}