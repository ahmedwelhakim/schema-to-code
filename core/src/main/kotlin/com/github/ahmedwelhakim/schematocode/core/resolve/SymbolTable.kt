package com.github.ahmedwelhakim.schematocode.core.resolve

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import com.github.ahmedwelhakim.schematocode.core.naming.internal.toPascalCase

class SymbolTable {
    private val byType = mutableMapOf<TypeIdentity, String>()
    private val byName = mutableMapOf<String, TypeIdentity>()
    private val objectToTypeIdentity = mutableMapOf<TypeDef, TypeIdentity>()
    private val usedTimes = mutableMapOf<String, Int>()

    fun declare(type: TypeDef, typeIdentity: TypeIdentity, name: String) {
        var uniqueName: String = name.toPascalCase()
        if (byName.containsKey(uniqueName)) {
            usedTimes[name] = usedTimes.getOrDefault(name, 1) + 1
            uniqueName = "${name.toPascalCase()}${usedTimes[name]}"
        }
        objectToTypeIdentity[type] = typeIdentity
        byName[uniqueName] = typeIdentity
        byType[typeIdentity] = uniqueName

    }

    fun semanticKeyOf(type: TypeDef): TypeIdentity = objectToTypeIdentity[type] ?: error("Type $type is not declared")
    fun nameOf(type: TypeDef): String = byType[objectToTypeIdentity[type]] ?: error("Type $type is not declared")
    fun nameOf(type: TypeIdentity): String = byType[type] ?: error("Type $type is not declared")
}