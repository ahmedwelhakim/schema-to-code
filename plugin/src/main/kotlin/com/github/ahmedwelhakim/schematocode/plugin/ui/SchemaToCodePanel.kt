package com.github.ahmedwelhakim.schematocode.plugin.ui

import com.github.ahmedwelhakim.schematocode.core.config.TargetLanguage
import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKeyHolder
import com.github.ahmedwelhakim.schematocode.core.naming.NamingStrategyType
import com.github.ahmedwelhakim.schematocode.core.options.BooleanOption
import com.github.ahmedwelhakim.schematocode.core.options.EnumOption
import com.github.ahmedwelhakim.schematocode.core.options.OptionDef
import com.github.ahmedwelhakim.schematocode.plugin.SchemaToCodeBundle
import com.github.ahmedwelhakim.schematocode.plugin.language.LanguageId
import com.github.ahmedwelhakim.schematocode.plugin.util.withEnumTranslation
import com.github.ahmedwelhakim.schematocode.plugin.viewmodel.SchemaToCodeViewModel
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Splitter
import com.intellij.ui.LanguageTextField
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.builder.RowLayout
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.layout.selected
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.awt.BorderLayout
import java.awt.Cursor
import java.awt.datatransfer.StringSelection
import javax.swing.*

/**
 * Main UI panel for the Schema to Code plugin.
 *
 * Contains:
 * - Settings toolbar (language, naming strategy, emission mode, language options)
 * - Split view with JSON input editor and generated code output editor
 * - Copy button for copying generated code to clipboard
 *
 * Uses MVVM pattern with [SchemaToCodeViewModel] for state management.
 *
 * @param project The project context for editor creation and settings.
 */
class SchemaToCodePanel(
    private val project: Project
) : JPanel(BorderLayout()), Disposable {

    private val viewModel = SchemaToCodeViewModel(project)
    private val uiScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val inputEditor = EditorFactory.create(project, LanguageId.JSON)
    private lateinit var outputEditor: LanguageTextField
    private lateinit var splitter: Splitter

    init {
        add(buildUi(), BorderLayout.CENTER)
        ApplicationManager.getApplication().invokeLater {
            inputEditor.requestFocusInWindow()
        }
        observeState()
        observeEditorSize()
    }

    override fun dispose() {
        uiScope.cancel()
        viewModel.dispose()
    }
    // ============================================================
    // UI
    // ============================================================

    private fun buildUi(): JComponent = panel {

        // ----------------------------
        // Toolbar
        // ----------------------------
        collapsibleGroup(
            SchemaToCodeBundle.message("ui.settings"),
            true
        ) {
            row {

                enumSelector(
                    SchemaToCodeBundle.message("ui.language"),
                    TargetLanguage.entries,
                    viewModel.state.value.targetLanguage
                ) { viewModel.onLanguageChanged(it) }

                enumSelector(
                    SchemaToCodeBundle.message("ui.namingStrategy"),
                    NamingStrategyType.entries,
                    viewModel.state.value.namingStrategy
                ) { viewModel.onNamingChanged(it) }


            }

            collapsibleGroup(
                SchemaToCodeBundle.message("ui.languageOptions"),
                true
            ) {

                buildLanguageOptions().forEach {
                    row {
                        cell(it)
                            .align(Align.FILL)
                    }
                }
            }.layout(RowLayout.LABEL_ALIGNED)
        }.layout(RowLayout.LABEL_ALIGNED)

        // ----------------------------
        // Editors
        // ----------------------------
        row {
            splitter = Splitter(false, 0.5f).apply {

                firstComponent = JPanel(BorderLayout()).apply {
                    add(JLabel(SchemaToCodeBundle.message("ui.input")), BorderLayout.NORTH)
                    add(
                        JScrollPane(inputEditor).apply {
                            verticalScrollBar.unitIncrement = 12
                            horizontalScrollBar.unitIncrement = 12
                        },
                        BorderLayout.CENTER
                    )
                }

                secondComponent = JPanel(BorderLayout()).apply {
                    add(JLabel(SchemaToCodeBundle.message("ui.output")), BorderLayout.NORTH)
                    add(buildOutputArea(), BorderLayout.CENTER)
                }

                dividerWidth = 10
            }

            cell(splitter).align(Align.FILL)
        }.resizableRow()

        // ----------------------------
        // Copy Button
        // ----------------------------
        row {
            val copyButton = JButton(SchemaToCodeBundle.message("ui.copy")).apply {
                isFocusable = false
                cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                addActionListener { copyOutput() }
            }

            val bottomBar = JPanel(BorderLayout()).apply {
                border = BorderFactory.createEmptyBorder(12, 8, 0, 8)
                add(copyButton, BorderLayout.EAST)

            }

            cell(bottomBar).align(Align.FILL)
        }


    }

    private fun buildOutputArea(): JComponent {
        outputEditor = EditorFactory.create(project, LanguageId.TYPESCRIPT)

        return JPanel(BorderLayout()).apply {
            add(JScrollPane(outputEditor).apply {
                verticalScrollBar.unitIncrement = 12
                horizontalScrollBar.unitIncrement = 12

            }, BorderLayout.CENTER)

        }
    }

    private fun buildLanguageOptions(): List<JComponent> {

        val optionDefs = viewModel.state.value.optionDefs
        return optionDefs.map { optionDef ->
            val key = optionDef.key
            val langOption = viewModel.state.value.getLanguageOption(optionDef.key)
            val initialValue = viewModel.state.value.descriptor.parseOptionValue(
                key,
                langOption
            ) ?: optionDef.default
            buildOptionsPanel(
                optionDef,
                initialValue!!
            ) {
                viewModel.onLanguageOptionChanged(optionDef.key, it as Enum<*>)
            }
        }
    }

    private fun buildOptionsPanel(
        def: OptionDef<*>,
        initialValue: Any,
        onChange: (Any) -> Unit
    ): JComponent = panel {

        when (def) {
            is EnumOption<*> -> row(SchemaToCodeBundle.message(def.i18nName)) {
                comboBox(def.values.toList())
                    .withEnumTranslation { it.bundleKey }
                    .applyToComponent {
                        selectedItem =
                            initialValue


                    }
                    .onChanged {
                        onChange(it.selectedItem!!)
                    }
            }

            is BooleanOption -> row(SchemaToCodeBundle.message(def.i18nName)) {
                checkBox(def.key.toString())
                    .onChanged { onChange(it) }
                    .applyToComponent {
                        isSelected = initialValue as Boolean


                    }.onChanged {
                        onChange(it.selected)
                    }
            }

        }
    }

    private fun <T> Row.enumSelector(
        labelText: String,
        values: Collection<T>,
        initialValue: T,
        onChange: (T) -> Unit
    ) where T : Enum<T>, T : MessageKeyHolder {

        panel {

            row(labelText) {

                comboBox(values)
                    .withEnumTranslation { it.bundleKey }
                    .align(Align.FILL)
                    .applyToComponent {
                        selectedItem = initialValue
                    }
                    .onChanged {
                        @Suppress("UNCHECKED_CAST")
                        onChange(it.selectedItem as T)
                    }
            }.resizableRow()
        }.resizableColumn()
    }

    private fun observeEditorSize() {
        splitter.addComponentListener(object : java.awt.event.ComponentAdapter() {
            override fun componentResized(e: java.awt.event.ComponentEvent?) {
                adjustSplitterOrientation()
            }
        })
    }

    // ============================================================
    // State Observer
    // ============================================================

    private fun observeState() {
        viewModel.state
            .onEach { state ->
                ApplicationManager.getApplication().invokeLater {

                    if (inputEditor.text != state.jsonInput)
                        inputEditor.text = state.jsonInput

                    outputEditor.text = state.output

                }
            }
            .launchIn(uiScope)

        inputEditor.document.addDocumentListener(
            object : DocumentListener {
                override fun documentChanged(event: DocumentEvent) {
                    viewModel.onJsonChanged(inputEditor.text)
                }
            }
        )
    }

    // ============================================================
    // Utilities
    // ============================================================


    private fun copyOutput() {
        CopyPasteManager.getInstance()
            .setContents(StringSelection(outputEditor.text))

        UINotifications.showCopied(project)
    }

    private fun adjustSplitterOrientation() {
        val vertical = width < 600
        splitter.orientation = vertical
    }
}
