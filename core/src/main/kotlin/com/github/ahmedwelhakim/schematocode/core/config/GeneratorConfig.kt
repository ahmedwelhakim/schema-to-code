package com.github.ahmedwelhakim.schematocode.core.config

import com.github.ahmedwelhakim.schematocode.core.naming.IdentityNamingStrategy
import com.github.ahmedwelhakim.schematocode.core.naming.NamingStrategy


data class GeneratorConfig(
    val namingStrategy: NamingStrategy = IdentityNamingStrategy(),
    val inlineObjects: Boolean = true
)