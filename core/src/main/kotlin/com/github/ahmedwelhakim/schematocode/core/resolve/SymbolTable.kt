package com.github.ahmedwelhakim.schematocode.core.resolve

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import com.github.ahmedwelhakim.schematocode.core.naming.internal.toPascalCase

class SymbolTable {
    private val byType = mutableMapOf<TypeDef.ObjectT, String>()
    private val byName = mutableMapOf<String, TypeDef.ObjectT>()
    private val usedTimes = mutableMapOf<String, Int>()

    fun declare(type: TypeDef.ObjectT, name: String) {
        var uniqueName: String = name.toPascalCase()
        if (byName.containsKey(name)) {
            usedTimes[name] = usedTimes.getOrDefault(name, 1) + 1
            uniqueName = "$name${usedTimes[name]}"
        }
        byName[uniqueName] = type
        byType[type] = uniqueName

    }


    fun nameOf(type: TypeDef.ObjectT): String = byType[type] ?: error("Type $type is not declared")
}