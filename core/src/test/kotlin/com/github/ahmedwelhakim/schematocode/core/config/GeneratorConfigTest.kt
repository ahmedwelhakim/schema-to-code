package com.github.ahmedwelhakim.schematocode.core.config

import com.github.ahmedwelhakim.schematocode.core.naming.NamingStrategyType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GeneratorConfigTest {

    @Test
    fun `default config has identity naming and Root name`() {
        val config = GeneratorConfig()
        assertEquals(NamingStrategyType.IDENTITY, config.namingStrategyType)
        assertEquals("Root", config.name)
    }

    @Test
    fun `custom config values are retained`() {
        val config = GeneratorConfig(
            namingStrategyType = NamingStrategyType.CAMEL,
            name = "MyModel"
        )
        assertEquals(NamingStrategyType.CAMEL, config.namingStrategyType)
        assertEquals("MyModel", config.name)
    }

    @Test
    fun `config is mutable via var properties`() {
        val config = GeneratorConfig()
        config.namingStrategyType = NamingStrategyType.PASCAL
        config.name = "Updated"
        assertEquals(NamingStrategyType.PASCAL, config.namingStrategyType)
        assertEquals("Updated", config.name)
    }

    @Test
    fun `config data class copy works`() {
        val original = GeneratorConfig(name = "Original")
        val copy = original.copy(name = "Copy")
        assertEquals("Original", original.name)
        assertEquals("Copy", copy.name)
    }

    @Test
    fun `config data class equality`() {
        val a = GeneratorConfig(NamingStrategyType.SNAKE, "Test")
        val b = GeneratorConfig(NamingStrategyType.SNAKE, "Test")
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun `config data class inequality`() {
        val a = GeneratorConfig(NamingStrategyType.SNAKE, "Test")
        val b = GeneratorConfig(NamingStrategyType.CAMEL, "Test")
        assertNotEquals(a, b)
    }
}

