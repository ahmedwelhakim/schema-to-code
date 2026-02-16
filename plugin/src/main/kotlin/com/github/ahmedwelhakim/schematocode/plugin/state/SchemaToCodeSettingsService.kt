package com.github.ahmedwelhakim.schematocode.plugin.state

import com.github.ahmedwelhakim.schematocode.plugin.state.SchemaToCodeSettingsService.Companion.getInstance
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project

/**
 * IntelliJ service for persisting Schema to Code plugin settings.
 *
 * Settings are stored per-project in `schemaToCode.xml` within the `.idea` directory.
 * Use [getInstance] to access the service for a given project.
 *
 * @see SchemaToCodeState
 */
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
        /**
         * Gets the settings service instance for the specified project.
         *
         * @param project The project to get settings for.
         * @return The settings service instance.
         */
        fun getInstance(project: Project): SchemaToCodeSettingsService =
            project.getService(SchemaToCodeSettingsService::class.java)
    }
}