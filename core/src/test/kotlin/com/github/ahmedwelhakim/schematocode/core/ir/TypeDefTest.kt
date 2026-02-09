package com.github.ahmedwelhakim.schematocode.core.ir

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TypeDefTest {
    @Test
    fun `objectT is a TypeDef`() {
        val obj = TypeDef.ObjectT("Test", emptyList())
        assertTrue(obj is TypeDef)
    }
}

