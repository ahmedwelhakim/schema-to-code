package com.github.ahmedwelhakim.schematocode.core.resolve

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import com.github.ahmedwelhakim.schematocode.core.naming.internal.toPascalCase

/**
 * Symbol table that maps types to their unique names.
 * Handles name collision resolution by appending numeric suffixes.
 */
class SymbolTable {
    private val byType = mutableMapOf<TypeIdentity, String>()
    private val byName = mutableMapOf<String, TypeIdentity>()
    private val objectToTypeIdentity = mutableMapOf<TypeDef, TypeIdentity>()
    private val usedTimes = mutableMapOf<String, Int>()

    /**
     * Declares a type with the given identity and name hint.
     * If the name is already used, a numeric suffix is appended.
     *
     * @param type The type definition to declare.
     * @param typeIdentity The semantic identity of the type.
     * @param name The name hint for the type.
     */
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

    /** Returns the semantic key for a declared type. */
    fun typeIdentityOf(type: TypeDef): TypeIdentity =
        objectToTypeIdentity[type] ?: error("Type $type is not declared")

    /** Returns the unique name for a declared type. */
    fun nameOf(type: TypeDef): String =
        byType[objectToTypeIdentity[type]] ?: error("Type $type is not declared")

    /** Returns the unique name for a type identity. */
    fun nameOf(type: TypeIdentity): String =
        byType[type] ?: error("Type $type is not declared")
}