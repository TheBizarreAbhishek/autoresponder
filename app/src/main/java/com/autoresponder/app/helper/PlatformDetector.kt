package com.autoresponder.app.helper

object PlatformDetector {
    private val platformPackages = mapOf(
        "com.whatsapp" to "whatsapp",
        "com.whatsapp.w4b" to "whatsapp",
        "com.instagram.android" to "instagram",
        "com.facebook.orca" to "facebook",
        "org.telegram.messenger" to "telegram",
        "com.twitter.android" to "twitter"
    )

    fun getPlatformName(packageName: String): String? {
        return platformPackages[packageName]
    }

    fun getSupportedPlatforms(): List<String> {
        return platformPackages.values.distinct()
    }
}

