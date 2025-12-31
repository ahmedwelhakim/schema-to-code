package com.github.ahmedwelhakim.schematocode.core.emit.typescript

import com.github.ahmedwelhakim.schematocode.core.emit.CodeEmitter
import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef

class TypescriptEmitter : CodeEmitter {
    private var indentSize = 2
    private var spaces = indentSize
    override fun emit(root: TypeDef.ObjectT): String = buildString {
        appendLine("export interface ${root.name} {")
        root.fields.forEach {
            val opt = if (it.optional) "?" else ""
            appendLine("${" ".repeat(spaces)}${it.name}$opt: ${emitType(it.type)};")
        }
        appendLine("}")
    }

    private fun emitType(type: TypeDef): String =
        when (type) {
            TypeDef.StringT -> "string"
            TypeDef.NumberT -> "number"
            TypeDef.BooleanT -> "boolean"
            TypeDef.NullT -> "unknown | null"
            TypeDef.AnyT -> "any"
            is TypeDef.UnionT -> if(type.types.size == 1) emitType(type.types.first()) else "(${type.types.joinToString(" | ") { emitType(it) }})"
            is TypeDef.ArrayT -> "${emitType(type.element)}[]"
            is TypeDef.ObjectT -> buildString {
                spaces += indentSize
                appendLine("{")
                type.fields.forEach {
                    val opt = if (it.optional) "?" else ""
                    appendLine("${" ".repeat(spaces)}${it.name}$opt: ${emitType(it.type)};")
                }
                spaces -= indentSize
                append("${" ".repeat(spaces)}}")
            }
            is TypeDef.FormattedT -> emitType(type.base)
        }


}