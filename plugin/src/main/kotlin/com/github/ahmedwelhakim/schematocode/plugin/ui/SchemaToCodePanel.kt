package com.github.ahmedwelhakim.schematocode.plugin.ui

import com.github.ahmedwelhakim.schematocode.core.config.Language
import com.github.ahmedwelhakim.schematocode.core.infer.inferFromJson
import com.intellij.json.JsonFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.EditorTextField
import com.intellij.ui.JBSplitter
import java.awt.BorderLayout
import javax.swing.*

class SchemaToCodePanel(
    private val project: Project
) : JPanel(BorderLayout()) {

    private val inputEditor = EditorTextField(
        "",
        project,
        JsonFileType.INSTANCE
    )

    private val outputEditor = EditorTextField(
        "",
        project,
        null
    ).apply {
        isViewer = true // read-only
    }

    private val languageBox = ComboBox(Language.entries.map { it.name }.toTypedArray())

    init {
        val splitter = JBSplitter(true, 0.6f).apply {
            firstComponent = wrap("JSON Input", inputEditor)
            secondComponent = wrap("Output", outputEditor)
        }

        add(splitter, BorderLayout.CENTER)
        add(createBottomBar(), BorderLayout.SOUTH)
    }

    private fun createBottomBar(): JComponent {
        val generateButton = JButton("Generate").apply {
            addActionListener { generate() }
        }

        return JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            add(JLabel("Language: "))
            add(languageBox)
            add(Box.createHorizontalStrut(8))
            add(generateButton)
            add(Box.createHorizontalGlue())
        }
    }

    private fun generate() {
        val json = inputEditor.text.trim()
        if (json.isEmpty()) {
            outputEditor.text = "// Paste JSON on the left"
            return
        }

        val language = languageBox.selectedItem as Language

        try {
            val ir = inferFromJson("Root", json)
            val emitter = emitterFor(language)
            val code = emitter.emit(ir, defaultConfig(language))

            outputEditor.text = code
        } catch (e: Exception) {
            outputEditor.text = "// Error:\n// ${e.message}"
        }
    }

    private fun wrap(title: String, component: JComponent): JComponent =
        JPanel(BorderLayout()).apply {
            border = BorderFactory.createTitledBorder(title)
            add(component, BorderLayout.CENTER)
        }
}