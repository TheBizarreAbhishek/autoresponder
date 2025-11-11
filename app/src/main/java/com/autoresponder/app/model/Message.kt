package com.autoresponder.app.model

data class Message(
    val id: Int,
    val sender: String,
    val message: String,
    val timestamp: String,
    val reply: String,
    val platform: String = "whatsapp"
)

