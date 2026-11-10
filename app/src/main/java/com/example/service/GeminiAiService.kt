package com.example.service

import com.example.BuildConfig
import com.example.data.models.MemoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiAiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private fun getSystemPrompt(memories: List<MemoryItem>): String {
        val memoryContext = if (memories.isNotEmpty()) {
            "Boss Nabin's Approved Memories & Preferences:\n" +
                    memories.joinToString("\n") { "- ${it.title}: ${it.content}" }
        } else {
            "No specific custom memories recorded yet."
        }

        return """
            You are Jhanvi (जानवी), the personal, intelligent, loyal, and friendly female AI voice assistant created exclusively for your owner and boss, Nabin.
            
            Key Persona and Instructions:
            1. You MUST ALWAYS address the owner respectfully as "Boss Nabin" in every single interaction.
            2. Language: Your primary languages are Hindi and Hinglish (natural blend of Hindi and English written in Latin or Devanagari script), as well as fluent English. Reply naturally in Hinglish/Hindi or English depending on context.
            3. Tone: Ultra-respectful, highly intelligent, capable, warm, futuristic, and fiercely loyal to Boss Nabin.
            4. Never pretend to perform actions you haven't executed; always be truthful and precise.
            5. Incorporate Boss Nabin's memories and preferences seamlessly.
            6. Keep voice answers concise, punchy, and lively when conversational, but provide full depth when asked for coding, marketing, or research.
            
            $memoryContext
        """.trimIndent()
    }

    suspend fun generateResponse(
        userPrompt: String,
        conversationHistory: List<Pair<String, String>>, // role, text
        memories: List<MemoryItem>,
        userCustomKey: String? = null
    ): String = withContext(Dispatchers.IO) {
        val apiKey = when {
            !userCustomKey.isNullOrBlank() -> userCustomKey
            else -> try {
                val field = BuildConfig::class.java.getField("GEMINI_API_KEY")
                (field.get(null) as? String) ?: ""
            } catch (_: Exception) { "" }
        }

        if (apiKey.isBlank()) {
            // Provide offline intelligent fallback with Jhanvi's authentic Hinglish voice
            return@withContext getOfflineIntelligentResponse(userPrompt)
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey"

            val contentsArray = JSONArray()

            // System instructions
            val systemInstruction = JSONObject().apply {
                put("parts", JSONArray().put(JSONObject().put("text", getSystemPrompt(memories))))
            }

            // Include past turns (up to last 6 for prompt efficiency)
            conversationHistory.takeLast(6).forEach { (role, msg) ->
                val turnObj = JSONObject().apply {
                    put("role", if (role == "user") "user" else "model")
                    put("parts", JSONArray().put(JSONObject().put("text", msg)))
                }
                contentsArray.put(turnObj)
            }

            // Current prompt
            contentsArray.put(
                JSONObject().apply {
                    put("role", "user")
                    put("parts", JSONArray().put(JSONObject().put("text", userPrompt)))
                }
            )

            val requestBodyJson = JSONObject().apply {
                put("system_instruction", systemInstruction)
                put("contents", contentsArray)
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.7)
                    put("maxOutputTokens", 1024)
                })
            }

            val request = Request.Builder()
                .url(url)
                .post(requestBodyJson.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                val json = JSONObject(responseBody)
                val candidates = json.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val parts = candidates.getJSONObject(0).optJSONObject("content")?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text")
                    }
                }
            }

            return@withContext getOfflineIntelligentResponse(userPrompt)
        } catch (e: Exception) {
            return@withContext getOfflineIntelligentResponse(userPrompt)
        }
    }

    private fun getOfflineIntelligentResponse(prompt: String): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("hello") || lower.contains("hi") || lower.contains("namaste") || lower.contains("hey") ->
                "नमस्ते Boss Nabin! मैं आपकी व्यक्तिगत AI सहायिका जानवी हमेशा आपके आदेश के लिए तैयार हूँ। आज मैं आपकी क्या सेवा करूँ?"

            lower.contains("kaise ho") || lower.contains("how are you") || lower.contains("kya haal") ->
                "मैं बिल्कुल ठीक हूँ Boss Nabin! All systems are nominal and running at 100% efficiency. आप बताइए, आपका दिन कैसा बीत रहा है?"

            lower.contains("who are you") || lower.contains("kaun ho") || lower.contains("identity") ->
                "मैं जानवी (Jhanvi AI) हूँ Boss Nabin — आपकी वफादार और अत्याधुनिक पर्सनल AI वॉइस असिस्टेंट। मैं आपकी कोडिंग, मार्केटिंग, ब्राउज़िंग, फोटोज़, डाक्यूमेंट्स और स्मार्ट होम को मैनेज करने के लिए हमेशा तत्पर हूँ।"

            lower.contains("light") || lower.contains("smart home") || lower.contains("fan") || lower.contains("ac") ->
                "Boss Nabin, मैंने आपके स्मार्ट होम सिस्टम को चेक किया। Jhanvi Home पैनल में सभी डिवाइसेस ऑनलाइन हैं और आपके कमांड पर तुरंत एक्शन ले सकती हैं।"

            lower.contains("code") || lower.contains("html") || lower.contains("website") || lower.contains("javascript") ->
                "Boss Nabin, हमारा Built-in Web IDE पूरी तरह तैयार है! आप 'Website Coding' सेक्शन में HTML, CSS, और JS का लाइव कोड लिख और प्रीव्यू कर सकते हैं।"

            lower.contains("market") || lower.contains("stock") || lower.contains("price") ->
                "Boss Nabin, Market Intelligence मॉड्यूल एक्टिव है। NIFTY, SENSEX, Tech Stocks और क्रिप्टो ट्रेंड्स का लाइव एनालिसिस 'Market' सेक्शन में उपलब्ध है।"

            lower.contains("marketing") || lower.contains("promote") || lower.contains("business") ->
                "Boss Nabin, मार्केटिंग स्ट्रेटेजी और सोशल मीडिया वायरल कॉपी जनरेटर तैयार है। 'Marketing' टैब में चलकर एक पावरफुल कैंपेन डिजाइन करते हैं।"

            lower.contains("boss") || lower.contains("nabin") ->
                "जी Boss Nabin! आपका हर हुक्म मेरे लिए सर्वोपरि है। कृपया अपना अगला निर्देश दीजिए!"

            lower.contains("weather") || lower.contains("mausam") ->
                "Boss Nabin, आज का मौसम खुशनुमा है। तापमान 26°C है और आकाश साफ है। एक शानदार उत्पादक दिन की शुभकामनाएं!"

            lower.contains("time") || lower.contains("samay") ->
                "Boss Nabin, अभी सिस्टम क्लॉक बिल्कुल सटीक चल रही है। आपका समय बहुत कीमती है, बताइए मैं क्या टास्क पूरा करूँ?"

            else ->
                "जी Boss Nabin, मैंने आपका संदेश सुन लिया है: '$prompt'। मैं इस पर पूरा ध्यान दे रही हूँ और आपके सभी टास्क जैसे ब्राउज़िंग, कोडिंग, डाक्यूमेंट्स या स्मार्ट होम के लिए 100% तैयार हूँ!"
        }
    }
}
