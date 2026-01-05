package com.github.ahmedwelhakim.schematocode.core.emit

import com.github.ahmedwelhakim.schematocode.core.options.OptionKey

interface LanguageOptions {
    fun <T> get(key: OptionKey): T?
    fun with(key: OptionKey, value: Any): LanguageOptions
}