package com.github.ahmedwelhakim.schematocode.core.options

enum class OptionKeys(val key: String) {
    MODEL_KIND("modelKind"),
    NAMING_STRATEGY("namingStrategy");

    override fun toString(): String {
        return key
    }
}