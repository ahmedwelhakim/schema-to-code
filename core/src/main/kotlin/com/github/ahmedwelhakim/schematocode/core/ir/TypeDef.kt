package com.github.ahmedwelhakim.schematocode.core.ir

sealed interface TypeDef {

    object AnyT : TypeDef

    class PrimitiveT(
        val type: ScalarType,
        val format: Format? = null
    ) : TypeDef

    class ArrayT(val element: TypeDef) : TypeDef
    class ObjectT(val fields: List<Field>) : TypeDef
    class UnionT(val types: Set<TypeDef>) : TypeDef

}

