package com.github.ahmedwelhakim.schematocode.core.emit

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import com.github.ahmedwelhakim.schematocode.core.resolve.TypeIdentity

data class ModelDeclaration(
    val name: String,
    val type: TypeDef.ObjectT,
    val typeIdentity: TypeIdentity
)
