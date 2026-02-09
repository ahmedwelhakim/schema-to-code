package com.github.ahmedwelhakim.schematocode.core.config

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class GeneratorConfigTest {
    @Test
    fun `default config is not null`() {
        val config = GeneratorConfig()
        assertNotNull(config)
    }
}

