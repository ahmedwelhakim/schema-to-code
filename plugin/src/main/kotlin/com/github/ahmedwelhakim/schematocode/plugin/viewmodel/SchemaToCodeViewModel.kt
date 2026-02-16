package com.github.ahmedwelhakim.schematocode.plugin.viewmodel

import com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig
import com.github.ahmedwelhakim.schematocode.core.config.TargetLanguage
import com.github.ahmedwelhakim.schematocode.core.emit.LanguageOptions
import com.github.ahmedwelhakim.schematocode.core.language.LanguageDescriptor
import com.github.ahmedwelhakim.schematocode.core.options.OptionKey
import com.github.ahmedwelhakim.schematocode.core.result.GenerationResult
import com.github.ahmedwelhakim.schematocode.core.service.SchemaToCodeService
import com.github.ahmedwelhakim.schematocode.plugin.language.LanguageRegistry
import com.github.ahmedwelhakim.schematocode.plugin.state.SchemaToCodeSettingsService
import com.github.ahmedwelhakim.schematocode.plugin.ui.GenerationController
import com.intellij.openapi.project.Project
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for the Schema to Code tool window.
 * Manages UI state and coordinates code generation.
 */
class SchemaToCodeViewModel(
    project: Project
) {
    val generationController: GenerationController

    init {
        generationController = GenerationController(
            {
                generate()
            }, {
                updateState { copy(output = it) }
            }
        )
    }

    private val settings =
        SchemaToCodeSettingsService.getInstance(project)


    private val _state: MutableStateFlow<SchemaToCodeUiState>

    val state: StateFlow<SchemaToCodeUiState>
    private var options: LanguageOptions
    private var descriptor: LanguageDescriptor<*>


    init {
        val initialState = SchemaToCodeUiState(
            targetLanguage = settings.state.targetLanguage,
            namingStrategy = settings.state.namingStrategy,
            languageOptions = settings.state.languageOptions,
            jsonInput = """
                {
  "title": "Example Schema",
  "type": "object",
  "properties": {
    "firstName": {
      "type": "string"
    },
    "lastName": {
      "type": "string"
    },
    "age": {
      "description": "Age in years",
      "type": "integer",
      "minimum": 0
    },
    "height": {
      "type": "number",
      "nullable": true
    },
    "favoriteFoods": {
      "type": "array",
      "minItems": 0,
      "maxItems": 2,
      "items": {
        "type": "string"
      }
    },
    "likesDogs": {
      "type": "boolean"
    }
  },
  "required": [
    "firstName",
    "lastName"
  ]
}
            """.trimIndent()
        )
        _state = MutableStateFlow(initialState)
        state = _state
        options = state.value.descriptor.parseOptionFromMap(settings.state.languageOptions)
//        updateState { copy(languageOptions = options) }
        descriptor = state.value.descriptor
    }


    // ============================================================
    // Public Actions
    // ============================================================

    fun onJsonChanged(json: String) {
        updateState { copy(jsonInput = json) }
        scheduleGeneration()
    }

    fun onLanguageChanged(language: TargetLanguage) {
        descriptor = LanguageRegistry.getLanguageDescriptor(language)
        options = descriptor.defaultOptions()

        settings.state.targetLanguage = language

        updateState { copy(targetLanguage = language) }
        scheduleGeneration()
    }

    fun onNamingChanged(strategy: com.github.ahmedwelhakim.schematocode.core.naming.NamingStrategyType) {
        settings.state.namingStrategy = strategy
        updateState { copy(namingStrategy = strategy) }
        scheduleGeneration()
    }


    fun onLanguageOptionChanged(key: OptionKey, value: Enum<*>) {
        updateState { withLanguageOption(key, value) }
        settings.state.languageOptions = state.value.languageOptions
        options = state.value.descriptor.parseOptionFromMap(settings.state.languageOptions)
        scheduleGeneration()
    }

    fun dispose() {
        generationController.dispose()
    }
    // ============================================================
    // Generation
    // ============================================================


    private fun generate(): String {
        val current = _state.value

        updateState { copy(isLoading = true, error = null) }

        val config = GeneratorConfig(
            namingStrategyType = current.namingStrategy,
        )

        @Suppress("UNCHECKED_CAST")
        val emitter =
            (descriptor as LanguageDescriptor<LanguageOptions>)
                .createEmitter(options)

        val result = SchemaToCodeService.generateFromJsonSafe(
            json = current.jsonInput,
            rootName = "Root",
            emitter = emitter,
            config = config
        )

        return when (result) {
            is GenerationResult.Success -> {
                updateState { copy(isLoading = false, error = null) }
                result.code
            }

            is GenerationResult.Failure -> {
                updateState { copy(isLoading = false, error = result.message) }
                "// Error: ${result.message}"
            }
        }
    }

    private fun scheduleGeneration() {
        generationController.schedule()
    }
    // ============================================================
    // Helper
    // ============================================================

    private fun updateState(
        reducer: SchemaToCodeUiState.() -> SchemaToCodeUiState
    ) {
        _state.update(reducer)
    }
}
