package com.github.ahmedwelhakim.schematocode.plugin.ui

import com.github.ahmedwelhakim.schematocode.plugin.language.LanguageId
import com.intellij.lang.Language
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.project.Project
import com.intellij.ui.LanguageTextField

/**
 * Factory for creating language-aware text editors.
 *
 * Creates [LanguageTextField] instances configured with syntax highlighting,
 * line numbers, and other editor features for the specified language.
 */
object EditorFactory {

    /**
     * Creates a new language-aware text editor.
     *
     * @param project The project context for the editor.
     * @param languageId The language ID for syntax highlighting.
     * @return A configured [LanguageTextField] instance.
     */
    fun create(
        project: Project,
        languageId: LanguageId
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
