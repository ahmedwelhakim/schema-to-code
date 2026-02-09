package com.github.ahmedwelhakim.schematocode.plugin.ui

import com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig
import com.github.ahmedwelhakim.schematocode.core.config.TargetLanguage
import com.github.ahmedwelhakim.schematocode.core.emit.LanguageOptions
import com.github.ahmedwelhakim.schematocode.core.language.LanguageDescriptor
import com.github.ahmedwelhakim.schematocode.core.language.TypescriptLanguage
import com.github.ahmedwelhakim.schematocode.core.naming.NamingStrategyType
import com.github.ahmedwelhakim.schematocode.core.options.BooleanOption
import com.github.ahmedwelhakim.schematocode.core.options.EnumOption
import com.github.ahmedwelhakim.schematocode.core.options.OptionDef
import com.github.ahmedwelhakim.schematocode.core.service.SchemaToCodeService
import com.github.ahmedwelhakim.schematocode.plugin.language.LanguageId
import com.github.ahmedwelhakim.schematocode.plugin.language.LanguageRegistry
import com.github.ahmedwelhakim.schematocode.plugin.language.getLanguageID
import com.github.ahmedwelhakim.schematocode.plugin.util.withEnumTranslation
import com.intellij.lang.Language
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.ui.Splitter
import com.intellij.ui.LanguageTextField
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.panel
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JScrollPane

class SchemaToCodeToolWindow(private val project: Project) : SimpleToolWindowPanel(true, true) {

    private val inputEditor = createEditor(
        LanguageId.JSON,
    )

    private val outputEditorContainer = JPanel(BorderLayout())
    private lateinit var outputEditor: LanguageTextField

    private var currentDescriptor: LanguageDescriptor<*> = TypescriptLanguage
    private var currentOptions: LanguageOptions = currentDescriptor.defaultOptions()
    private val generatorConfig = GeneratorConfig()
    private val optionsPanel = JPanel(BorderLayout())

    init {
        inputEditor.text = """
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
        setContent(buildUi())
        rebuildOptionsPanel()
        rebuildOutputEditor(currentDescriptor.targetLanguage)
    }

    private fun buildUi(): JComponent = panel {

        row {
            comboBox(TargetLanguage.entries)
                .label("Language:")
                .withEnumTranslation { it.bundleKey }
                .bindItem(
                    { currentDescriptor.targetLanguage },
                    {
                        currentDescriptor = LanguageRegistry.getLanguageDescriptor(it!!)
                        currentOptions = currentDescriptor.defaultOptions()

                        rebuildOptionsPanel()
                        rebuildOutputEditor(it)
                        regenerate()
                    }
                )
            comboBox(NamingStrategyType.entries)
                .label("Naming Strategy")
                .applyToComponent {
                    selectedItem = generatorConfig.namingStrategyType
                }
                .onChanged {
                    generatorConfig.namingStrategyType = it.selectedItem!! as NamingStrategyType
                }
                .withEnumTranslation { it.bundleKey }

            checkBox("Inline")
                .applyToComponent {
                    isSelected = generatorConfig.inlineObjects
                }
                .onChanged {
                    generatorConfig.inlineObjects = it.isSelected
                }
        }

        row {
            cell(
                Splitter(false, 0.5f, 0.1f, 0.9f).apply {
                    firstComponent = JScrollPane(inputEditor).apply {
                        verticalScrollBar.unitIncrement = 12
                        horizontalScrollBar.unitIncrement = 12

                    }
                    secondComponent = JScrollPane(outputEditorContainer).apply {
                        verticalScrollBar.unitIncrement = 12
                        horizontalScrollBar.unitIncrement = 12

                    }
                }
            ).align(Align.FILL)
        }.resizableRow()

        row {
            cell(optionsPanel).align(Align.FILL)
        }

        row {
            button("Generate") {
                regenerate()
            }
        }
    }

    private fun buildOptionsPanel(
        defs: List<OptionDef<*>>,
        onChange: () -> Unit
    ): JComponent = panel {
        defs.forEach { def ->
            when (def) {
                is EnumOption<*> -> row {
                    comboBox(def.values.toList())
                        .withEnumTranslation { it.bundleKey }
                        .applyToComponent {
                            selectedItem =
                                currentOptions.get(def.key) ?: def.default
                        }
                        .onChanged {
                            val selectedItem = it.selectedItem
                            currentOptions =
                                if (selectedItem != null)
                                    currentOptions.with(def.key, selectedItem)
                                else currentOptions
                            onChange()
                        }
                }

                is BooleanOption -> row {
                    checkBox(def.key.toString())
                        .onChanged { onChange() }
                        .applyToComponent {
                            isSelected = currentOptions.get(def.key) ?: def.default
                        }.onChanged {
                            currentOptions = currentOptions.with(def.key, it.isSelected)
                            onChange()
                        }
                }
            }
        }
    }


    private fun rebuildOptionsPanel() {
        optionsPanel.removeAll()


        optionsPanel.add(
            buildOptionsPanel(currentDescriptor.optionDefs()) {},
            BorderLayout.CENTER
        )

        optionsPanel.revalidate()
        optionsPanel.repaint()
    }

    private fun regenerate() {
        try {
            @Suppress("UNCHECKED_CAST")
            val emitter = (currentDescriptor as LanguageDescriptor<LanguageOptions>).createEmitter(currentOptions)
            outputEditor.text = SchemaToCodeService.generateFromJson(
                json = inputEditor.text,
                rootName = "Root",
                emitter = emitter,
                config = generatorConfig
            )
        } catch (e: Exception) {
            outputEditor.text = "// Error\n// ${e.message}"
        }
    }

    private fun rebuildOutputEditor(newLanguage: TargetLanguage) {
        val oldText = if (::outputEditor.isInitialized) outputEditor.text else ""

        outputEditorContainer.removeAll()

        outputEditor = createEditor(getLanguageID(newLanguage))
        outputEditor.text = oldText

        outputEditorContainer.add(outputEditor, BorderLayout.CENTER)
        outputEditorContainer.revalidate()
        outputEditorContainer.repaint()
    }

    private fun createEditor(
        languageId: LanguageId,
    ): LanguageTextField {
        val lang = Language.findLanguageByID(languageId.toString())
        return LanguageTextField(lang, project, "").apply {
            setOneLineMode(false)

            isViewer = false
            preferredSize = null
            minimumSize = java.awt.Dimension(0, 0)
            addSettingsProvider { editor ->
                val ex = editor as EditorEx
                ex.settings.apply {
                    isLineNumbersShown = true
                    isIndentGuidesShown = true
                    isFoldingOutlineShown = true
                }


            }
        }
    }
}


