package com.github.ahmedwelhakim.schematocode.core.emit.typescript

data class TypescriptOptions(
    val modelKind: com.github.ahmedwelhakim.schematocode.core.emit.typescript.TypeScriptModelKind = _root_ide_package_.com.github.ahmedwelhakim.schematocode.core.emit.typescript.TypeScriptModelKind.INTERFACE
)

enum class TypeScriptModelKind {
    INTERFACE,
    TYPE_ALIAS
}