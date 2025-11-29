package com.autoresponder.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.RemoteInput
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import com.autoresponder.app.R
import com.autoresponder.app.helper.MessageHandler
import com.autoresponder.app.helper.PlatformDetector
import com.autoresponder.app.ui.MainActivity
import com.autoresponder.app.model.Message
import java.util.HashSet

class AutoReplyNotificationService : NotificationListenerService() {

    companion object {
        private const val TAG = "AutoReplyService"
        private const val NOTIFICATION_CHANNEL_ID = "auto_reply_service_channel"
    }

    private val respondedMessages = HashSet<String>()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var messageHandler: MessageHandler
    private lateinit var dbHelper: com.autoresponder.app.database.DatabaseHelper
    private var botReplyMessage: String = ""

    // Supported platform packages
    private val supportedPlatforms = mapOf(
        "com.whatsapp" to "whatsapp",
        "com.whatsapp.w4b" to "whatsapp", // WhatsApp Business
        "com.instagram.android" to "instagram",
        "com.facebook.orca" to "facebook", // Facebook Messenger
        "org.telegram.messenger" to "telegram",
        "com.twitter.android" to "twitter"
    )

    override fun onCreate() {
        super.onCreate()
        messageHandler = MessageHandler(this)
        dbHelper = com.autoresponder.app.database.DatabaseHelper(this)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        createNotificationChannel()
        startForegroundService()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        val packageName = sbn.packageName
        val platform = supportedPlatforms[packageName] ?: return

        // Check if this platform is enabled
        if (!isPlatformEnabled(platform)) {
            Log.d(TAG, "Platform $platform is disabled")
            return
        }

        val extras = sbn.notification.extras
        val messageId = sbn.key
        val title = extras.getString(Notification.EXTRA_TITLE)
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)

        // Check if we've already responded to this message
        if (respondedMessages.contains(messageId)) {
            return
        }

        // Skip if message is from the user themselves
        if (isMessageFromUser(text)) {
            Log.d(TAG, "Skipping reply: Message is from user")
            return
        }

        // Skip if chat is active
        if (isChatActive(sbn)) {
            Log.d(TAG, "Skipping reply: Chat is currently active")
            return
        }

        // Check if notification has reply action
        val actions = sbn.notification.actions
        if (actions == null || actions.isEmpty()) {
            Log.d(TAG, "Skipping reply: No reply actions available")
            return
        }

        // Check if any action has remote input (reply capability)
        var hasReplyAction = false
        for (action in actions) {
            if (action.remoteInputs != null && action.remoteInputs.isNotEmpty()) {
                hasReplyAction = true
                break
            }
        }

        if (!hasReplyAction) {
            Log.d(TAG, "Skipping reply: No reply action found in notification")
            return
        }

        // Add this message to the set of responded messages
        respondedMessages.add(messageId)

        // Process the message and send auto-reply
        if (text != null && text.isNotEmpty()) {
            val senderMessage = text.toString()

            if (sharedPreferences.getBoolean("is_bot_enabled", true)) {
                val maxReply = sharedPreferences.getString("max_reply", "100")?.toIntOrNull() ?: 100

                messageHandler.getAllMessagesBySender(title ?: "Unknown", platform, object : MessageHandler.OnMessagesRetrievedListener {
                    override fun onMessagesRetrieved(messages: List<Message>) {
                        if (messages.size < maxReply) {
                            val groupReplyEnabled = sharedPreferences.getBoolean("is_group_reply_enabled", false)

                            if (groupReplyEnabled) {
                                processAutoReply(sbn, title ?: "Unknown", senderMessage, messageId, platform)
                            } else {
                                if (!isGroupMessage(title)) {
                                    processAutoReply(sbn, title ?: "Unknown", senderMessage, messageId, platform)
                                }
                            }
                        }
                    }
                })
            }
        }

        // Clear the set if it reaches size 50
        if (respondedMessages.size > 50) {
            respondedMessages.clear()
        }
    }

    private fun processAutoReply(
        sbn: StatusBarNotification,
        sender: String,
        message: String,
        messageId: String,
        platform: String
    ) {
        val actions = sbn.notification.actions ?: return

        for (action in actions) {
            if (action.remoteInputs != null && action.remoteInputs.isNotEmpty()) {
                val replyPrefix = sharedPreferences.getString(
                    "reply_prefix_message",
                    getString(R.string.default_reply_prefix)
                )?.trim() ?: ""

                // First, check if there's a matching preset
                val matchingPreset = dbHelper.findMatchingPreset(message)
                if (matchingPreset != null) {
                    // Use preset reply
                    botReplyMessage = "$replyPrefix ${matchingPreset.reply}".trim()
                    val botReplyWithoutPrefix = botReplyMessage.replace(replyPrefix, "").trim()
                    messageHandler.handleIncomingMessage(sender, message, botReplyWithoutPrefix, platform)
                    sendWithNaturalDelay(action, botReplyMessage, messageId)
                    return
                }

                // If no preset matches, check if AI is enabled
                if (isAIConfigured() && sharedPreferences.getBoolean("is_ai_reply_enabled", false)) {
                    val llmModel = sharedPreferences.getString("llm_model", "gpt-4o-mini")?.lowercase() ?: "gpt-4o-mini"

                    when {
                        llmModel.startsWith("gpt") -> {
                            generateGPTReply(sender, message, platform) { reply ->
                                botReplyMessage = "$replyPrefix $reply".trim()
                                val botReplyWithoutPrefix = botReplyMessage.replace(replyPrefix, "").trim()
                                messageHandler.handleIncomingMessage(sender, message, botReplyWithoutPrefix, platform)
                                sendWithNaturalDelay(action, botReplyMessage, messageId)
                            }
                        }
                        llmModel.startsWith("gemini") -> {
                            generateGeminiReply(sender, message, platform) { reply ->
                                botReplyMessage = "$replyPrefix $reply".trim()
                                val botReplyWithoutPrefix = botReplyMessage.replace(replyPrefix, "").trim()
                                messageHandler.handleIncomingMessage(sender, message, botReplyWithoutPrefix, platform)
                                sendWithNaturalDelay(action, botReplyMessage, messageId)
                            }
                        }
                        llmModel.startsWith("deepseek") -> {
                            generateDeepSeekReply(sender, message, platform) { reply ->
                                botReplyMessage = "$replyPrefix $reply".trim()
                                val botReplyWithoutPrefix = botReplyMessage.replace(replyPrefix, "").trim()
                                messageHandler.handleIncomingMessage(sender, message, botReplyWithoutPrefix, platform)
                                sendWithNaturalDelay(action, botReplyMessage, messageId)
                            }
                        }
                        llmModel.startsWith("custom") -> {
                            generateCustomReply(sender, message, platform) { reply ->
                                botReplyMessage = "$replyPrefix $reply".trim()
                                val botReplyWithoutPrefix = botReplyMessage.replace(replyPrefix, "").trim()
                                messageHandler.handleIncomingMessage(sender, message, botReplyWithoutPrefix, platform)
                                sendWithNaturalDelay(action, botReplyMessage, messageId)
                            }
                        }
                    }
                } else {
                    // Default reply message
                    botReplyMessage = (
                        replyPrefix + " " + sharedPreferences.getString(
                            "default_reply_message",
                            getString(R.string.default_reply_message)
                        )
                    ).trim()
                    val botReplyWithoutPrefix = botReplyMessage.replace(replyPrefix, "").trim()
                    messageHandler.handleIncomingMessage(sender, message, botReplyWithoutPrefix, platform)
                    sendWithNaturalDelay(action, botReplyMessage, messageId)
                }

                break
            }
        }
    }

    private fun generateGPTReply(sender: String, message: String, platform: String, callback: (String) -> Unit) {
        val generator = com.autoresponder.app.api.ChatGPTReplyGenerator(this, sharedPreferences, messageHandler)
        generator.generateReply(sender, message, platform, callback)
    }

    private fun generateGeminiReply(sender: String, message: String, platform: String, callback: (String) -> Unit) {
        val generator = com.autoresponder.app.api.GeminiReplyGenerator(this, sharedPreferences, messageHandler)
        generator.generateReply(sender, message, platform, callback)
    }

    private fun generateDeepSeekReply(sender: String, message: String, platform: String, callback: (String) -> Unit) {
        // TODO: Implement DeepSeek reply generation - for now use default
        val defaultReply = sharedPreferences.getString("default_reply_message", getString(R.string.default_reply_message)) ?: ""
        callback(defaultReply)
    }

    private fun generateCustomReply(sender: String, message: String, platform: String, callback: (String) -> Unit) {
        // TODO: Implement Custom API reply generation - for now use default
        val defaultReply = sharedPreferences.getString("default_reply_message", getString(R.string.default_reply_message)) ?: ""
        callback(defaultReply)
    }

    private fun send(action: Notification.Action, botReplyMessage: String) {
        val remoteInput = action.remoteInputs?.get(0) ?: return

        val intent = Intent()
        val bundle = Bundle()
        bundle.putCharSequence(remoteInput.resultKey, botReplyMessage)

        RemoteInput.addResultsToIntent(arrayOf(remoteInput), intent, bundle)

        try {
            action.actionIntent?.send(this, 0, intent)
        } catch (e: PendingIntent.CanceledException) {
            Log.e(TAG, "sendAutoReply: ", e)
        }
    }

    private fun sendWithNaturalDelay(action: Notification.Action, botReplyMessage: String, messageId: String) {
        var delay = 0L
        var customDelaySet = false

        // Check for custom delay first
        val customDelayStr = sharedPreferences.getString("custom_delay_seconds", "")?.trim() ?: ""
        if (customDelayStr.isNotEmpty()) {
            try {
                val customDelaySeconds = customDelayStr.toDouble()
                customDelaySet = true
                if (customDelaySeconds > 0) {
                    delay = (customDelaySeconds * 1000).toLong()
                }
            } catch (e: NumberFormatException) {
                Log.e(TAG, "Invalid custom delay value: $customDelayStr", e)
            }
        }

        // If custom delay is NOT set, check for natural delay
        if (!customDelaySet) {
            val isNaturalDelayEnabled = sharedPreferences.getBoolean("is_natural_delay_enabled", true)
            if (isNaturalDelayEnabled) {
                delay = calculateNaturalDelay(botReplyMessage)
            }
        }

        // Send with calculated delay
        if (delay > 0) {
            Handler(Looper.getMainLooper()).postDelayed({
                send(action, botReplyMessage)
                Handler(Looper.getMainLooper()).postDelayed({
                    respondedMessages.remove(messageId)
                }, 2000)
            }, delay)
        } else {
            send(action, botReplyMessage)
            Handler(Looper.getMainLooper()).postDelayed({
                respondedMessages.remove(messageId)
            }, 2000)
        }
    }

    private fun calculateNaturalDelay(replyText: String): Long {
        if (replyText.isEmpty()) {
            return 1000
        }

        val length = replyText.length
        return when {
            length <= 20 -> (1000 + (length * 50)).toLong()
            length <= 100 -> (2000 + ((length - 20) * 25)).toLong()
            else -> {
                val delay = 4000 + ((length - 100) * 40)
                (if (delay > 8000) 8000 else delay).toLong()
            }
        }.coerceAtLeast(1000)
    }

    private fun isGroupMessage(title: String?): Boolean {
        return title?.contains(":") == true
    }

    private fun isChatActive(sbn: StatusBarNotification): Boolean {
        val notification = sbn.notification
        val extras = notification.extras ?: return false

        val title = extras.getString(Notification.EXTRA_TITLE)
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)

        if (title != null && text != null && title == text.toString()) {
            return true
        }

        if ((notification.flags and Notification.FLAG_ONGOING_EVENT) != 0) {
            return true
        }

        return false
    }

    private fun isMessageFromUser(text: CharSequence?): Boolean {
        if (text == null || text.isEmpty()) {
            return false
        }

        val messageText = text.toString().lowercase()
        return messageText.startsWith("you:") ||
            messageText.startsWith("you ") ||
            messageText.contains("you sent") ||
            messageText.contains("your message") ||
            messageText.contains("read") ||
            messageText.contains("delivered") ||
            messageText.contains("typing...") ||
            messageText.contains("online") ||
            messageText.contains("last seen")
    }

    private fun isAIConfigured(): Boolean {
        if (!sharedPreferences.getBoolean("is_ai_reply_enabled", false)) {
            return false
        }
        val apiKey = sharedPreferences.getString("api_key", "")?.trim() ?: ""
        return apiKey.isNotEmpty()
    }

    private fun isPlatformEnabled(platform: String): Boolean {
        return sharedPreferences.getBoolean("platform_${platform}_enabled", true)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Auto Reply Service",
                NotificationManager.IMPORTANCE_LOW
            )
            channel.description = "Channel for Auto Reply Service"
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun startForegroundService() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Auto-Reply Active")
            .setContentText("Auto-reply service is running")
            .setSmallIcon(R.drawable.ic_menu)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1, notification, android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
        } else {
            startForeground(1, notification)
        }
    }
}

