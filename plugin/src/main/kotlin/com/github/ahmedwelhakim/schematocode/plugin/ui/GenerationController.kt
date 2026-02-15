package com.github.ahmedwelhakim.schematocode.plugin.ui

import com.intellij.openapi.application.ApplicationManager
import kotlinx.coroutines.*

class GenerationController(
    private val generate: () -> String,
    private val onResult: (String) -> Unit
) {

    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.Default
    )

    private var job: Job? = null

    fun schedule() {
        job?.cancel()
        job = scope.launch {
            delay(400)
            val result = generate()
            ApplicationManager.getApplication().invokeLater {
                onResult(result)
            }
        }
    }

    fun dispose() {
        scope.cancel()
    }
}