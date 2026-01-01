package com.github.ahmedwelhakim.schematocode.core.config

import com.github.ahmedwelhakim.schematocode.plugin.naming.*


data class GeneratorConfig(
    val namingStrategy: com.github.ahmedwelhakim.schematocode.core.naming.NamingStrategy = _root_ide_package_.com.github.ahmedwelhakim.schematocode.core.naming.IdentityNamingStrategy(),
    val inlineObjects: Boolean = true
)