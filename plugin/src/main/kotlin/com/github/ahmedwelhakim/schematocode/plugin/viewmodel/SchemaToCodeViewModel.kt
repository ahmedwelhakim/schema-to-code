package com.github.ahmedwelhakim.schematocode.plugin.viewmodel

import com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig
import com.github.ahmedwelhakim.schematocode.core.config.ModelEmissionMode
import com.github.ahmedwelhakim.schematocode.core.config.TargetLanguage
import com.github.ahmedwelhakim.schematocode.core.emit.LanguageOptions
import com.github.ahmedwelhakim.schematocode.core.language.LanguageDescriptor
import com.github.ahmedwelhakim.schematocode.core.service.SchemaToCodeService
import com.github.ahmedwelhakim.schematocode.plugin.language.LanguageRegistry
import com.github.ahmedwelhakim.schematocode.plugin.state.SchemaToCodeSettingsService
import com.github.ahmedwelhakim.schematocode.plugin.ui.GenerationController
import com.intellij.openapi.project.Project
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SchemaToCodeViewModel(
    private val project: Project
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


    private val _state = MutableStateFlow(
        SchemaToCodeUiState(
            targetLanguage = settings.state.targetLanguage,
            namingStrategy = settings.state.namingStrategy,
            emissionMode = settings.state.emissionMode,
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
    )

    val state: StateFlow<SchemaToCodeUiState> = _state

    private var descriptor =
        LanguageRegistry.getLanguageDescriptor(_state.value.targetLanguage)

    private var options: LanguageOptions =
        descriptor.defaultOptions()

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

    fun onEmissionModeChanged(mode: ModelEmissionMode) {
        settings.state.emissionMode = mode
        updateState { copy(emissionMode = mode) }
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

        try {
            updateState { copy(isLoading = true, error = null) }

            val config = GeneratorConfig(
                namingStrategyType = current.namingStrategy,
                emissionMode = current.emissionMode
            )

            @Suppress("UNCHECKED_CAST")
            val emitter =
                (descriptor as LanguageDescriptor<LanguageOptions>)
                    .createEmitter(options)

            val result = SchemaToCodeService.generateFromJson(
                json = current.jsonInput,
                rootName = "Root",
                emitter = emitter,
                config = config
            )

            updateState {
                copy(
                    isLoading = false,
                    error = null
                )
            }
            return result

        } catch (e: Exception) {
            updateState {
                copy(

                    isLoading = false,
                    error = e.message
                )
            }
            return "// Error\n// ${e.message}"
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
