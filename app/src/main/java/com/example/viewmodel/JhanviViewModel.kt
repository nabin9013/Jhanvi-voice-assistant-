package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.provider.Telephony
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.models.ChatMessage
import com.example.data.models.DeviceType
import com.example.data.models.DocumentItem
import com.example.data.models.JhanviSettings
import com.example.data.models.MarketItem
import com.example.data.models.MemoryItem
import com.example.data.models.MessageSender
import com.example.data.models.SmartDevice
import com.example.data.models.SmsItem
import com.example.data.models.WebCodeProject
import com.example.service.GeminiAiService
import com.example.service.VoiceAssistantService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class JhanviViewModel(application: Application) : AndroidViewModel(application) {

    val voiceService = VoiceAssistantService(application.applicationContext)
    private val aiService = GeminiAiService()
    private val prefs = application.getSharedPreferences("jhanvi_prefs", Context.MODE_PRIVATE)

    // Current Active Navigation Screen
    private val _currentScreen = MutableStateFlow("home")
    val currentScreen = _currentScreen.asStateFlow()

    // Status
    val isListening = voiceService.isListening
    val isSpeaking = voiceService.isSpeaking
    val audioWaveformLevel = voiceService.audioWaveformLevel
    val speechError = voiceService.speechError

    // Settings
    private val _settings = MutableStateFlow(
        JhanviSettings(
            isAssistantOn = prefs.getBoolean("assistant_on", true),
            language = prefs.getString("language", "Hinglish (Hindi + English)") ?: "Hinglish (Hindi + English)",
            speechSpeed = prefs.getFloat("speech_speed", 1.0f),
            speechPitch = prefs.getFloat("speech_pitch", 1.15f),
            isHandsFreeEnabled = prefs.getBoolean("hands_free", false),
            darkTheme = prefs.getBoolean("dark_theme", true),
            highContrast = prefs.getBoolean("high_contrast", false),
            largeText = prefs.getBoolean("large_text", false),
            apiKey = prefs.getString("custom_api_key", "") ?: ""
        )
    )
    val settings = _settings.asStateFlow()

    // Chat History
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = MessageSender.JHANVI,
                text = "Hello Boss Nabin, मैं आपकी व्यक्तिगत AI सहायिका जानवी हूँ। All systems online and ready for your command! मैं आपकी क्या मदद कर सकती हूँ?"
            )
        )
    )
    val chatMessages = _chatMessages.asStateFlow()

    // Memories System
    private val _memories = MutableStateFlow<List<MemoryItem>>(
        listOf(
            MemoryItem(
                title = "Boss's Identity",
                content = "Owner & Master of this system is Nabin. Always addressed respectfully as 'Boss Nabin'.",
                category = "Core Protocol"
            ),
            MemoryItem(
                title = "Primary Language Preference",
                content = "Prefers Hindi and Hinglish voice interactions with occasional English technical terms.",
                category = "Language"
            ),
            MemoryItem(
                title = "Favorite Coffee",
                content = "Strong Espresso with a touch of cinnamon in the morning.",
                category = "Preferences"
            ),
            MemoryItem(
                title = "Work Priority",
                content = "AI development, Web coding, and tech venture investments.",
                category = "Career"
            )
        )
    )
    val memories = _memories.asStateFlow()

    // Smart Home Devices
    private val _smartDevices = MutableStateFlow<List<SmartDevice>>(
        listOf(
            SmartDevice("dev_1", "Cyber Neon Chandelier", "Boss Office", DeviceType.LIGHT, true, 90),
            SmartDevice("dev_2", "Quantum Workstation PC", "Boss Desk", DeviceType.PLUG, true, 100),
            SmartDevice("dev_3", "Climate Controller (22°C)", "Main Hall", DeviceType.AC, true, 22),
            SmartDevice("dev_4", "Perimeter Hologram Grid", "Entrance", DeviceType.SECURITY, true, 100),
            SmartDevice("dev_5", "Cyber Sentry Bot", "Lab", DeviceType.ROBOT, false, 0)
        )
    )
    val smartDevices = _smartDevices.asStateFlow()

    // Web Coding IDE
    private val _webProject = MutableStateFlow(
        WebCodeProject(
            title = "Boss_Nabin_Cyber_Portal",
            html = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Boss Nabin Cyber Portal</title>
                </head>
                <body>
                    <div class="card">
                        <h1>JHANVI AI // SYSTEM ONLINE</h1>
                        <p>Welcome back, <strong>Boss Nabin</strong>.</p>
                        <button onclick="activateCore()">ENGAGE HYPERDRIVE</button>
                        <div id="status">Standby Mode</div>
                    </div>
                </body>
                </html>
            """.trimIndent(),
            css = """
                body {
                    margin: 0;
                    padding: 24px;
                    background: #090314;
                    color: #F3E8FF;
                    font-family: 'Segoe UI', sans-serif;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    min-height: 80vh;
                }
                .card {
                    background: #190B2E;
                    border: 2px solid #B026FF;
                    border-radius: 16px;
                    padding: 24px;
                    text-align: center;
                    box-shadow: 0 0 25px rgba(176, 38, 255, 0.4);
                }
                h1 {
                    color: #00F0FF;
                    font-size: 20px;
                    letter-spacing: 2px;
                }
                button {
                    background: #B026FF;
                    color: white;
                    border: none;
                    padding: 12px 24px;
                    border-radius: 8px;
                    font-weight: bold;
                    cursor: pointer;
                    margin-top: 16px;
                }
                #status {
                    margin-top: 16px;
                    color: #00FF9D;
                    font-weight: bold;
                }
            """.trimIndent(),
            js = """
                function activateCore() {
                    const el = document.getElementById('status');
                    el.innerText = '⚡ CORE SYNCHRONIZED: Jhanvi & Boss Nabin Connected!';
                    el.style.color = '#FF2A85';
                }
            """.trimIndent()
        )
    )
    val webProject = _webProject.asStateFlow()

    // Documents
    private val _documents = MutableStateFlow<List<DocumentItem>>(
        listOf(
            DocumentItem(
                title = "Boss Nabin AI Strategic Blueprint",
                snippet = "Architecture overview of autonomous AI agents and voice pipelines...",
                fullText = "CONFIDENTIAL ARCHITECTURE DOCUMENT\nOwner: Boss Nabin\nSystem: Jhanvi Autonomous Node\n\n1. Real-time neural voice pipeline with multi-lingual Hinglish switching.\n2. Quantum cryptographic pairing with local PC.\n3. Encrypted memory vault guarded by multi-factor biometric check.",
                category = "Strategy",
                date = "2026-11-09"
            ),
            DocumentItem(
                title = "Smart Home IoT Specification",
                snippet = "Protocols for MQTT / Matter integration with ambient lights...",
                fullText = "Jhanvi Home Controller v3.4\nSupports local Zigbee/Matter protocols. Zero cloud dependency for local security actuators. Voice priority overrides manual switches.",
                category = "IoT",
                date = "2026-11-08"
            ),
            DocumentItem(
                title = "Q4 Venture Investments & Market Notes",
                snippet = "High growth portfolio review across robotics and clean-tech...",
                fullText = "PORTFOLIO TRACKER\nTarget Alpha: +24% YoY.\nKey drivers: Generative robotics, spatial computing, on-device silicon.",
                category = "Finance",
                date = "2026-11-07"
            )
        )
    )
    val documents = _documents.asStateFlow()

    // Market Items
    private val _marketItems = MutableStateFlow<List<MarketItem>>(
        listOf(
            MarketItem("NIFTY 50", "National Stock Exchange", "24,852.10", "+142.30 (+0.58%)", true, "Indices"),
            MarketItem("SENSEX", "Bombay Stock Exchange", "81,720.50", "+410.80 (+0.51%)", true, "Indices"),
            MarketItem("NVDA", "NVIDIA Corp (AI Chipset)", "$148.60", "+$4.20 (+2.91%)", true, "Tech Stocks"),
            MarketItem("GOOGL", "Alphabet Inc (Gemini)", "$192.40", "+$3.10 (+1.64%)", true, "Tech Stocks"),
            MarketItem("BTC/USD", "Bitcoin Digital Gold", "$94,320.00", "+$1,850 (+2.00%)", true, "Crypto"),
            MarketItem("BRENT", "Brent Crude Oil", "$73.80", "-$0.95 (-1.27%)", false, "Commodities")
        )
    )
    val marketItems = _marketItems.asStateFlow()

    // SMS Items (Authorized)
    private val _smsList = MutableStateFlow<List<SmsItem>>(emptyList())
    val smsList = _smsList.asStateFlow()
    private val _smsPermissionGranted = MutableStateFlow(false)
    val smsPermissionGranted = _smsPermissionGranted.asStateFlow()

    // Photos
    private val _capturedPhotoUris = MutableStateFlow<List<Uri>>(emptyList())
    val capturedPhotoUris = _capturedPhotoUris.asStateFlow()

    // Browser State
    val browserUrl = MutableStateFlow("https://www.google.com")
    val browserTabs = MutableStateFlow(listOf("https://www.google.com", "https://news.google.com"))
    val browserBookmarks = MutableStateFlow(
        listOf(
            "https://www.google.com" to "Google",
            "https://github.com" to "GitHub",
            "https://news.google.com" to "Google News",
            "https://ai.google.dev" to "Gemini Developer"
        )
    )

    // PC Phone Pairing
    private val _isPcConnected = MutableStateFlow(false)
    val isPcConnected = _isPcConnected.asStateFlow()
    private val _pcPairingCode = MutableStateFlow("JHANVI-BOSS-9821")
    val pcPairingCode = _pcPairingCode.asStateFlow()

    init {
        voiceService.onSpeechRecognized = { spokenText ->
            processUserVoiceInput(spokenText)
        }
    }

    fun navigateTo(screen: String) {
        _currentScreen.value = screen
    }

    fun toggleAssistantState() {
        val next = !_settings.value.isAssistantOn
        _settings.update { it.copy(isAssistantOn = next) }
        prefs.edit().putBoolean("assistant_on", next).apply()
        if (!next) {
            voiceService.stopListening()
            voiceService.stopSpeaking()
        } else {
            speakGreeting()
        }
    }

    fun toggleListening() {
        if (!_settings.value.isAssistantOn) return
        if (voiceService.isListening.value) {
            voiceService.stopListening()
        } else {
            voiceService.startListening()
        }
    }

    fun stopSpeaking() {
        voiceService.stopSpeaking()
    }

    fun repeatLastAnswer() {
        val lastJhanviMessage = _chatMessages.value.lastOrNull { it.sender == MessageSender.JHANVI }
        if (lastJhanviMessage != null) {
            voiceService.speak(
                lastJhanviMessage.text,
                _settings.value.speechPitch,
                _settings.value.speechSpeed
            )
        }
    }

    fun speakGreeting() {
        val greeting = "Hello Boss Nabin! All systems are online. How can Jhanvi assist you today?"
        voiceService.speak(greeting, _settings.value.speechPitch, _settings.value.speechSpeed)
    }

    fun processUserVoiceInput(text: String) {
        if (text.isBlank() || !_settings.value.isAssistantOn) return

        // Add user message to chat
        val userMsg = ChatMessage(sender = MessageSender.USER, text = text)
        _chatMessages.update { it + userMsg }

        // Check for quick voice actions (e.g., turn on light, open browser)
        val lower = text.lowercase()
        if (lower.contains("turn on") && lower.contains("light")) {
            toggleDevice("dev_1", true)
            val reply = "Boss Nabin, I have turned ON the Cyber Neon Lights for you."
            recordJhanviReply(reply)
            return
        } else if (lower.contains("turn off") && lower.contains("light")) {
            toggleDevice("dev_1", false)
            val reply = "Boss Nabin, the lights have been turned OFF."
            recordJhanviReply(reply)
            return
        } else if (lower.contains("open browser") || lower.contains("search for")) {
            val query = text.replace(Regex("(?i)open browser|search for"), "").trim()
            if (query.isNotBlank()) {
                browserUrl.value = "https://www.google.com/search?q=" + Uri.encode(query)
            }
            navigateTo("browser")
            val reply = "Opening the Quantum Browser for you, Boss Nabin."
            recordJhanviReply(reply)
            return
        }

        // Call Gemini AI or intelligent persona engine
        viewModelScope.launch {
            val history = _chatMessages.value.map {
                (if (it.sender == MessageSender.USER) "user" else "model") to it.text
            }
            val reply = aiService.generateResponse(
                userPrompt = text,
                conversationHistory = history,
                memories = _memories.value,
                userCustomKey = _settings.value.apiKey
            )
            recordJhanviReply(reply)
        }
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        val userMsg = ChatMessage(sender = MessageSender.USER, text = text)
        _chatMessages.update { it + userMsg }

        viewModelScope.launch {
            val history = _chatMessages.value.map {
                (if (it.sender == MessageSender.USER) "user" else "model") to it.text
            }
            val reply = aiService.generateResponse(
                userPrompt = text,
                conversationHistory = history,
                memories = _memories.value,
                userCustomKey = _settings.value.apiKey
            )
            val aiMsg = ChatMessage(sender = MessageSender.JHANVI, text = reply)
            _chatMessages.update { it + aiMsg }
            // Speak if continuous conversation enabled
            if (_settings.value.isContinuousConversation && _settings.value.isAssistantOn) {
                voiceService.speak(reply, _settings.value.speechPitch, _settings.value.speechSpeed)
            }
        }
    }

    private fun recordJhanviReply(reply: String) {
        val aiMsg = ChatMessage(sender = MessageSender.JHANVI, text = reply)
        _chatMessages.update { it + aiMsg }
        if (_settings.value.isAssistantOn) {
            voiceService.speak(reply, _settings.value.speechPitch, _settings.value.speechSpeed)
        }
    }

    fun clearChat() {
        _chatMessages.value = listOf(
            ChatMessage(
                sender = MessageSender.JHANVI,
                text = "New conversation initiated. I am ready, Boss Nabin!"
            )
        )
    }

    // Memories management
    fun addMemory(title: String, content: String, category: String) {
        val newMem = MemoryItem(title = title, content = content, category = category)
        _memories.update { listOf(newMem) + it }
    }

    fun deleteMemory(id: String) {
        _memories.update { list -> list.filterNot { it.id == id } }
    }

    fun clearAllMemories() {
        _memories.value = emptyList()
    }

    // Smart Home device toggles
    fun toggleDevice(id: String, forceState: Boolean? = null) {
        _smartDevices.update { list ->
            list.map {
                if (it.id == id) {
                    val next = forceState ?: !it.isOn
                    it.copy(isOn = next)
                } else it
            }
        }
    }

    fun updateDeviceValue(id: String, value: Int) {
        _smartDevices.update { list ->
            list.map { if (it.id == id) it.copy(value = value) else it }
        }
    }

    // Web Coding IDE
    fun updateWebCode(html: String, css: String, js: String) {
        _webProject.value = _webProject.value.copy(html = html, css = css, js = js)
    }

    fun requestAiCodingAssistance(prompt: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            val fullPrompt = "Boss Nabin needs coding assistance for HTML/CSS/JS. Prompt: $prompt. Provide clean, modern code snippets and clear advice."
            val reply = aiService.generateResponse(fullPrompt, emptyList(), _memories.value, _settings.value.apiKey)
            onResult(reply)
        }
    }

    // Documents
    fun addDocument(title: String, content: String, category: String) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val item = DocumentItem(
            title = title,
            snippet = content.take(60) + if (content.length > 60) "..." else "",
            fullText = content,
            category = category,
            date = dateFormat.format(Date())
        )
        _documents.update { listOf(item) + it }
    }

    fun summarizeDocument(doc: DocumentItem, onResult: (String) -> Unit) {
        viewModelScope.launch {
            val prompt = "Boss Nabin requests an executive summary of this document titled '${doc.title}':\n\n${doc.fullText}"
            val summary = aiService.generateResponse(prompt, emptyList(), _memories.value, _settings.value.apiKey)
            onResult(summary)
        }
    }

    // Photos
    fun addCapturedPhoto(uri: Uri) {
        _capturedPhotoUris.update { listOf(uri) + it }
    }

    // SMS Loading
    fun loadSmsMessages() {
        try {
            val context = getApplication<Application>().applicationContext
            val contentResolver = context.contentResolver
            val cursor = contentResolver.query(
                Telephony.Sms.CONTENT_URI,
                arrayOf(Telephony.Sms._ID, Telephony.Sms.ADDRESS, Telephony.Sms.BODY, Telephony.Sms.DATE, Telephony.Sms.TYPE),
                null,
                null,
                "${Telephony.Sms.DATE} DESC LIMIT 25"
            )

            val list = mutableListOf<SmsItem>()
            cursor?.use {
                val idCol = it.getColumnIndex(Telephony.Sms._ID)
                val addrCol = it.getColumnIndex(Telephony.Sms.ADDRESS)
                val bodyCol = it.getColumnIndex(Telephony.Sms.BODY)
                val dateCol = it.getColumnIndex(Telephony.Sms.DATE)
                val typeCol = it.getColumnIndex(Telephony.Sms.TYPE)

                while (it.moveToNext()) {
                    val id = if (idCol >= 0) it.getString(idCol) else ""
                    val addr = if (addrCol >= 0) it.getString(addrCol) ?: "Unknown" else "Unknown"
                    val body = if (bodyCol >= 0) it.getString(bodyCol) ?: "" else ""
                    val date = if (dateCol >= 0) it.getLong(dateCol) else 0L
                    val type = if (typeCol >= 0) it.getInt(typeCol) else 1
                    list.add(SmsItem(id, addr, body, date, type))
                }
            }
            _smsList.value = list
            _smsPermissionGranted.value = true
        } catch (e: Exception) {
            Log.e("JhanviViewModel", "Error querying SMS", e)
        }
    }

    // PC Phone Pairing
    fun togglePcPairing() {
        _isPcConnected.value = !_isPcConnected.value
    }

    // Settings Updates
    fun updateSettings(newSettings: JhanviSettings) {
        _settings.value = newSettings
        prefs.edit()
            .putBoolean("assistant_on", newSettings.isAssistantOn)
            .putString("language", newSettings.language)
            .putFloat("speech_speed", newSettings.speechSpeed)
            .putFloat("speech_pitch", newSettings.speechPitch)
            .putBoolean("hands_free", newSettings.isHandsFreeEnabled)
            .putBoolean("dark_theme", newSettings.darkTheme)
            .putBoolean("high_contrast", newSettings.highContrast)
            .putBoolean("large_text", newSettings.largeText)
            .putString("custom_api_key", newSettings.apiKey)
            .apply()
    }

    override fun onCleared() {
        super.onCleared()
        voiceService.cleanup()
    }
}
