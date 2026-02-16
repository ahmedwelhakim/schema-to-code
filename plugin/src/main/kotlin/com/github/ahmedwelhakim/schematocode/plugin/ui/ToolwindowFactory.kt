package com.github.ahmedwelhakim.schematocode.plugin.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

/**
 * Factory for creating the Schema to Code tool window.
 *
 * Registered in `plugin.xml` to provide the tool window UI for the plugin.
 * Creates and manages the lifecycle of [SchemaToCodeToolWindow].
 */
class SchemaToCodeToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val panel = SchemaToCodeToolWindow(project)
        val content = ContentFactory.getInstance()
            .createContent(panel, null, false)
        content.setDisposer(panel)
        toolWindow.contentManager.addContent(content)
    }

}
