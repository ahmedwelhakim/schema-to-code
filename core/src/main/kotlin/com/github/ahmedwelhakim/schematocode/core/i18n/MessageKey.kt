package com.github.ahmedwelhakim.schematocode.core.i18n

/**
 * All Message keys for i18n bundle that should be added to the plugin's resource bundle
 */
enum class MessageKey(override val bundleKey: String) : MessageKeyHolder {
    MODEL_KIND("languageOptions.modelKind"),
    NAMING_STRATEGY("languageOptions.namingStrategy"),
    TYPESCRIPT("languages.typescript"),
    JSON("languages.json"),
    JAVA("languages.java"),
    KOTLIN("languages.kotlin"),
    INTERFACE("modelKinds.interface"),
    TYPE_ALIAS("modelKinds.typeAlias"),
    PRESERVE("namingStrategies.preserve"),
    PASCAL("namingStrategies.pascal"),
    CAMEL("namingStrategies.camel"),
    SNAKE("namingStrategies.snake"),
    KEBAB("namingStrategies.kebab"),
    IDENTITY("namingStrategies.identity"),
}