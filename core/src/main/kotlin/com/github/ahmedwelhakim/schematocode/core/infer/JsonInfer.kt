package com.github.ahmedwelhakim.schematocode.core.infer

import com.github.ahmedwelhakim.schematocode.core.ir.Field
import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import kotlinx.serialization.json.*

fun inferFromJson(name: String, jsonText: String): TypeDef {
    return when (val json = Json.parseToJsonElement(jsonText)) {
        is JsonObject -> TypeDef.ObjectT(name, inferFields(json))
        is JsonArray -> TypeDef.ArrayT(inferType(json))
        is JsonPrimitive -> inferPrimitive(json)
        JsonNull -> TypeDef.PrimitiveT(ScalarType.NULL)
    }
}

private fun inferFields(obj: JsonObject, optional: Boolean = false): List<Field> =
    obj.map { (key, value) ->
        Field(
            name = key,
            type = inferType(value),
            optional = optional
        )
    }

private fun inferType(el: JsonElement): TypeDef =
    when (el) {
        is JsonPrimitive -> inferPrimitive(el)
        is JsonArray -> inferArray(el)
        is JsonObject -> TypeDef.ObjectT(
            name = "Anonymous",
            fields = inferFields(el)
        )

        JsonNull -> TypeDef.PrimitiveT(ScalarType.NULL)
    }

private fun inferPrimitive(p: JsonPrimitive): TypeDef =
    when {
        p.isString -> TypeDef.PrimitiveT(ScalarType.STRING)
        p.booleanOrNull != null -> TypeDef.PrimitiveT(ScalarType.BOOLEAN)
        p.doubleOrNull != null -> TypeDef.PrimitiveT(ScalarType.NUMBER)
        p.contentOrNull == null -> TypeDef.PrimitiveT(ScalarType.NULL)
        else -> TypeDef.AnyT
    }

private fun inferArray(arr: JsonArray): TypeDef {
    if (arr.isEmpty()) return TypeDef.ArrayT(TypeDef.AnyT)

    val types = arr.map { inferType(it) }.toSet()
    return TypeDef.ArrayT(
        if (types.size == 1) types.first()
        else TypeDef.UnionT(types)
    )
}
