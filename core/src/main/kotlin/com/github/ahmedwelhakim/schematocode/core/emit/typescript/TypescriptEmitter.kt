package com.github.ahmedwelhakim.schematocode.core.emit.typescript

import com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig
import com.github.ahmedwelhakim.schematocode.core.emit.CodeEmitter
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef

class TypescriptEmitter :
    com.github.ahmedwelhakim.schematocode.core.emit.CodeEmitter<com.github.ahmedwelhakim.schematocode.core.emit.typescript.TypescriptOptions> {
    private var indentSize = 2
    private var spaces = indentSize
    override fun emit(ir: com.github.ahmedwelhakim.schematocode.core.ir.TypeDef, config: com.github.ahmedwelhakim.schematocode.core.config.GeneratorConfig, options: com.github.ahmedwelhakim.schematocode.core.emit.typescript.TypescriptOptions): String = buildString {
        if (ir !is com.github.ahmedwelhakim.schematocode.core.ir.TypeDef.ObjectT) {
            throw IllegalArgumentException("Only object types are supported")
        }
        appendLine("export interface ${ir.name} {")
        ir.fields.forEach {
            val opt = if (it.optional) "?" else ""
            appendLine("${" ".repeat(spaces)}${it.name}$opt: ${emitType(it.type)};")
        }
        appendLine("}")
    }

    private fun emitType(type: com.github.ahmedwelhakim.schematocode.core.ir.TypeDef): String =
        when (type) {
            _root_ide_package_.com.github.ahmedwelhakim.schematocode.core.ir.TypeDef.StringT -> "string"
            _root_ide_package_.com.github.ahmedwelhakim.schematocode.core.ir.TypeDef.NumberT -> "number"
            _root_ide_package_.com.github.ahmedwelhakim.schematocode.core.ir.TypeDef.BooleanT -> "boolean"
            _root_ide_package_.com.github.ahmedwelhakim.schematocode.core.ir.TypeDef.NullT -> "unknown | null"
            _root_ide_package_.com.github.ahmedwelhakim.schematocode.core.ir.TypeDef.AnyT -> "any"
            is com.github.ahmedwelhakim.schematocode.core.ir.TypeDef.UnionT -> if (type.types.size == 1) emitType(type.types.first()) else "(${
                type.types.joinToString(
                    " | "
                ) { emitType(it) }
            })"

            is com.github.ahmedwelhakim.schematocode.core.ir.TypeDef.ArrayT -> "${emitType(type.element)}[]"
            is com.github.ahmedwelhakim.schematocode.core.ir.TypeDef.ObjectT -> buildString {
                spaces += indentSize
                appendLine("{")
                type.fields.forEach {
                    val opt = if (it.optional) "?" else ""
                    appendLine("${" ".repeat(spaces)}${it.name}$opt: ${emitType(it.type)};")
                }
                spaces -= indentSize
                append("${" ".repeat(spaces)}}")
            }

            is com.github.ahmedwelhakim.schematocode.core.ir.TypeDef.FormattedT -> emitType(type.base)
        }
}

