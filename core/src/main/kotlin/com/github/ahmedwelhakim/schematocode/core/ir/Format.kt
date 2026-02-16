package com.github.ahmedwelhakim.schematocode.core.ir

/**
 * Represents semantic format hints for primitive types.
 * These provide additional context about the expected format of a value,
 * allowing emitters to use more specific types when available.
 *
 * For example, a STRING with UUID format might emit as `UUID` type in some languages.
 */
sealed interface Format {
    /** UUID format (e.g., "550e8400-e29b-41d4-a716-446655440000"). */
    object UUID : Format

    /** ISO 8601 date-time format (e.g., "2026-02-16T10:30:00Z"). */
    object DateTime : Format

    /**
     * Custom format with a user-defined name.
     * @property name The format identifier string.
     */
    data class Custom(val name: String) : Format
}