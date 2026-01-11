package com.github.ahmedwelhakim.schematocode.core.normalize

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef

object TypeNormalizer {


    fun normalize(type: TypeDef): TypeDef {


        return when (type) {

            is TypeDef.ArrayT ->
                TypeDef.ArrayT(normalize(type.element))

            is TypeDef.ObjectT ->
                mergeObjects(listOf(type))

            is TypeDef.UnionT ->
                mergeTypes(type.types.toList())

            else -> type
        }
    }
}