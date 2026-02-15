package com.github.ahmedwelhakim.schematocode.plugin.state

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project

@State(
    name = "SchemaToCodeSettings",
    storages = [Storage("schemaToCode.xml")]
)
@Service(Service.Level.PROJECT)
class SchemaToCodeSettingsService :
    PersistentStateComponent<SchemaToCodeState> {

    private var state = SchemaToCodeState()

    override fun getState(): SchemaToCodeState = state

    override fun loadState(state: SchemaToCodeState) {
        this.state = state
    }

    companion object {
        fun getInstance(project: Project): SchemaToCodeSettingsService =
            project.getService(SchemaToCodeSettingsService::class.java)
    }
   
}