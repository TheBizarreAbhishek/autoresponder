package com.autoresponder.app.helper

import android.content.ComponentName
import android.content.Context
import android.provider.Settings
import android.text.TextUtils

object NotificationHelper {
    
    fun isNotificationServicePermissionGranted(context: Context): Boolean {
        val pkgName = context.packageName
        val flat = Settings.Secure.getString(
            context.contentResolver,
            "enabled_notification_listeners"
        ) ?: return false

        val names = flat.split(":")
        for (name in names) {
            val cn = ComponentName.unflattenFromString(name)
            if (cn != null && TextUtils.equals(pkgName, cn.packageName)) {
                return true
            }
        }
        return false
    }

    fun isNotificationListenerServiceRunning(context: Context): Boolean {
        return isNotificationServicePermissionGranted(context)
    }
}

