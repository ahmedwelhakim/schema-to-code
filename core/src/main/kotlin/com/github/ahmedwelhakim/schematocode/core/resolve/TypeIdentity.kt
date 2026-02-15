package com.github.ahmedwelhakim.schematocode.core.resolve

import com.github.ahmedwelhakim.schematocode.core.normalize.StructuralKey

data class TypeIdentity(
    val nameHint: String?,
    val structure: StructuralKey
)