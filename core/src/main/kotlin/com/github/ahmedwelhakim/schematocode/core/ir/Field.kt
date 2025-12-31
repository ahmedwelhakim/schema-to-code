package com.github.ahmedwelhakim.schematocode.core.ir

data class Field(val name: String, val type: TypeDef, val optional: Boolean = false)
