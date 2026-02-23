package com.github.ahmedwelhakim.schematocode.core.naming

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class NamingStrategyTypeTest {

    @Test
    fun `IDENTITY creates IdentityNamingStrategy`() {
        val strategy = NamingStrategyType.IDENTITY.create()
        assertTrue(strategy is IdentityNamingStrategy)
    }

    @Test
    fun `PASCAL creates PascalCaseStrategy`() {
        val strategy = NamingStrategyType.PASCAL.create()
        assertTrue(strategy is PascalCaseStrategy)
    }

    @Test
    fun `CAMEL creates CamelCaseStrategy`() {
        val strategy = NamingStrategyType.CAMEL.create()
        assertTrue(strategy is CamelCaseStrategy)
    }

    @Test
    fun `SNAKE creates SnakeCaseStrategy`() {
        val strategy = NamingStrategyType.SNAKE.create()
        assertTrue(strategy is SnakeCaseStrategy)
    }

    @Test
    fun `all entries have non-blank bundle keys`() {
        NamingStrategyType.entries.forEach {
            assertTrue(it.bundleKey.isNotBlank(), "bundleKey for $it should not be blank")
        }
    }
}

