package com.github.ahmedwelhakim.schematocode.plugin.language

import com.github.ahmedwelhakim.schematocode.core.config.TargetLanguage

/**
 * Maps a core [TargetLanguage] to its corresponding IntelliJ [LanguageId].
 *
 * This is used to configure editors with syntax highlighting for the target language.
 *
 * @param language The target language from the core module.
 * @return The IntelliJ language ID for editor configuration.
 */
fun getLanguageID(language: TargetLanguage): LanguageId = when (language) {
    TargetLanguage.TYPESCRIPT -> LanguageId.TYPESCRIPT
}