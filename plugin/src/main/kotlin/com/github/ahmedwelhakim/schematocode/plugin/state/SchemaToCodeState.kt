package com.github.ahmedwelhakim.schematocode.plugin.state


import com.github.ahmedwelhakim.schematocode.core.config.ModelEmissionMode
import com.github.ahmedwelhakim.schematocode.core.config.TargetLanguage
import com.github.ahmedwelhakim.schematocode.core.naming.NamingStrategyType

data class SchemaToCodeState(
    var targetLanguage: TargetLanguage = TargetLanguage.TYPESCRIPT,
    var namingStrategy: NamingStrategyType = NamingStrategyType.IDENTITY,
    var emissionMode: ModelEmissionMode = ModelEmissionMode.SEPARATE,
    var languageOptions: Map<String, String> = emptyMap()

)