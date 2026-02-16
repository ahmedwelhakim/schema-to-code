package com.github.ahmedwelhakim.schematocode.plugin.state

import com.github.ahmedwelhakim.schematocode.core.config.TargetLanguage
import com.github.ahmedwelhakim.schematocode.core.emit.typescript.ModelEmissionMode
import com.github.ahmedwelhakim.schematocode.core.naming.NamingStrategyType

/**
 * Persistent state for Schema to Code plugin settings.
 *
 * This state is automatically persisted by IntelliJ's settings infrastructure
 * and restored when the project is reopened.
 *
 * @property targetLanguage The selected target language for code generation.
 * @property namingStrategy The naming strategy for transforming field names.
 * @property emissionMode The mode for emitting types (separate vs nested).
 * @property languageOptions Language-specific option values, keyed by "LANGUAGE:OPTION_KEY".
 */
data class SchemaToCodeState(
    var targetLanguage: TargetLanguage = TargetLanguage.TYPESCRIPT,
    var namingStrategy: NamingStrategyType = NamingStrategyType.IDENTITY,
    var emissionMode: ModelEmissionMode = ModelEmissionMode.SEPARATE,
    var languageOptions: Map<String, String> = emptyMap()

)