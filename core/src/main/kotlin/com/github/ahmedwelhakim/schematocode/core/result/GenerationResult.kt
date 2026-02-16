package com.github.ahmedwelhakim.schematocode.core.result

/**
 * Represents the result of a code generation operation.
 * Uses a sealed class pattern for type-safe error handling.
 */
sealed class GenerationResult {
    /**
     * Successful generation with the produced code.
     * @property code The generated source code.
     */
    data class Success(val code: String) : GenerationResult()

    /**
     * Failed generation with error details.
     * @property message Human-readable error message.
     * @property exception The underlying exception, if any.
     */
    data class Failure(
        val message: String,
        val exception: Throwable? = null
    ) : GenerationResult()

    /**
     * Returns the generated code if successful, or null if failed.
     */
    fun getOrNull(): String? = when (this) {
        is Success -> code
        is Failure -> null
    }

    /**
     * Returns the generated code if successful, or the result of [defaultValue] if failed.
     */
    inline fun getOrElse(defaultValue: (Failure) -> String): String = when (this) {
        is Success -> code
        is Failure -> defaultValue(this)
    }

    /**
     * Returns true if the generation was successful.
     */
    fun isSuccess(): Boolean = this is Success

    /**
     * Returns true if the generation failed.
     */
    fun isFailure(): Boolean = this is Failure

    companion object {
        /**
         * Creates a successful result.
         */
        fun success(code: String): GenerationResult = Success(code)

        /**
         * Creates a failure result from an exception.
         */
        fun failure(exception: Throwable): GenerationResult =
            Failure(exception.message ?: "Unknown error", exception)

        /**
         * Creates a failure result from a message.
         */
        fun failure(message: String): GenerationResult = Failure(message)

        /**
         * Wraps a block of code and catches any exceptions as failures.
         */
        inline fun runCatching(block: () -> String): GenerationResult =
            try {
                Success(block())
            } catch (e: Exception) {
                failure(e)
            }
    }
}

