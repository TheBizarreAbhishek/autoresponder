package com.autoresponder.app.api

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.autoresponder.app.helper.CustomMethods
import com.autoresponder.app.helper.MessageHandler
import com.autoresponder.app.model.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class ChatGPTReplyGenerator(
    private val context: Context,
    private val sharedPreferences: SharedPreferences,
    private val messageHandler: MessageHandler
) {
    companion object {
        private const val TAG = "ChatGPTGenerator"
        private const val API_URL = "https://api.openai.com/v1/chat/completions"
    }

    private val apiKey: String = sharedPreferences.getString("api_key", "")?.trim() ?: ""
    private val llmModel: String = sharedPreferences.getString("llm_model", "gpt-4o-mini") ?: "gpt-4o-mini"
    private val defaultReplyMessage: String = sharedPreferences.getString(
        "default_reply_message",
        "I am not available right now. I will be back soon."
    ) ?: "I am not available right now. I will be back soon."
    private val aiReplyLanguage: String = sharedPreferences.getString("ai_reply_language", "English") ?: "English"
    private val botName: String = sharedPreferences.getString("bot_name", "Yuji") ?: "Yuji"
    private val customPrompt: String = sharedPreferences.getString("custom_ai_prompt", "")?.trim() ?: ""

    fun generateReply(sender: String, message: String, platform: String, callback: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            messageHandler.getMessagesHistory(sender, platform, object : MessageHandler.OnMessagesRetrievedListener {
                override fun onMessagesRetrieved(messages: List<Message>) {
                    val chatHistory = getChatHistory(messages)
                    
                    try {
                        val jsonBody = buildRequest(sender, message, chatHistory)
                        val client = OkHttpClient.Builder()
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .build()

                        val mediaType = "application/json; charset=utf-8".toMediaType()
                        val requestBody = RequestBody.create(mediaType, jsonBody.toString())

                        val request = Request.Builder()
                            .url(API_URL)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", "Bearer $apiKey")
                            .post(requestBody)
                            .build()

                        client.newCall(request).enqueue(object : Callback {
                            override fun onFailure(call: Call, e: java.io.IOException) {
                                Log.e(TAG, "onFailure: ", e)
                                callback(defaultReplyMessage)
                            }

                            override fun onResponse(call: Call, response: Response) {
                                if (!response.isSuccessful) {
                                    Log.d(TAG, "onResponse: ${response.code}")
                                    callback(defaultReplyMessage)
                                    return
                                }

                                val body = response.body?.string()
                                if (body != null) {
                                    val reply = parseResponse(body)
                                    callback(reply ?: defaultReplyMessage)
                                } else {
                                    callback(defaultReplyMessage)
                                }
                            }
                        })
                    } catch (e: Exception) {
                        Log.e(TAG, "generateReply: ", e)
                        callback(defaultReplyMessage)
                    }
                }
            })
        }
    }

    private fun buildRequest(sender: String, message: String, chatHistory: StringBuilder): JSONObject {
        val container = JSONObject()
        val messagesArray = JSONArray()

        val systemRole = JSONObject()
        val userRole1 = JSONObject()
        val userRole2 = JSONObject()

        if (customPrompt.isNotEmpty()) {
            systemRole.put("role", "system")
            systemRole.put("content", customPrompt)

            userRole1.put("role", "user")
            userRole1.put(
                "content",
                if (chatHistory.isEmpty()) {
                    "There is no previous chat history. This is the first message from the sender."
                } else {
                    "Previous chat history:\n$chatHistory"
                }
            )

            userRole2.put("role", "user")
            userRole2.put("content", "Most recent message (from $sender): $message")
        } else {
            systemRole.put("role", "system")
            systemRole.put(
                "content",
                "You are a WhatsApp auto-reply bot. " +
                    "Your task is to read the provided previous chat history and reply to the most recent incoming message. " +
                    "Always respond in $aiReplyLanguage. Be polite, context-aware, and ensure your replies are relevant to the conversation."
            )

            userRole1.put("role", "user")
            userRole1.put(
                "content",
                if (chatHistory.isEmpty()) {
                    "There are no any previous chat history. This is the first message from the sender."
                } else {
                    "Previous chat history: $chatHistory"
                }
            )

            userRole2.put("role", "user")
            userRole2.put("content", "Most recent message from the sender ($sender): $message")
        }

        messagesArray.put(systemRole)
        messagesArray.put(userRole1)
        messagesArray.put(userRole2)

        container.put("model", llmModel)
        container.put("messages", messagesArray)

        return container
    }

    private fun getChatHistory(messages: List<Message>): StringBuilder {
        val chatHistory = StringBuilder()

        if (messages.isNotEmpty()) {
            for (msg in messages) {
                chatHistory.append("${msg.sender}: ${msg.message}\n")
                chatHistory.append("Time: ${msg.timestamp}\n")
                chatHistory.append("My reply: ${msg.reply}\n\n")
            }
        }

        return chatHistory
    }

    private fun parseResponse(responseData: String): String? {
        return try {
            val jsonObject = JSONObject(responseData)
            val choicesArray = jsonObject.getJSONArray("choices")
            if (choicesArray.length() > 0) {
                val choice = choicesArray.getJSONObject(0)
                val message = choice.getJSONObject("message")
                message.getString("content")
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "parseResponse: ", e)
            null
        }
    }
}

