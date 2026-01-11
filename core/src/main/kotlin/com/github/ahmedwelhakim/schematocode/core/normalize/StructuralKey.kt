package com.github.ahmedwelhakim.schematocode.core.normalize

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef

fun TypeDef.structuralKey(): String =
    when (this) {
        is TypeDef.PrimitiveT ->
            "P:${type}"

        is TypeDef.ArrayT ->
            "A:${element.structuralKey()}"

        is TypeDef.ObjectT ->
            "O:" + fields
                .sortedBy { it.name }
                .joinToString("|") {
                    "${it.name}:${it.type.structuralKey()}:${it.optional}"
                }

        is TypeDef.UnionT ->
            "U:" + types.map { it.structuralKey() }.sorted().joinToString("|")

        TypeDef.AnyT -> "ANY"
    }