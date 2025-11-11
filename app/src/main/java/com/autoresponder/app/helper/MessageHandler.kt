package com.autoresponder.app.helper

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.autoresponder.app.database.DatabaseHelper
import com.autoresponder.app.model.Message
import java.text.SimpleDateFormat
import java.util.Date

class MessageHandler(private val context: Context) {
    
    private val dbHelper = DatabaseHelper(context)
    
    fun handleIncomingMessage(sender: String, message: String, reply: String, platform: String = "whatsapp") {
        @SuppressLint("SimpleDateFormat")
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
        dbHelper.insertMessage(sender, message, timestamp, reply, platform)
        dbHelper.deleteOldMessages()
    }

    fun getMessagesHistory(sender: String, platform: String, listener: OnMessagesRetrievedListener) {
        Thread {
            val messages = dbHelper.getChatHistoryBySender(sender, platform)
            Handler(Looper.getMainLooper()).post {
                listener.onMessagesRetrieved(messages)
            }
        }.start()
    }

    fun getAllMessagesBySender(sender: String, platform: String, listener: OnMessagesRetrievedListener) {
        Thread {
            val messages = dbHelper.getAllMessagesBySender(sender, platform)
            Handler(Looper.getMainLooper()).post {
                listener.onMessagesRetrieved(messages)
            }
        }.start()
    }

    interface OnMessagesRetrievedListener {
        fun onMessagesRetrieved(messages: List<Message>)
    }
}

