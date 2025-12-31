package com.github.ahmedwelhakim.schematocode.core.ir

sealed interface TypeDef {
    object StringT : TypeDef
    object NumberT : TypeDef
    object BooleanT : TypeDef
    object NullT : TypeDef
    object AnyT : TypeDef

    data class ArrayT(val element: TypeDef) : TypeDef
    data class ObjectT(val name: String, val fields: List<Field>) : TypeDef
    data class UnionT(val types: Set<TypeDef>) : TypeDef
    data class FormattedT(val base: TypeDef, val format: Format) : TypeDef
}

