package com.github.ahmedwelhakim.schematocode.core.normalize

import com.github.ahmedwelhakim.schematocode.core.ir.Field
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef

fun mergeObjects(
    objects: List<TypeDef.ObjectT>
): TypeDef.ObjectT {

//    if (objects.size == 1) return objects.first()

    val fieldsByName =
        objects.flatMap { it.fields }
            .groupBy { it.name }

    val mergedFields = fieldsByName.map { (name, variants) ->
        val mergedType =
            mergeTypes(variants.map { it.type })

        Field(
            name = name,
            type = mergedType,
            optional = variants.size < objects.size
        )
    }

    return TypeDef.ObjectT(
        name = objects.first().name,
        fields = mergedFields
    )
}