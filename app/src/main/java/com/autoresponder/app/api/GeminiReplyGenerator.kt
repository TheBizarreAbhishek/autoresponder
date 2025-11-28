package com.autoresponder.app.api

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.autoresponder.app.helper.MessageHandler
import com.autoresponder.app.model.Message
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GeminiReplyGenerator(
    private val context: Context,
    private val sharedPreferences: SharedPreferences,
    private val messageHandler: MessageHandler
) {
    companion object {
        private const val TAG = "GeminiGenerator"
    }

    private val apiKey: String = sharedPreferences.getString("api_key", "")?.trim() ?: ""
    private val llmModel: String = sharedPreferences.getString("llm_model", "gemini-pro") ?: "gemini-pro"
    private val defaultReplyMessage: String = sharedPreferences.getString(
        "default_reply_message",
        "I am not available right now. I will be back soon."
    ) ?: "I am not available right now. I will be back soon."
    private val aiReplyLanguage: String = sharedPreferences.getString("ai_reply_language", "English") ?: "English"
    private val customPrompt: String = sharedPreferences.getString("custom_ai_prompt", "")?.trim() ?: ""

    fun generateReply(sender: String, message: String, platform: String, callback: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            messageHandler.getMessagesHistory(sender, platform) { messages ->
                try {
                    val generativeModel = GenerativeModel(
                        modelName = llmModel,
                        apiKey = apiKey
                    )

                    val systemInstruction = if (customPrompt.isNotEmpty()) {
                        customPrompt
                    } else {
                        "You are a WhatsApp auto-reply bot. " +
                        "Your task is to read the provided previous chat history and reply to the most recent incoming message. " +
                        "Always respond in $aiReplyLanguage. Be polite, context-aware, and ensure your replies are relevant to the conversation."
                    }

                    // Build chat history for Gemini
                    val chatHistory = messages.flatMap { msg ->
                        listOf(
                            content("user") { text("${msg.sender}: ${msg.message}") },
                            content("model") { text(msg.reply) }
                        )
                    }

                    val chat = generativeModel.startChat(
                        history = chatHistory
                    )

                    // Add system instruction as part of the prompt or context if the API doesn't support system roles directly in this version
                    // For this version of the SDK, we can prepend the system instruction to the first message or the current prompt
                    // But since we are using startChat, we can just send the message with context.
                    
                    // Note: 0.2.2 SDK might not support system instructions in startChat directly or might behave differently.
                    // A common pattern is to include it in the prompt.
                    
                    val prompt = "$systemInstruction\n\nMost recent message from $sender: $message"
                    
                    // Since generateContent is a suspend function, we need to call it within the scope.
                    val response = chat.sendMessage(prompt)
                    val responseText = response.text

                    if (responseText != null) {
                        callback(responseText)
                    } else {
                        callback(defaultReplyMessage)
                    }

                } catch (e: Exception) {
                    Log.e(TAG, "generateReply: ", e)
                    callback(defaultReplyMessage)
                }
            }
        }
    }
}
