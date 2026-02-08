package com.github.ahmedwelhakim.schematocode.core.emit

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef

data class EmissionUnit(
    val name: String,
    val type: TypeDef.ObjectT
)
