# Add project specific ProGuard rules here.
-keep class com.autoresponder.app.** { *; }
-dontwarn com.autoresponder.app.**

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Gemini AI
-keep class com.google.ai.client.generativeai.** { *; }
-dontwarn com.google.ai.client.generativeai.**

# Guava
-dontwarn com.google.common.**
-keep class com.google.common.** { *; }

