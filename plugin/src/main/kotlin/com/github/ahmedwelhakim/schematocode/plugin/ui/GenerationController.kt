package com.github.ahmedwelhakim.schematocode.plugin.ui

import com.intellij.openapi.application.ApplicationManager
import kotlinx.coroutines.*

/**
 * Controller for debounced code generation.
 *
 * Schedules generation with a delay to avoid regenerating on every keystroke.
 * If a new generation is scheduled before the previous one completes, the previous
 * one is cancelled.
 *
 * @param generate Function that performs the code generation and returns the result.
 * @param onResult Callback invoked on the EDT with the generation result.
 */
class GenerationController(
    private val generate: () -> String,
    private val onResult: (String) -> Unit
) {

    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.Default
    )

    private var job: Job? = null

    /**
     * Schedules a code generation with debouncing.
     * Cancels any pending generation and starts a new one after a 400ms delay.
     */
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

    /**
     * Disposes the controller and cancels any pending generation.
     */
    fun dispose() {
        scope.cancel()
    }
}