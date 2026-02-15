package com.github.ahmedwelhakim.schematocode.plugin.ui

import com.github.ahmedwelhakim.schematocode.core.config.ModelEmissionMode
import com.github.ahmedwelhakim.schematocode.core.config.TargetLanguage
import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKeyHolder
import com.github.ahmedwelhakim.schematocode.core.naming.NamingStrategyType
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
import com.intellij.ui.dsl.builder.panel
import com.intellij.util.ui.UIUtil
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

class SchemaToCodePanel(
    private val project: Project
) : JPanel(BorderLayout()), Disposable {

    private val viewModel = SchemaToCodeViewModel(project)
    private val uiScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val inputEditor = EditorFactory.create(project, LanguageId.JSON)
    private lateinit var outputEditor: LanguageTextField
    private val outputContainer = JPanel(BorderLayout())
    private lateinit var splitter: Splitter

    init {
        add(buildUi(), BorderLayout.CENTER)
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
        row {

            enumSelector(
                "Language",
                TargetLanguage.entries
            ) { viewModel.onLanguageChanged(it) }

            enumSelector(
                "Naming Strategy",
                NamingStrategyType.entries
            ) { viewModel.onNamingChanged(it) }

            enumSelector(
                "Mode",
                ModelEmissionMode.entries
            ) { viewModel.onEmissionModeChanged(it) }

        }

        // ----------------------------
        // Editors
        // ----------------------------
        row {
            splitter = Splitter(false, 0.5f).apply {
                firstComponent = JScrollPane(inputEditor)
                secondComponent = buildOutputArea()
                dividerWidth = 10
            }

            cell(splitter).align(Align.FILL)
        }.resizableRow()

    }

    private fun buildOutputArea(): JComponent {
        outputEditor = EditorFactory.create(project, LanguageId.TYPESCRIPT)

        val copyButton = JButton("Copy").apply {
            isFocusable = false
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            addActionListener { copyOutput() }
        }

        val bottomBar = JPanel(BorderLayout()).apply {
            border = BorderFactory.createEmptyBorder(4, 8, 4, 8)
            add(copyButton, BorderLayout.EAST)
        }

        return JPanel(BorderLayout()).apply {
            add(JScrollPane(outputEditor), BorderLayout.CENTER)
            add(bottomBar, BorderLayout.SOUTH)
        }
    }

    private fun <T> Row.enumSelector(
        labelText: String,
        values: Collection<T>,
        onChange: (T) -> Unit
    ) where T : Enum<T>, T : MessageKeyHolder {
        panel {
            row {
                label(labelText).applyToComponent {
                    foreground = UIUtil.getContextHelpForeground()
                }
            }
            row {
                comboBox(values)
                    .withEnumTranslation { it.bundleKey }
                    .align(Align.FILL)
                    .onChanged {
                        @Suppress("UNCHECKED_CAST")
                        onChange(it.selectedItem as T)
                    }
            }
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
