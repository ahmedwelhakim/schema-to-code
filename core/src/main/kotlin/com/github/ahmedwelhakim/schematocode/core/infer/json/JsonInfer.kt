package com.github.ahmedwelhakim.schematocode.core.infer.json

import com.github.ahmedwelhakim.schematocode.core.infer.InputParseException
import com.github.ahmedwelhakim.schematocode.core.infer.InputParser
import com.github.ahmedwelhakim.schematocode.core.ir.Field
import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import kotlinx.serialization.json.*

/**
 * Parser for JSON input that infers type definitions from JSON data.
 * Analyzes the structure of JSON to create corresponding TypeDef representations.
 */
object JsonInputParser : InputParser {
    override fun parse(input: String, rootName: String): TypeDef {
        return try {
            inferFromJson(rootName, input)
        } catch (e: Exception) {
            throw InputParseException("Failed to parse JSON: ${e.message}", e)
        }
    }
}

/**
 * Infers a TypeDef from a JSON string.
 *
 * @param name The name hint for the root type.
 * @param jsonText The JSON string to parse.
 * @return The inferred type definition.
 */
fun inferFromJson(name: String, jsonText: String): TypeDef {
    val raw = when (val json = Json.parseToJsonElement(jsonText)) {
        is JsonObject -> TypeDef.ObjectT(inferFields(json))
        is JsonArray -> inferArray(json, name)
        is JsonPrimitive -> inferPrimitive(json)
        JsonNull -> TypeDef.PrimitiveT(ScalarType.NULL)
    }

    return raw
}

private fun inferFields(obj: JsonObject): List<Field> =
    obj.map { (key, value) ->
        Field(
            name = key,
            type = inferType(value, key),
            optional = false
        )
    }

private fun inferType(el: JsonElement, key: String): TypeDef =
    when (el) {
        is JsonPrimitive -> inferPrimitive(el)
        is JsonArray -> inferArray(el, key)
        is JsonObject -> TypeDef.ObjectT(
            fields = inferFields(el)
        )

        JsonNull -> TypeDef.PrimitiveT(ScalarType.NULL)
    }

private fun inferArray(arr: JsonArray, key: String): TypeDef {
    if (arr.isEmpty()) return TypeDef.ArrayT(TypeDef.AnyT)

    val elements = arr.map { inferType(it, key) }
    return TypeDef.ArrayT(TypeDef.UnionT(elements.toSet()))
}

private fun inferPrimitive(p: JsonPrimitive): TypeDef =
    when {
        p.isString -> TypeDef.PrimitiveT(ScalarType.STRING)
        p.booleanOrNull != null -> TypeDef.PrimitiveT(ScalarType.BOOLEAN)
        p.intOrNull != null -> TypeDef.PrimitiveT(ScalarType.INT)
        p.doubleOrNull != null -> TypeDef.PrimitiveT(ScalarType.DOUBLE)
        p.contentOrNull == null -> TypeDef.PrimitiveT(ScalarType.NULL)
        else -> TypeDef.AnyT
    }

