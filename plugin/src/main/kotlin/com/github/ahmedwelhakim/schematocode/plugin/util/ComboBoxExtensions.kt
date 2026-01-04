package com.github.ahmedwelhakim.schematocode.plugin.util

import com.github.ahmedwelhakim.schematocode.core.i18n.MessageKeyHolder
import com.github.ahmedwelhakim.schematocode.plugin.SchemaToCodeBundle
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.dsl.builder.Cell

fun <C : MessageKeyHolder> Cell<ComboBox<C>>.withEnumTranslation(
    keyProvider: (C) -> String
): Cell<ComboBox<C>> =
    applyToComponent {
        renderer = SimpleListCellRenderer.create { label, value, _ ->
            label.text =
                if (value != null)
                    SchemaToCodeBundle.message(keyProvider(value))
                else
                    ""
        }
    }

