package com.github.ahmedwelhakim.schematocode.core.emit.typescript

import com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig
import com.github.ahmedwelhakim.schematocode.core.config.ModelEmissionMode
import com.github.ahmedwelhakim.schematocode.core.emit.CodeEmitter
import com.github.ahmedwelhakim.schematocode.core.emit.ModelDeclaration
import com.github.ahmedwelhakim.schematocode.core.emit.ModelPlan
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
        plan: ModelPlan,
        config: GeneratorConfig
    ): String = buildString {

        this@TypescriptEmitter.config = config
        this@TypescriptEmitter.naming = config.namingStrategyType.create()

        if (config.emissionMode == ModelEmissionMode.NESTED) {
            emitRootInline(plan.root)
            return@buildString
        }

        symbols = plan.symbols

        plan.units
            .asReversed()
            .forEach { unit ->
                emitNamedModel(unit)
                line()
            }

        if (plan.root !is TypeDef.ObjectT) {
            line("export type ${config.name} = ${emitType(plan.root, true)}")
        }
    }

    // ============================================================
    // Separate Mode
    // ============================================================

    private fun StringBuilder.emitNamedModel(unit: ModelDeclaration) {
        val header = when (options.modelKind) {
            TypescriptModelKind.INTERFACE ->
                "export interface ${unit.name}"

            TypescriptModelKind.TYPE_ALIAS ->
                "export type ${unit.name} ="
        }

        block(header) {
            emitObjectBody(unit.type, separate = true)
        }
    }

    // ============================================================
    // Inline Mode
    // ============================================================

    private fun StringBuilder.emitRootInline(root: TypeDef) {
        when (root) {

            is TypeDef.ObjectT -> {
                val header = when (options.modelKind) {
                    TypescriptModelKind.INTERFACE ->
                        "export interface ${config.name}"

                    TypescriptModelKind.TYPE_ALIAS ->
                        "export type ${config.name} ="
                }

                block(header) {
                    emitObjectBody(root, separate = false)
                }
            }

            else -> {
                line("export type ${config.name} = ${emitType(root, false)}")
            }
        }
    }

    // ============================================================
    // Shared Object Body
    // ============================================================

    private fun StringBuilder.emitObjectBody(
        obj: TypeDef.ObjectT,
        separate: Boolean
    ) {
        obj.fields.forEach { field ->
            line(emitField(field, separate))
        }
    }

    // ============================================================
    // Field
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
    // Type
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
                if (separate)
                    symbols.nameOf(type)
                else
                    buildInlineObject(type)
        }

    private fun buildInlineObject(obj: TypeDef.ObjectT): String =
        buildString {
            separateBlock() {
                emitObjectBody(obj, false)
            }
        }

    // ============================================================
    // Pretty Printer Helpers
    // ============================================================

    private fun StringBuilder.block(
        header: String,
        body: StringBuilder.() -> Unit
    ) {
        line("$header {")
        indentLevel++
        body()
        indentLevel--
        line("}")
    }

    private fun StringBuilder.separateBlock(
        body: StringBuilder.() -> Unit
    ) {
        appendLine("{")
        indentLevel++
        body()
        indentLevel--
        append("${indent()}}")
    }

    private fun StringBuilder.line(text: String = "") {
        if (text.isEmpty()) {
            appendLine()
        } else {
            appendLine("${indent()}$text")
        }
    }

    private fun indent(): String =
        " ".repeat(indentLevel * indentSize)
}
