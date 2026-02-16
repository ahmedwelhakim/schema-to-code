package com.github.ahmedwelhakim.schematocode.plugin.ui

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel

/**
 * Main tool window container for the Schema to Code plugin.
 *
 * This is the top-level component displayed in the IntelliJ tool window.
 * It wraps [SchemaToCodePanel] which contains the actual UI elements.
 *
 * @param project The project this tool window belongs to.
 */
class SchemaToCodeToolWindow(
    project: Project
) : SimpleToolWindowPanel(true, false), Disposable {

    var panel = SchemaToCodePanel(project)

    init {
        setContent(panel)
    }

    override fun dispose() {
        panel.dispose()
    }

}
