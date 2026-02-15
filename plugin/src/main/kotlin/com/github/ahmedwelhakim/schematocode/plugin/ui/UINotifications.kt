package com.github.ahmedwelhakim.schematocode.plugin.ui

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object UINotifications {
    fun showCopied(project: Project) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("SchemaToCode")
            .createNotification(
                "Copied to clipboard",
                NotificationType.INFORMATION
            )
            .notify(project)
    }
}