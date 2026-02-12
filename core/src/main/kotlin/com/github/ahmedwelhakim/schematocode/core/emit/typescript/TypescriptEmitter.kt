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
import com.github.ahmedwelhakim.schematocode.core.resolve.SymbolTable

class TypescriptEmitter(
    private val options: TypescriptOptions
) : CodeEmitter {
    private var indentLevel = 0
    private val indentSize = 4

    private lateinit var symbols: SymbolTable
    private lateinit var config: GeneratorConfig
    private lateinit var naming: NamingStrategy

    override fun emit(
        plan: EmissionPlan,
        config: GeneratorConfig
    ): String = buildString {

        this@TypescriptEmitter.config = config
        this@TypescriptEmitter.naming = config.namingStrategyType.create()

        if (config.inlineObjects) {
            emitInlineRoot(plan.root)
            return@buildString
        }

        symbols = plan.symbols

        // Emit named models (Root first)
        plan.units
            .asReversed()
            .forEach { unit ->
                emitNamedObject(unit)
                appendLine()
            }

        // If root is not object, emit alias
        if (plan.root !is TypeDef.ObjectT) {
            emitRootAlias(plan.root)
        }
    }

    // ============================================================
    // Separate Model Mode
    // ============================================================

    private fun StringBuilder.emitNamedObject(unit: EmissionUnit) {

        when (options.modelKind) {
            TypescriptModelKind.INTERFACE ->
                appendLine("export interface ${unit.name} {")

            TypescriptModelKind.TYPE_ALIAS ->
                appendLine("export type ${unit.name} = {")
        }
        indentLevel++
        unit.type.fields.forEach { field ->
            appendLine("${indent()}${emitField(field, separate = true)}")
        }
        indentLevel--
        appendLine("}")
    }

    private fun StringBuilder.emitRootAlias(root: TypeDef) {
        appendLine(
            "export type ${config.name} = ${emitType(root, separate = true)}"
        )
    }

    // ============================================================
    // Inline Mode
    // ============================================================

    private fun StringBuilder.emitInlineRoot(root: TypeDef) {
        when (root) {

            is TypeDef.ObjectT -> {
                when (options.modelKind) {
                    TypescriptModelKind.INTERFACE ->
                        appendLine("export interface ${config.name} {")

                    TypescriptModelKind.TYPE_ALIAS ->
                        appendLine("export type ${config.name} = {")
                }

                indentLevel++
                root.fields.forEach { field ->
                    appendLine("${indent()}${emitField(field, separate = false)}")
                }

                indentLevel--
                appendLine("${indent()}}")
            }

            else -> {
                appendLine(
                    "export type ${config.name} = ${emitType(root, separate = false)}"
                )
            }
        }
    }

    // ============================================================
    // Field Emission
    // ============================================================

    private fun emitField(
        field: Field,
        separate: Boolean
    ): String {
        val name = naming.fieldName(field.name)
        val optional = if (field.optional) "?" else ""
        val type = emitType(field.type, separate)

        return "$name$optional: $type;"
    }

    // ============================================================
    // Type Emission
    // ============================================================

    private fun emitType(
        type: TypeDef,
        separate: Boolean
    ): String =
        when (type) {

            is TypeDef.PrimitiveT ->
                when (type.type) {
                    ScalarType.STRING -> "string"
                    ScalarType.INT,
                    ScalarType.DOUBLE -> "number"

                    ScalarType.BOOLEAN -> "boolean"
                    ScalarType.NULL -> "null"
                }

            TypeDef.AnyT ->
                "any"

            is TypeDef.ArrayT ->
                "${emitType(type.element, separate)}[]"

            is TypeDef.UnionT ->
                type.types.joinToString(" | ") {
                    emitType(it, separate)
                }

            is TypeDef.ObjectT ->
                if (separate) {
                    symbols.nameOf(type)
                } else {
                    buildInlineObject(type)
                }
        }

    private fun buildInlineObject(obj: TypeDef.ObjectT): String =
        buildString {
            appendLine("{")
            indentLevel++
            obj.fields.forEach {
                appendLine("${indent()}${emitField(it, separate = false)}")
            }
            indentLevel--
            append("${indent()}}")
        }

    private fun indent(): String = " ".repeat(indentLevel * indentSize)
}
