package com.github.ahmedwelhakim.schematocode.core.ir

sealed interface TypeDef {

    object AnyT : TypeDef

    data class PrimitiveT(
        val type: ScalarType,
        val format: Format? = null
    ) : TypeDef

    data class ArrayT(val element: TypeDef) : TypeDef
    data class ObjectT(val name: String, val fields: List<Field>) : TypeDef
    data class UnionT(val types: Set<TypeDef>) : TypeDef

}

