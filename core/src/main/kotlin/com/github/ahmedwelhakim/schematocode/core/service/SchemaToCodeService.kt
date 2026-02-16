package com.github.ahmedwelhakim.schematocode.core.service

import com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig
import com.github.ahmedwelhakim.schematocode.core.config.InputFormat
import com.github.ahmedwelhakim.schematocode.core.emit.CodeEmitter
import com.github.ahmedwelhakim.schematocode.core.emit.ModelPlanner
import com.github.ahmedwelhakim.schematocode.core.infer.InputParserRegistry
import com.github.ahmedwelhakim.schematocode.core.infer.json.inferFromJson
import com.github.ahmedwelhakim.schematocode.core.normalize.TypeNormalizer
import com.github.ahmedwelhakim.schematocode.core.resolve.TypeNameAllocator
import com.github.ahmedwelhakim.schematocode.core.result.GenerationResult
import com.github.ahmedwelhakim.schematocode.core.service.SchemaToCodeService.generateFromJsonSafe

/**
 * Main service for converting schemas to code.
 * Orchestrates the transformation pipeline: Parse → Normalize → Plan → Emit.
 */
object SchemaToCodeService {

    /**
     * Generates code from JSON input.
     *
     * @param json The JSON string to convert.
     * @param rootName The name for the root type.
     * @param emitter The code emitter for the target language.
     * @param config The generation configuration.
     * @return The generated code string.
     * @deprecated Use [generateFromJsonSafe] for proper error handling.
     */
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

    /**
     * Generates code from JSON input with proper error handling.
     *
     * @param json The JSON string to convert.
     * @param rootName The name for the root type.
     * @param emitter The code emitter for the target language.
     * @param config The generation configuration.
     * @return A [GenerationResult] containing either the generated code or error details.
     */
    fun generateFromJsonSafe(
        json: String,
        rootName: String,
        emitter: CodeEmitter,
        config: GeneratorConfig
    ): GenerationResult = GenerationResult.runCatching {
        generateFromJson(json, rootName, emitter, config)
    }

    /**
     * Generates code from input in the specified format.
     *
     * @param input The input string to convert.
     * @param format The format of the input (JSON, etc.).
     * @param rootName The name for the root type.
     * @param emitter The code emitter for the target language.
     * @param config The generation configuration.
     * @return A [GenerationResult] containing either the generated code or error details.
     */
    fun generate(
        input: String,
        format: InputFormat,
        rootName: String,
        emitter: CodeEmitter,
        config: GeneratorConfig
    ): GenerationResult = GenerationResult.runCatching {
        val parser = InputParserRegistry.getParser(format)
        val ir = parser.parse(input, rootName)
        val normalized = TypeNormalizer.normalize(ir)
        val plan = ModelPlanner(TypeNameAllocator()).plan(normalized, rootName)

        emitter.emit(plan, config)
    }
}