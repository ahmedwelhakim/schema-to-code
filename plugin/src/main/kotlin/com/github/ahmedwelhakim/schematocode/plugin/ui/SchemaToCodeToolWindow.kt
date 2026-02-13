package com.github.ahmedwelhakim.schematocode.plugin.ui

import com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig
import com.github.ahmedwelhakim.schematocode.core.config.ModelEmissionMode
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
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.ui.Splitter
import com.intellij.ui.LanguageTextField
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.panel
import com.intellij.util.ui.UIUtil
import kotlinx.coroutines.launch
import java.awt.BorderLayout
import java.awt.Cursor
import java.awt.datatransfer.StringSelection
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JScrollPane

class SchemaToCodeToolWindow(private val project: Project) : SimpleToolWindowPanel(true, true) {

    private val inputEditor = createEditor(
        LanguageId.JSON,
    )

    private val outputEditorContainer = JPanel(BorderLayout())
    private lateinit var outputEditor: LanguageTextField
    private lateinit var mainSplitter: Splitter

    private var currentDescriptor: LanguageDescriptor<*> = TypescriptLanguage
    private var currentOptions: LanguageOptions = currentDescriptor.defaultOptions()
    private val generatorConfig = GeneratorConfig()
    private val optionsPanel = JPanel(BorderLayout())
    private val scope = kotlinx.coroutines.CoroutineScope(
        kotlinx.coroutines.SupervisorJob() +
                kotlinx.coroutines.Dispatchers.Default
    )
    private var regenerateJob: kotlinx.coroutines.Job? = null


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
        mainSplitter.addComponentListener(object : java.awt.event.ComponentAdapter() {
            override fun componentResized(e: java.awt.event.ComponentEvent?) {
                adjustSplitterOrientation()
            }
        })
        rebuildOptionsPanel()
        rebuildOutputEditor(currentDescriptor.targetLanguage)
        inputEditor.document.addDocumentListener(
            object : com.intellij.openapi.editor.event.DocumentListener {
                override fun documentChanged(event: com.intellij.openapi.editor.event.DocumentEvent) {
                    scheduleRegeneration()
                }
            }
        )
        writeText(generate())
    }

    private fun writeText(text: String) {
        ApplicationManager.getApplication().invokeLater {
            outputEditor.text = text
        }
    }

    private fun buildUi(): JComponent = panel {

        // ============================================================
        // Top Toolbar Row
        // ============================================================
        row {

            panel {
                row {
                    label("Language").applyToComponent {
                        foreground = UIUtil.getContextHelpForeground()
                    }
                }
                row {
                    comboBox(TargetLanguage.entries)
                        .withEnumTranslation { it.bundleKey }
                        .align(Align.FILL)
                        .applyToComponent {
                            selectedItem = currentDescriptor.targetLanguage
                        }
                        .onChanged {

                            currentDescriptor =
                                LanguageRegistry.getLanguageDescriptor(it.selectedItem as TargetLanguage)
                            currentOptions = currentDescriptor.defaultOptions()
                            writeText(generate())
                        }
                }
            }.resizableColumn()

            panel {
                row {
                    label("Naming Strategy").applyToComponent {
                        foreground = UIUtil.getContextHelpForeground()
                    }
                }
                row {
                    comboBox(NamingStrategyType.entries)
                        .withEnumTranslation { it.bundleKey }
                        .align(Align.FILL)
                        .applyToComponent {
                            selectedItem = generatorConfig.namingStrategyType
                        }
                        .onChanged {
                            generatorConfig.namingStrategyType = it.selectedItem as NamingStrategyType
                            writeText(generate())
                        }

                }
            }.resizableColumn()

            panel {
                row {
                    label("Mode").applyToComponent {
                        foreground = UIUtil.getContextHelpForeground()
                    }
                }
                row {
                    comboBox(ModelEmissionMode.entries)
                        .withEnumTranslation { it.bundleKey }
                        .align(Align.FILL)
                        .applyToComponent {
                            selectedItem = generatorConfig.emissionMode
                        }
                        .onChanged {
                            generatorConfig.emissionMode = it.selectedItem as ModelEmissionMode
                            writeText(generate())
                        }
                }
            }.resizableColumn()


        }


        // ============================================================
        // Editors Area (Resizable)
        // ============================================================
        row {
            mainSplitter = Splitter(false, 0.5f).apply {
                firstComponent = JScrollPane(inputEditor).apply {
                    verticalScrollBar.unitIncrement = 12
                    horizontalScrollBar.unitIncrement = 12

                }
                secondComponent = JScrollPane(outputEditorContainer).apply {
                    verticalScrollBar.unitIncrement = 12
                    horizontalScrollBar.unitIncrement = 12
                }
                dividerWidth = 12
            }


            cell(
                mainSplitter
            ).align(Align.FILL)
        }.resizableRow()
        // Bottom bar with copy button
        val bottomPanel = JPanel(BorderLayout()).apply {
            border = javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8)
        }

        val copyButton = JButton("Copy").apply {
            toolTipText = "Copy generated code to clipboard"
            isFocusable = false
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            addActionListener {
                CopyPasteManager.getInstance()
                    .setContents(StringSelection(outputEditor.text))

                showCopyNotification()
            }
        }

        bottomPanel.add(copyButton, BorderLayout.EAST)

        row {
            cell(bottomPanel)
                .align(Align.FILL)
        }
        // ============================================================
        // Advanced Options Section (Cleaner Layout)
        // ============================================================

        collapsibleGroup(
            "Advanced Options",
            true
        ) {
            row {
                cell(optionsPanel)
                    .align(Align.FILL)
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

    private fun generate(): String {
        try {
            @Suppress("UNCHECKED_CAST")
            val emitter = (currentDescriptor as LanguageDescriptor<LanguageOptions>).createEmitter(currentOptions)
            return SchemaToCodeService.generateFromJson(
                json = inputEditor.text,
                rootName = "Root",
                emitter = emitter,
                config = generatorConfig
            )
        } catch (e: Exception) {
            return "// Error\n// ${e.message}"
        }
    }

    private fun rebuildOutputEditor(newLanguage: TargetLanguage) {
        val oldText = if (::outputEditor.isInitialized) outputEditor.text else ""

        outputEditorContainer.removeAll()
        outputEditorContainer.layout = BorderLayout()

        // Create editor
        outputEditor = createEditor(getLanguageID(newLanguage))
        outputEditor.text = oldText

        // Center editor
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

    private fun adjustSplitterOrientation() {
        val widthThreshold = 600

        val shouldBeVertical = width < widthThreshold

        if (mainSplitter.orientation !=
            if (shouldBeVertical) true else false
        ) {
            mainSplitter.orientation = shouldBeVertical
        }
    }

    private fun scheduleRegeneration() {
        regenerateJob?.cancel()

        regenerateJob = scope.launch {
            kotlinx.coroutines.delay(400)
            val res = generate()
            ApplicationManager.getApplication().invokeLater {
                writeText(res)
            }
        }
    }

    private fun showCopyNotification() {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("SchemaToCode")
            .createNotification(
                "Copied to clipboard",
                NotificationType.INFORMATION
            )
            .notify(project)
    }

}


