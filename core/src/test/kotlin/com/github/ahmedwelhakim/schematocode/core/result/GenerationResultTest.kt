package com.github.ahmedwelhakim.schematocode.core.result

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GenerationResultTest {

    @Test
    fun `success result contains code`() {
        val result = GenerationResult.success("generated code")

        assertTrue(result.isSuccess())
        assertFalse(result.isFailure())
        assertEquals("generated code", result.getOrNull())
    }

    @Test
    fun `failure result contains message`() {
        val result = GenerationResult.failure("Something went wrong")

        assertFalse(result.isSuccess())
        assertTrue(result.isFailure())
        assertNull(result.getOrNull())

        val failure = result as GenerationResult.Failure
        assertEquals("Something went wrong", failure.message)
    }

    @Test
    fun `failure from exception contains exception`() {
        val exception = RuntimeException("Test error")
        val result = GenerationResult.failure(exception)

        val failure = result as GenerationResult.Failure
        assertEquals("Test error", failure.message)
        assertEquals(exception, failure.exception)
    }

    @Test
    fun `getOrElse returns code on success`() {
        val result = GenerationResult.success("code")

        val value = result.getOrElse { "default" }

        assertEquals("code", value)
    }

    @Test
    fun `getOrElse returns default on failure`() {
        val result = GenerationResult.failure("error")

        val value = result.getOrElse { "default: ${it.message}" }

        assertEquals("default: error", value)
    }

    @Test
    fun `runCatching returns success when no exception`() {
        val result = GenerationResult.runCatching {
            "success"
        }

        assertTrue(result.isSuccess())
        assertEquals("success", result.getOrNull())
    }

    @Test
    fun `runCatching returns failure when exception thrown`() {
        val result = GenerationResult.runCatching {
            throw RuntimeException("Test error")
        }

        assertTrue(result.isFailure())
        val failure = result as GenerationResult.Failure
        assertEquals("Test error", failure.message)
    }
}

