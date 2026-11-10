package com.example.service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class VoiceAssistantService(private val context: Context) : TextToSpeech.OnInitListener {

    private var speechRecognizer: SpeechRecognizer? = null
    private var textToSpeech: TextToSpeech? = null
    private var isTtsInitialized = false

    private val _isListening = MutableStateFlow(false)
    val isListening = _isListening.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking = _isSpeaking.asStateFlow()

    private val _lastSpokenResult = MutableStateFlow("")
    val lastSpokenResult = _lastSpokenResult.asStateFlow()

    private val _audioWaveformLevel = MutableStateFlow(0f)
    val audioWaveformLevel = _audioWaveformLevel.asStateFlow()

    private val _speechError = MutableStateFlow<String?>(null)
    val speechError = _speechError.asStateFlow()

    var onSpeechRecognized: ((String) -> Unit)? = null

    init {
        try {
            textToSpeech = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e("VoiceAssistantService", "TTS Init error", e)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isTtsInitialized = true
            // Prefer Hindi locale for Jhanvi, or default if not available
            val hindi = Locale("hi", "IN")
            val available = textToSpeech?.isLanguageAvailable(hindi)
            if (available == TextToSpeech.LANG_AVAILABLE || available == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                textToSpeech?.language = hindi
            } else {
                textToSpeech?.language = Locale.ENGLISH
            }
            textToSpeech?.setPitch(1.15f)
            textToSpeech?.setSpeechRate(0.95f)

            textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                }

                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                }
            })
        }
    }

    fun startListening() {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _speechError.value = "Speech recognition is not available on this device"
            return
        }

        stopSpeaking()
        stopListening()

        try {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        _isListening.value = true
                        _speechError.value = null
                    }

                    override fun onBeginningOfSpeech() {
                        _isListening.value = true
                    }

                    override fun onRmsChanged(rmsdB: Float) {
                        // Normalize -2dB to 10dB to 0.0 .. 1.0
                        val norm = ((rmsdB + 2f) / 12f).coerceIn(0f, 1f)
                        _audioWaveformLevel.value = norm
                    }

                    override fun onBufferReceived(buffer: ByteArray?) {}

                    override fun onEndOfSpeech() {
                        _isListening.value = false
                    }

                    override fun onError(error: Int) {
                        _isListening.value = false
                        _audioWaveformLevel.value = 0f
                        val msg = when (error) {
                            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                            SpeechRecognizer.ERROR_CLIENT -> "Client error"
                            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Microphone permission required"
                            SpeechRecognizer.ERROR_NETWORK -> "Network connection error"
                            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                            SpeechRecognizer.ERROR_NO_MATCH -> "No speech recognized. Please speak again, Boss Nabin."
                            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Speech service busy"
                            SpeechRecognizer.ERROR_SERVER -> "Server error"
                            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Speech timeout. I am listening..."
                            else -> "Recognition error: $error"
                        }
                        _speechError.value = msg
                    }

                    override fun onResults(results: Bundle?) {
                        _isListening.value = false
                        _audioWaveformLevel.value = 0f
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val text = matches?.firstOrNull()
                        if (!text.isNullOrBlank()) {
                            _lastSpokenResult.value = text
                            onSpeechRecognized?.invoke(text)
                        }
                    }

                    override fun onPartialResults(partialResults: Bundle?) {
                        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        matches?.firstOrNull()?.let {
                            _lastSpokenResult.value = it
                        }
                    }

                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
            }

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN")
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "hi-IN")
                putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, false)
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Listening to Boss Nabin...")
            }

            speechRecognizer?.startListening(intent)
        } catch (e: Exception) {
            _isListening.value = false
            _speechError.value = e.localizedMessage ?: "Failed to start speech recognizer"
        }
    }

    fun stopListening() {
        try {
            speechRecognizer?.stopListening()
            speechRecognizer?.destroy()
            speechRecognizer = null
        } catch (_: Exception) {}
        _isListening.value = false
        _audioWaveformLevel.value = 0f
    }

    fun speak(text: String, pitch: Float = 1.15f, rate: Float = 0.95f) {
        if (!isTtsInitialized || text.isBlank()) return
        stopListening()
        textToSpeech?.setPitch(pitch)
        textToSpeech?.setSpeechRate(rate)

        // Sanitize markdown and emojis for smooth pronunciation
        val cleanText = text
            .replace(Regex("[*#`_~>]"), "")
            .replace(Regex("Boss Nabin", RegexOption.IGNORE_CASE), "Boss Nabin")
            .trim()

        val params = Bundle()
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "JHANVI_RESPONSE")
        textToSpeech?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, params, "JHANVI_RESPONSE")
    }

    fun stopSpeaking() {
        if (isTtsInitialized) {
            textToSpeech?.stop()
        }
        _isSpeaking.value = false
    }

    fun cleanup() {
        stopListening()
        stopSpeaking()
        textToSpeech?.shutdown()
        textToSpeech = null
    }
}
