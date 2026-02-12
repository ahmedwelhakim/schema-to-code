package com.github.ahmedwelhakim.schematocode.core.emit.typescript

import com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig
import com.github.ahmedwelhakim.schematocode.core.emit.CodeEmitter
import com.github.ahmedwelhakim.schematocode.core.emit.EmissionPlan
import com.github.ahmedwelhakim.schematocode.core.emit.EmissionUnit
import com.github.ahmedwelhakim.schematocode.core.ir.Field
import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import com.github.ahmedwelhakim.schematocode.core.naming.NamingStrategy
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

        // Build lookup table using object instance identity
        symbols = plan.units.associate { it.semanticKey to it.name }

        // Emit in reversed order (Root first)
        plan.units
            .asReversed()
            .forEach { unit ->
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
            appendLine("  ${emitField(field, naming)}")
        }

        appendLine("}")
    }

    // ----------------------------------------------------------------------

    private fun emitField(
        field: Field,
        naming: NamingStrategy
    ): String {
        val name = naming.fieldName(field.name)
        val optional = if (field.optional) "?" else ""
        val type = emitType(field.type, field.name)

        return "$name$optional: $type;"
    }

    // ----------------------------------------------------------------------

    private fun emitType(type: TypeDef, hintName: String): String =
        when (type) {

            is TypeDef.PrimitiveT ->
                when (type.type) {
                    ScalarType.STRING -> "string"
                    ScalarType.NUMBER -> "number"
                    ScalarType.BOOLEAN -> "boolean"
                    ScalarType.NULL -> "null"
                }

            TypeDef.AnyT ->
                "any"

            is TypeDef.ArrayT ->
                "${emitType(type.element, hintName.plus("Item"))}[]"

            is TypeDef.UnionT ->
                type.types.joinToString(" | ") { emitType(it, hintName) }

            is TypeDef.ObjectT -> {
                val key = SemanticKey(
                    nameHint = hintName,
                    structure = type.structuralKey()
                )
                symbols[key]
                    ?: error("Undefined emitted type for object: ${type.structuralKey()}")
            }
        }
}
