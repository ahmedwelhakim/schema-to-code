package com.github.ahmedwelhakim.schematocode.core.config

import com.github.ahmedwelhakim.schematocode.core.naming.NamingStrategyType


data class GeneratorConfig(
    var namingStrategyType: NamingStrategyType = NamingStrategyType.IDENTITY,
    var inlineObjects: Boolean = true,
    var name: String = "Root"
)