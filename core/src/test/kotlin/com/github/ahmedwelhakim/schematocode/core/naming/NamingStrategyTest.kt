package com.github.ahmedwelhakim.schematocode.core.naming

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NamingStrategyTest {
    @Test
    fun `camel case strategy works`() {
        val strategy = CamelCaseStrategy()
        assertEquals("myField", strategy.fieldName("my_field"))
    }
}

