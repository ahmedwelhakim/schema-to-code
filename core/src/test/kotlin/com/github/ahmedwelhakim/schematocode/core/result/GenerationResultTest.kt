package com.github.ahmedwelhakim.schematocode.core.result

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GenerationResultTest {

    // ── Success ────────────────────────────────────────────────

    @Test
    fun `success isSuccess returns true`() {
        val result = GenerationResult.success("code")
        assertTrue(result.isSuccess())
        assertFalse(result.isFailure())
    }

    @Test
    fun `success getOrNull returns code`() {
        val result = GenerationResult.success("generated code")
        assertEquals("generated code", result.getOrNull())
    }

    @Test
    fun `success getOrElse returns code`() {
        val result = GenerationResult.success("code")
        val value = result.getOrElse { "fallback" }
        assertEquals("code", value)
    }

    // ── Failure ────────────────────────────────────────────────

    @Test
    fun `failure isFailure returns true`() {
        val result = GenerationResult.failure("error")
        assertTrue(result.isFailure())
        assertFalse(result.isSuccess())
    }

    @Test
    fun `failure getOrNull returns null`() {
        val result = GenerationResult.failure("error")
        assertNull(result.getOrNull())
    }

    @Test
    fun `failure getOrElse returns fallback`() {
        val result = GenerationResult.failure("error")
        val value = result.getOrElse { "fallback" }
        assertEquals("fallback", value)
    }

    @Test
    fun `failure from exception preserves message`() {
        val ex = RuntimeException("boom")
        val result = GenerationResult.failure(ex)
        assertTrue(result is GenerationResult.Failure)
        assertEquals("boom", (result as GenerationResult.Failure).message)
        assertSame(ex, result.exception)
    }

    @Test
    fun `failure from exception with null message uses Unknown error`() {
        val ex = RuntimeException()
        val result = GenerationResult.failure(ex)
        assertTrue(result is GenerationResult.Failure)
        assertEquals("Unknown error", (result as GenerationResult.Failure).message)
    }

    @Test
    fun `failure from string message has no exception`() {
        val result = GenerationResult.failure("msg")
        assertTrue(result is GenerationResult.Failure)
        val failure = result as GenerationResult.Failure
        assertEquals("msg", failure.message)
        assertNull(failure.exception)
    }

    // ── runCatching ────────────────────────────────────────────

    @Test
    fun `runCatching success`() {
        val result = GenerationResult.runCatching { "hello" }
        assertTrue(result.isSuccess())
        assertEquals("hello", result.getOrNull())
    }

    @Test
    fun `runCatching failure`() {
        val result = GenerationResult.runCatching { throw RuntimeException("fail") }
        assertTrue(result.isFailure())
        assertEquals("fail", (result as GenerationResult.Failure).message)
    }

    // ── getOrElse with failure info ────────────────────────────

    @Test
    fun `getOrElse provides failure details`() {
        val result = GenerationResult.failure("specific error")
        val value = result.getOrElse { failure -> "Error: ${failure.message}" }
        assertEquals("Error: specific error", value)
    }
}

