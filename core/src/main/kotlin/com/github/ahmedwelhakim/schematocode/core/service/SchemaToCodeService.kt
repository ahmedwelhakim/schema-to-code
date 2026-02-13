package com.github.ahmedwelhakim.schematocode.core.service

import com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig
import com.github.ahmedwelhakim.schematocode.core.emit.CodeEmitter
import com.github.ahmedwelhakim.schematocode.core.emit.ModelPlanner
import com.github.ahmedwelhakim.schematocode.core.infer.json.inferFromJson
import com.github.ahmedwelhakim.schematocode.core.normalize.TypeNormalizer
import com.github.ahmedwelhakim.schematocode.core.resolve.TypeNameAllocator

object SchemaToCodeService {
    fun generateFromJson(
        json: String,
        rootName: String,
        emitter: CodeEmitter,
        config: GeneratorConfig
    ): String {

        val ir = inferFromJson(rootName, json)
        val normalized = TypeNormalizer.normalize(ir)
        val plan = ModelPlanner(TypeNameAllocator()).plan(normalized, rootName)

        return emitter.emit(plan, config)
    }
}