package com.autoresponder.app.model

data class Preset(
    val id: Int,
    val keyword: String,
    val reply: String,
    val isCaseSensitive: Boolean = false,
    val isExactMatch: Boolean = false
)

