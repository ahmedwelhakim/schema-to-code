package com.github.ahmedwelhakim.schematocode.core.emit

import com.github.ahmedwelhakim.schematocode.core.ir.TypeDef

interface CodeEmitter {
    fun emit(root: TypeDef.ObjectT): String
}