package com.github.ahmedwelhakim.schematocode.core.emit.typescript

import com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig
import com.github.ahmedwelhakim.schematocode.core.emit.CodeEmitter
import com.github.ahmedwelhakim.schematocode.core.emit.EmissionPlan
import com.github.ahmedwelhakim.schematocode.core.emit.EmissionUnit
import com.github.ahmedwelhakim.schematocode.core.ir.Field
import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import com.github.ahmedwelhakim.schematocode.core.naming.create
import com.github.ahmedwelhakim.schematocode.core.resolve.SemanticKey
import com.github.ahmedwelhakim.schematocode.core.resolve.structuralKey

class TypescriptEmitter(
    private val options: TypescriptOptions
) : CodeEmitter {
    private lateinit var symbols: Map<SemanticKey, String>
    override fun emit(
        plan: EmissionPlan,
        config: GeneratorConfig
    ): String = buildString {

        symbols =
            plan.units.associate {
                SemanticKey(it.type.name, it.type.structuralKey()) to it.name
            }
        plan.units.reversed().forEach { unit ->
            emitObject(unit, config)
            appendLine()
        }


    }

    // ----------------------------------------------------------------------

    private fun StringBuilder.emitObject(
        unit: EmissionUnit,
        config: GeneratorConfig
    ) {
        val naming = config.namingStrategyType.create()

        when (options.modelKind) {
            TypescriptModelKind.INTERFACE ->
                appendLine("export interface ${unit.name} {")

            TypescriptModelKind.TYPE_ALIAS ->
                appendLine("export type ${unit.name} = {")
        }

        unit.type.fields.forEach { field ->
            appendLine(
                "  ${emitField(field, naming)}"
            )
        }

        appendLine("}")
    }

    private fun emitField(
        field: Field,
        naming: com.github.ahmedwelhakim.schematocode.core.naming.NamingStrategy
    ): String {
        val name = naming.fieldName(field.name)
        val optional = if (field.optional) "?" else ""
        val type = emitType(field.type)

        return "$name$optional: $type;"
    }


    // ----------------------------------------------------------------------

    private fun emitType(type: TypeDef): String =
        when (type) {

            is TypeDef.PrimitiveT -> when (type.type) {
                ScalarType.STRING -> "string"
                ScalarType.NUMBER -> "number"
                ScalarType.BOOLEAN -> "boolean"
                ScalarType.NULL -> "null"
            }

            TypeDef.AnyT ->
                "any"

            is TypeDef.ArrayT ->
                "${emitType(type.element)}[]"

            is TypeDef.UnionT ->
                type.types.joinToString(" | ") { emitType(it) }

            is TypeDef.ObjectT -> {

                val key = SemanticKey(
                    nameHint = type.name,
                    structure = type.structuralKey()
                )
                symbols[key] ?: error("Undefined type ${type.structuralKey()}")
            }
        }
}
