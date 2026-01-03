package com.github.ahmedwelhakim.schematocode.plugin.ui

import com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig
import com.github.ahmedwelhakim.schematocode.core.config.Language
import com.github.ahmedwelhakim.schematocode.core.emit.LanguageOptions
import com.github.ahmedwelhakim.schematocode.core.infer.inferFromJson
import com.github.ahmedwelhakim.schematocode.core.language.LanguageDescriptor
import com.github.ahmedwelhakim.schematocode.core.language.TypescriptLanguage
import com.github.ahmedwelhakim.schematocode.core.options.BooleanOption
import com.github.ahmedwelhakim.schematocode.core.options.EnumOption
import com.github.ahmedwelhakim.schematocode.core.options.OptionDef
import com.github.ahmedwelhakim.schematocode.plugin.language.LanguageRegistry
import com.intellij.json.JsonFileType
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.fileEditor.impl.createSplitter
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.EditorTextField
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.panel
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JScrollPane

class SchemaToCodeToolWindow : SimpleToolWindowPanel(true, true) {

    private val inputEditor = createEditor(
        fileType = JsonFileType.INSTANCE,
        editable = true
    )

    private val outputEditor = createEditor(
        fileType = PlainTextFileType.INSTANCE,
        editable = false
    )

    private var currentDescriptor: LanguageDescriptor<*> = TypescriptLanguage
    private var currentOptions: LanguageOptions = currentDescriptor.defaultOptions()

    private val optionsPanel = JPanel(BorderLayout())

    init {

        setContent(buildUi())
        rebuildOptionsPanel()
    }

    private fun buildUi(): JComponent = panel {
        row {
            comboBox(Language.values().toList())
                .label("Language:")
                .bindItem(
                    { currentDescriptor.language },
                    {
                        currentDescriptor = LanguageRegistry.getLanguageDescriptor(it!!)
                        currentOptions = currentDescriptor.defaultOptions()
                        rebuildOptionsPanel()
                        regenerate()
                    }
                )
        }

        row {
            cell(
                createSplitter(false, 0.5f, 0.1f, 0.9f).apply {
                    firstComponent = JScrollPane(inputEditor)
                    secondComponent = JScrollPane(outputEditor)
                }
            ).align(Align.FILL)
        }

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
                        .onChanged { onChange() }
                }

                is BooleanOption -> row {
                    checkBox(def.key.toString())
                        .onChanged { onChange() }
                }
            }
        }
    }

    private fun rebuildOptionsPanel() {
        optionsPanel.removeAll()


        optionsPanel.add(
            buildOptionsPanel(currentDescriptor.optionDefs()) {
                regenerate()
            },
            BorderLayout.CENTER
        )

        optionsPanel.revalidate()
        optionsPanel.repaint()
    }

    private fun regenerate() {
        try {
            val ir = inferFromJson("Root", inputEditor.text)
            val emitter = (currentDescriptor as LanguageDescriptor<LanguageOptions>).createEmitter(currentOptions)
            outputEditor.text = emitter.emit(ir, GeneratorConfig())
        } catch (e: Exception) {
            outputEditor.text = "// Error\n// ${e.message}"
        }
    }

    private fun createEditor(
        fileType: FileType,
        editable: Boolean
    ): EditorTextField =
        EditorTextField("", null, fileType).apply {
            setOneLineMode(false)
            setEnabled(true)
            setViewer(!editable)

            addSettingsProvider { editor ->
                (editor as EditorEx).settings.apply {
                    isLineNumbersShown = true
                    isLineMarkerAreaShown = false
                    isFoldingOutlineShown = true
                    isIndentGuidesShown = true
                }
            }
        }

}
