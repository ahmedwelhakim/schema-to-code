package com.github.ahmedwelhakim.schematocode.core.normalize

import com.github.ahmedwelhakim.schematocode.core.ir.Field
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef

/**
 * Merges multiple object types into a single unified object type.
 *
 * This is used when multiple objects with the same structure are encountered,
 * such as in arrays of objects. Fields are merged by name, and fields that
 * don't appear in all objects are marked as optional.
 *
 * @param objects The list of object types to merge.
 * @return A single merged object type containing all fields from the input objects.
 */
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
        fields = mergedFields
    )
}