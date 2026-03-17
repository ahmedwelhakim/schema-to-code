package com.github.ahmedwelhakim.schematocode.plugin.ui

import com.github.ahmedwelhakim.schematocode.plugin.SchemaToCodeBundle
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

/**
 * Utility object for showing UI notifications in the plugin.
 *
 * Uses IntelliJ's notification system to display balloon notifications
 * to the user.
 */
object UINotifications {
    /**
     * Shows a notification indicating content was copied to clipboard.
     *
     * @param project The project context for the notification.
     */
    fun showCopied(project: Project) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("SchemaToCode")
            .createNotification(
                SchemaToCodeBundle.message("notification.copied"),
                NotificationType.INFORMATION
            )
            .notify(project)
    }
}