package com.autoresponder.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.autoresponder.app.service.AutoReplyNotificationService

class RestartServiceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == Intent.ACTION_MY_PACKAGE_REPLACED
        ) {
            val serviceIntent = Intent(context, AutoReplyNotificationService::class.java)
            context.startService(serviceIntent)
        }
    }
}

