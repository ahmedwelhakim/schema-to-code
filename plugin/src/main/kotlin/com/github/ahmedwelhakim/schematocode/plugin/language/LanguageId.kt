package com.github.ahmedwelhakim.schematocode.plugin.language

/**
 * Those are the language ids used by `com.intellij.lang.Language.findLanguageByID` to identify the language
 * @see com.intellij.lang.Language.findLanguageByID
 **/
enum class LanguageId(val id: String) {

    JAVA("JAVA"),

    JSON("JSON5"),

    TYPESCRIPT("TypeScript"),

    KOTLIN("kotlin");

    override fun toString(): String = id
}