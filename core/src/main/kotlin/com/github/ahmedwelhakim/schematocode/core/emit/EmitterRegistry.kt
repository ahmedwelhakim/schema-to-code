package com.github.ahmedwelhakim.schematocode.core.emit

import com.github.ahmedwelhakim.schematocode.core.config.Language
import com.github.ahmedwelhakim.schematocode.core.emit.typescript.TypescriptEmitter

object EmitterRegistry {
    fun emitterFor(language: Language): CodeEmitter<*> =
        when (language) {
            Language.TYPESCRIPT -> TypescriptEmitter()
        }
}
