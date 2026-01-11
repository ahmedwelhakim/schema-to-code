package com.github.ahmedwelhakim.schematocode.core.resolve

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef

internal class SymbolTable {
    private val byType = mutableMapOf<TypeDef.ObjectT, String>()
    private val byName = mutableMapOf<String, TypeDef.ObjectT>()
    private val usedTimes = mutableMapOf<String, Int>()

    fun declare(type: TypeDef.ObjectT, name: String) {
        var uniqueName: String = name
        if (byName.containsKey(name)) {
            usedTimes[name] = usedTimes.getOrDefault(name, 1) + 1
            uniqueName = "$name${usedTimes[name]}"
        }
        byName[uniqueName] = type
        byType[type] = uniqueName
        type.fields.forEach {
            when (it.type) {
                is TypeDef.ObjectT -> declare(it.type, it.name)
                is TypeDef.ArrayT -> declare(it.type, it.name)
                is TypeDef.UnionT -> it.type.types.forEach { declare(it, uniqueName) }
                else -> Unit
            }
        }
    }

    fun declare(type: TypeDef.ArrayT, name: String) {
        val element = type.element
        val resolvedName = name + "Item"
        when (element) {
            is TypeDef.ObjectT -> declare(element, resolvedName)
            is TypeDef.ArrayT -> declare(element, resolvedName)
            is TypeDef.UnionT -> element.types.forEach { declare(it, name) }
            else -> Unit
        }
    }

    fun declare(type: TypeDef, name: String) {
        when (type) {
            is TypeDef.ObjectT -> declare(type, type.name)
            is TypeDef.ArrayT -> declare(type, name)
            is TypeDef.UnionT -> type.types.forEach { declare(it, name) }
            else -> Unit
        }
    }

    fun nameOf(type: TypeDef.ObjectT): String = byType[type] ?: error("Type $type is not declared")
}