package com.github.ahmedwelhakim.schematocode.core.config

import com.github.ahmedwelhakim.schematocode.core.naming.NamingStrategyType

/**
 * Configuration for code generation.
 *
 * @property namingStrategyType The naming strategy for field and type names.
 * @property emissionMode Whether to emit types as separate declarations or nested inline.
 * @property name The name for the root type.
 */
data class GeneratorConfig(
    var namingStrategyType: NamingStrategyType = NamingStrategyType.IDENTITY,
    var emissionMode: ModelEmissionMode = ModelEmissionMode.SEPARATE,
    var name: String = "Root"
)