package com.autoresponder.app.helper

import java.text.DateFormat
import java.util.Calendar
import java.util.Locale

object CustomMethods {
    
    fun getCurrentDateTime(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US)
        return dateFormat.format(calendar.time)
    }

    fun processPromptTemplate(
        template: String,
        language: String,
        botName: String,
        sender: String,
        message: String,
        chatHistory: String
    ): String {
        if (template.isEmpty()) {
            return template
        }

        val finalPrompt = StringBuilder(template)

        if (chatHistory.isNotBlank()) {
            finalPrompt.append("\n\nPrevious chat history:\n").append(chatHistory)
        } else {
            finalPrompt.append("\n\nThere is no previous chat history. This is the first message from the sender.")
        }

        if (sender.isNotBlank() && message.isNotBlank()) {
            finalPrompt.append("\n\nMost recent message (from $sender): $message")
        }

        return finalPrompt.toString()
    }
}

