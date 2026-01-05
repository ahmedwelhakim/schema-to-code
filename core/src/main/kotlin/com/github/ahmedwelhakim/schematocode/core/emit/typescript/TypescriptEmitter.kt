package com.github.ahmedwelhakim.schematocode.core.emit.typescript

import com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig
import com.github.ahmedwelhakim.schematocode.core.emit.CodeEmitter
import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import com.github.ahmedwelhakim.schematocode.core.naming.create
import com.github.ahmedwelhakim.schematocode.core.util.indent

class TypescriptEmitter(
    private val options: TypescriptOptions
) : CodeEmitter {
    private var indentSize = 4
    private var spacesCount = indentSize
    private val stringBuilder = StringBuilder()
    private lateinit var config: GeneratorConfig
    override fun emit(ir: TypeDef, config: GeneratorConfig): String {
        this.config = config
        buildInlineModel(ir)

        return stringBuilder.toString()
    }

    private fun emitType(type: TypeDef): String =
        when (type) {
            is TypeDef.PrimitiveT -> when (type.type) {
                ScalarType.STRING -> "string"
                ScalarType.NUMBER -> "number"
                ScalarType.BOOLEAN -> "boolean"
                ScalarType.NULL -> "null"
            }

            TypeDef.AnyT -> "any"
            is TypeDef.UnionT -> if (type.types.size == 1) emitType(type.types.first()) else "(${
                type.types.joinToString(
                    " | "
                ) { emitType(it) }
            })"

            is TypeDef.ArrayT -> "${emitType(type.element)}[]"
            is TypeDef.ObjectT -> buildString {
                val namingStrategy = config.namingStrategyType.create()
                spacesCount += indentSize
                appendLine("{")
                type.fields.forEach {
                    val opt = if (it.optional) "?" else ""
                    val name = config.namingStrategyType.create().fieldName(it.name)
                    appendLine("${indent(spacesCount)}${name}$opt: ${emitType(it.type)};")
                }
                spacesCount -= indentSize
                append("${" ".repeat(spacesCount)}}")
            }

        }

    private fun buildInlineModel(ir: TypeDef) {
        val namingStrategy = config.namingStrategyType.create()
        when (ir) {
            is TypeDef.ObjectT -> {
                when (options.modelKind) {
                    TypescriptModelKind.INTERFACE -> stringBuilder.appendLine("export interface ${config.name} {")
                    TypescriptModelKind.TYPE_ALIAS -> stringBuilder.appendLine("export type ${config.name} = {")
                }
                ir.fields.forEach {
                    val name = namingStrategy.fieldName(it.name)
                    stringBuilder.appendLine("${indent(spacesCount)}${name}: ${emitType(it.type)};")
                }
                stringBuilder.appendLine("}")
            }

            is TypeDef.ArrayT -> {
                stringBuilder.appendLine("export type ${config.name} = ${emitType(ir.element)}")
            }

            is TypeDef.PrimitiveT -> {
                stringBuilder.appendLine("export type ${config.name} = ${emitType(ir)}")
            }

            is TypeDef.UnionT -> {
                stringBuilder.appendLine("export type ${config.name} = ${emitType(ir)}")
            }

            is TypeDef.AnyT -> {
                stringBuilder.appendLine("export type ${config.name} = ${emitType(ir)}")
            }
        }
    }


}

