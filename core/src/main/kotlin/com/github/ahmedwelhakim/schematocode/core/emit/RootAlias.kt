package com.github.ahmedwelhakim.schematocode.core.emit

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef

data class RootAlias(
    val name: String,
    val target: TypeDef
)
