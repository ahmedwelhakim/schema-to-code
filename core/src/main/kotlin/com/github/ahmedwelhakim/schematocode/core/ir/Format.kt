package com.github.ahmedwelhakim.schematocode.core.ir

sealed interface Format {

    object UUID : Format

    object DateTime : Format

    data class Custom(val name: String) : Format
}