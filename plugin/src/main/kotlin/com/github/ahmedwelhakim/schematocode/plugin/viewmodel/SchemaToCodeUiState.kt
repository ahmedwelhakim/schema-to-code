package com.github.ahmedwelhakim.schematocode.plugin.viewmodel

import com.github.ahmedwelhakim.schematocode.core.config.ModelEmissionMode
import com.github.ahmedwelhakim.schematocode.core.config.TargetLanguage
import com.github.ahmedwelhakim.schematocode.core.naming.NamingStrategyType

data class SchemaToCodeUiState(
    val jsonInput: String = "",
    val output: String = "",
    val targetLanguage: TargetLanguage = TargetLanguage.TYPESCRIPT,
    val namingStrategy: NamingStrategyType = NamingStrategyType.PASCAL,
    val emissionMode: ModelEmissionMode = ModelEmissionMode.SEPARATE,
    val isLoading: Boolean = false,
    val error: String? = null
)
