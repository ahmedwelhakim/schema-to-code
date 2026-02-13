package com.github.ahmedwelhakim.schematocode.core.resolve

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef

data class TypeIdentity(
    val nameHint: String?,
    val structure: TypeDef
)