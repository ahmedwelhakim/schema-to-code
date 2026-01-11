package com.github.ahmedwelhakim.schematocode.core.emit.typescript

import com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig
import com.github.ahmedwelhakim.schematocode.core.emit.CodeEmitter
import com.github.ahmedwelhakim.schematocode.core.ir.ScalarType
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import com.github.ahmedwelhakim.schematocode.core.naming.NamingStrategyType
import com.github.ahmedwelhakim.schematocode.core.naming.create
import com.github.ahmedwelhakim.schematocode.core.util.indent

class TypescriptEmitter(
    private val options: TypescriptOptions
) : CodeEmitter {
    private var indentSize = 4
    private var spacesCount = indentSize
    private val stringBuilder = StringBuilder()
    private lateinit var config: GeneratorConfig
    private val pascalNamingStrategy = NamingStrategyType.PASCAL.create()
    private val emittedModels = mutableSetOf<String>()
    override fun emit(ir: TypeDef, config: GeneratorConfig): String {
        this.config = config
        if (config.inlineObjects) {
            buildInlineModel(ir)
            emittedModels.add(stringBuilder.toString())
        } else {
            buildSeparateModels(ir)
        }

        return emittedModels.reversed().joinToString("")
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
            is TypeDef.UnionT -> if (type.types.size == 1) emitType(type.types.first()) else
                type.types.joinToString(
                    " | "
                ) { emitType(it) }


            is TypeDef.ArrayT -> if (type.element is TypeDef.UnionT) "(${emitType(type.element)})[]" else "${
                emitType(
                    type.element
                )
            }[]"

            is TypeDef.ObjectT -> buildString {
                val namingStrategy = config.namingStrategyType.create()
                spacesCount += indentSize
                appendLine("{")
                type.fields.forEach {
                    val name = namingStrategy.fieldName(it.name)
                    var fieldType = it.type
                    var opt = if (it.optional) "?" else ""
                    appendLine(
                        "${indent(spacesCount)}${name}${opt}: ${emitType(fieldType)};"
                    )

                }
                spacesCount -= indentSize
                append("${indent(spacesCount)}}")
            }

        }

    private fun buildInlineModel(ir: TypeDef) {
        when (ir) {
            is TypeDef.ObjectT -> {
                val namingStrategy = config.namingStrategyType.create()
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

            is TypeDef.PrimitiveT,
            is TypeDef.UnionT,
            is TypeDef.AnyT -> {
                stringBuilder.appendLine("export type ${config.name} = ${emitType(ir)}")
            }
        }
    }

    private fun emitNamedObject(obj: TypeDef.ObjectT) {
        val emittedObjectBuilder = StringBuilder()
        val namingStrategy = config.namingStrategyType.create()
        val modelName = pascalNamingStrategy.fieldName(obj.name)
        when (options.modelKind) {
            TypescriptModelKind.INTERFACE ->
                emittedObjectBuilder.appendLine("export interface ${modelName} {")

            TypescriptModelKind.TYPE_ALIAS ->
                emittedObjectBuilder.appendLine("export type ${modelName} = {")
        }

        obj.fields.forEach {
            val fieldName = namingStrategy.fieldName(it.name)
            val opt = if (it.optional) "?" else ""
            val fieldType = when (it.type) {
                is TypeDef.ObjectT ->
                    pascalNamingStrategy.fieldName(it.type.name)

                is TypeDef.ArrayT -> when (val el = it.type.element) {
                    is TypeDef.ObjectT ->
                        "${pascalNamingStrategy.fieldName(el.name)}[]"

                    else ->
                        "${emitType(el)}[]"
                }

                else -> emitType(it.type)
            }
            emittedObjectBuilder.appendLine(
                "${indent(indentSize)}$fieldName${opt}: $fieldType;"
            )
        }

        emittedObjectBuilder.appendLine("}")
        emittedObjectBuilder.appendLine()
        emittedModels.add(emittedObjectBuilder.toString())
    }

    private fun emitRootAlias(ir: TypeDef) {
        val emittedObjectBuilder = StringBuilder()
        when (ir) {
            is TypeDef.ArrayT -> {
                val elementName = when (val element = ir.element) {
                    is TypeDef.ObjectT ->
                        pascalNamingStrategy.fieldName(element.name)

                    else ->
                        emitType(element)
                }

                emittedObjectBuilder.appendLine(
                    "export type ${config.name} = ${elementName}[]"
                )
                emittedObjectBuilder.appendLine()
            }

            is TypeDef.ObjectT -> {}

            else -> {
                emittedObjectBuilder.appendLine(
                    "export type ${config.name} = ${emitType(ir)}"
                )
                emittedObjectBuilder.appendLine()
            }
        }
        emittedModels.add(emittedObjectBuilder.toString())
    }

    private fun collectAndEmitModels(type: TypeDef) {
        when (type) {

            is TypeDef.ObjectT -> {
                // First recurse into fields
                type.fields.forEach { collectAndEmitModels(it.type) }

                // Then emit this object
                emitNamedObject(type)
            }

            is TypeDef.ArrayT ->
                collectAndEmitModels(type.element)

            is TypeDef.UnionT ->
                type.types.forEach { collectAndEmitModels(it) }

            is TypeDef.PrimitiveT,
            TypeDef.AnyT -> Unit
        }
    }

    private fun buildSeparateModels(ir: TypeDef) {
        collectAndEmitModels(ir)
        emitRootAlias(ir)
    }

}

