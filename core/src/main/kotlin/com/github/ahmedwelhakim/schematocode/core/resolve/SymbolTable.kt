package com.github.ahmedwelhakim.schematocode.core.resolve

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import com.github.ahmedwelhakim.schematocode.core.naming.internal.toPascalCase

class SymbolTable {
    private val byType = mutableMapOf<SemanticKey, String>()
    private val byName = mutableMapOf<String, SemanticKey>()
    private val objectToSemanticKey = mutableMapOf<TypeDef, SemanticKey>()
    private val usedTimes = mutableMapOf<String, Int>()

    fun declare(type: TypeDef, semanticKey: SemanticKey, name: String) {
        var uniqueName: String = name.toPascalCase()
        if (byName.containsKey(uniqueName)) {
            usedTimes[name] = usedTimes.getOrDefault(name, 1) + 1
            uniqueName = "${name.toPascalCase()}${usedTimes[name]}"
        }
        objectToSemanticKey[type] = semanticKey
        byName[uniqueName] = semanticKey
        byType[semanticKey] = uniqueName

    }

    fun semanticKeyOf(type: TypeDef): SemanticKey = objectToSemanticKey[type] ?: error("Type $type is not declared")
    fun nameOf(type: TypeDef): String = byType[objectToSemanticKey[type]] ?: error("Type $type is not declared")
    fun nameOf(type: SemanticKey): String = byType[type] ?: error("Type $type is not declared")
}