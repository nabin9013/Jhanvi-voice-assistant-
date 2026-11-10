package com.example.data.models

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: MessageSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isSpoken: Boolean = false
)

enum class MessageSender {
    USER,
    JHANVI
}

data class MemoryItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val category: String = "Preference",
    val timestamp: Long = System.currentTimeMillis()
)

data class SmsItem(
    val id: String,
    val address: String,
    val body: String,
    val date: Long,
    val type: Int // 1 = inbox, 2 = sent
)

data class SmartDevice(
    val id: String,
    val name: String,
    val room: String,
    val type: DeviceType,
    var isOn: Boolean,
    var value: Int = 80 // e.g., brightness or power %
)

enum class DeviceType {
    LIGHT,
    PLUG,
    AC,
    SECURITY,
    ROBOT
}

data class WebCodeProject(
    val title: String,
    var html: String,
    var css: String,
    var js: String
)

data class DocumentItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val snippet: String,
    val fullText: String,
    val category: String,
    val date: String
)

data class MarketItem(
    val symbol: String,
    val name: String,
    val price: String,
    val change: String,
    val isPositive: Boolean,
    val category: String
)

data class JhanviSettings(
    val isAssistantOn: Boolean = true,
    val language: String = "Hinglish (Hindi + English)",
    val speechSpeed: Float = 1.0f,
    val speechPitch: Float = 1.15f, // slightly higher for feminine AI voice
    val isHandsFreeEnabled: Boolean = false,
    val isContinuousConversation: Boolean = true,
    val darkTheme: Boolean = true,
    val highContrast: Boolean = false,
    val largeText: Boolean = false,
    val browserDefault: String = "Built-in Quantum Browser",
    val apiKey: String = ""
)
