package com.github.ahmedwelhakim.schematocode.core.emit

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef
import com.github.ahmedwelhakim.schematocode.core.resolve.SemanticKey

data class EmissionUnit(
    val name: String,
    val type: TypeDef.ObjectT,
    val semanticKey: SemanticKey
)
