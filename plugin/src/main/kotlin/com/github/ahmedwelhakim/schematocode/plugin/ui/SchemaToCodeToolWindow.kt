package com.github.ahmedwelhakim.schematocode.plugin.ui

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel

class SchemaToCodeToolWindow(
    project: Project
) : SimpleToolWindowPanel(true, true), Disposable {

    var panel = SchemaToCodePanel(project)

    init {
        setContent(panel)
    }

    override fun dispose() {
        panel.dispose()
    }

}
