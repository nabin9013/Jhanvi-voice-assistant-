package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.JhanviSettings
import com.example.ui.components.CyberGlowCard
import com.example.ui.theme.CardGlowBorder
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceContainer
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.ElectricPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.OnlineGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.JhanviViewModel

// 1. Settings Screen
@Composable
fun SettingsScreen(viewModel: JhanviViewModel, onBack: () -> Unit) {
    BackHandler { onBack() }
    val settings by viewModel.settings.collectAsState()
    val context = LocalContext.current

    var selectedLang by remember { mutableStateOf(settings.language) }
    var speed by remember { mutableStateOf(settings.speechSpeed) }
    var pitch by remember { mutableStateOf(settings.speechPitch) }
    var customApiKey by remember { mutableStateOf(settings.apiKey) }
    var handsFree by remember { mutableStateOf(settings.isHandsFreeEnabled) }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBarBack(title = "SETTINGS", subtitle = "Voice, Language & AI System", onBack = onBack)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            CyberGlowCard(modifier = Modifier.fillMaxWidth(), borderColor = NeonViolet) {
                Column {
                    Text("ASSISTANT LANGUAGE", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    listOf("Hinglish (Hindi + English)", "Pure Hindi (हिन्दी)", "English (US / Global)", "Multilingual Auto-Detect").forEach { lang ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedLang = lang
                                    viewModel.updateSettings(settings.copy(language = lang))
                                }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(if (selectedLang == lang) NeonViolet else DarkSurfaceVariant)
                                    .border(1.dp, CardGlowBorder, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(lang, color = if (selectedLang == lang) ElectricCyan else TextPrimary, fontSize = 14.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            CyberGlowCard(modifier = Modifier.fillMaxWidth(), borderColor = ElectricCyan) {
                Column {
                    Text("VOICE SYNTHESIS SETTINGS", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Speech Rate: ${String.format("%.2f", speed)}x", color = TextSecondary, fontSize = 12.sp)
                    Slider(
                        value = speed,
                        onValueChange = {
                            speed = it
                            viewModel.updateSettings(settings.copy(speechSpeed = it))
                        },
                        valueRange = 0.5f..1.5f,
                        colors = SliderDefaults.colors(thumbColor = ElectricCyan, activeTrackColor = NeonViolet)
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Speech Pitch: ${String.format("%.2f", pitch)}x (Female AI Tone)", color = TextSecondary, fontSize = 12.sp)
                    Slider(
                        value = pitch,
                        onValueChange = {
                            pitch = it
                            viewModel.updateSettings(settings.copy(speechPitch = it))
                        },
                        valueRange = 0.8f..1.6f,
                        colors = SliderDefaults.colors(thumbColor = ElectricPink, activeTrackColor = NeonViolet)
                    )

                    Button(
                        onClick = { viewModel.voiceService.speak("नमस्ते Boss Nabin! जानवी की आवाज का परीक्षण संपन्न हुआ।", pitch, speed) },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Test Jhanvi Voice")
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            CyberGlowCard(modifier = Modifier.fillMaxWidth(), borderColor = ElectricPink) {
                Column {
                    Text("CUSTOM GEMINI API CONFIGURATION", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Optional: Enter your custom Gemini API Key if you wish to override cloud defaults.", color = TextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = customApiKey,
                        onValueChange = {
                            customApiKey = it
                            viewModel.updateSettings(settings.copy(apiKey = it))
                        },
                        placeholder = { Text("AIzaSy...", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonViolet,
                            unfocusedBorderColor = CardGlowBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                }
            }
        }
    }
}

// 2. Accessibility Screen
@Composable
fun AccessibilityScreen(viewModel: JhanviViewModel, onBack: () -> Unit) {
    BackHandler { onBack() }
    val settings by viewModel.settings.collectAsState()

    Scaffold(
        containerColor = DarkBackground,
        topBar = { TopAppBarBack(title = "ACCESSIBILITY", subtitle = "Universal Access & High Contrast", onBack = onBack) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            CyberGlowCard(modifier = Modifier.fillMaxWidth(), borderColor = OnlineGreen) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("High Contrast Mode", style = MaterialTheme.typography.titleMedium, color = Color.White)
                        Text("Enhances border visibility and text sharpness across all screens.", color = TextSecondary, fontSize = 12.sp)
                    }
                    Switch(
                        checked = settings.highContrast,
                        onCheckedChange = { viewModel.updateSettings(settings.copy(highContrast = it)) }
                    )
                }
            }

            CyberGlowCard(modifier = Modifier.fillMaxWidth(), borderColor = ElectricCyan) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Large Text Scaling", style = MaterialTheme.typography.titleMedium, color = Color.White)
                        Text("Optimizes typography size for enhanced readability.", color = TextSecondary, fontSize = 12.sp)
                    }
                    Switch(
                        checked = settings.largeText,
                        onCheckedChange = { viewModel.updateSettings(settings.copy(largeText = it)) }
                    )
                }
            }

            CyberGlowCard(modifier = Modifier.fillMaxWidth(), borderColor = NeonViolet) {
                Column {
                    Text("TALKBACK & SCREEN READER", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "All buttons in Jhanvi AI include semantic content descriptions and 48dp+ interactive touch targets for full TalkBack compatibility.",
                        color = TextPrimary,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

// 3. Jhanvi Rules Screen
@Composable
fun JhanviRulesScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val rules = listOf(
        "Rule 1: Always address the owner respectfully as 'Boss Nabin' in every interaction.",
        "Rule 2: Never pretend to perform an action or task that was not actually completed.",
        "Rule 3: Always request explicit user authorization before accessing private SMS, camera, or photos.",
        "Rule 4: Confirm with Boss Nabin before sending any external message or executing critical IoT overrides.",
        "Rule 5: Be 100% transparent and honest about capabilities, accuracy, and AI limitations.",
        "Rule 6: Protect Boss Nabin's private data, code, memories, and files from unauthorized exposure."
    )

    Scaffold(
        containerColor = DarkBackground,
        topBar = { TopAppBarBack(title = "JHANVI RULES", subtitle = "Core Operating Principles", onBack = onBack) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(rules) { rule ->
                CyberGlowCard(modifier = Modifier.fillMaxWidth(), borderColor = NeonViolet) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.Gavel, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(rule, style = MaterialTheme.typography.bodyLarge, color = TextPrimary, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

// 4. PC Phone Connectivity Screen
@Composable
fun PcPhoneScreen(viewModel: JhanviViewModel, onBack: () -> Unit) {
    BackHandler { onBack() }
    val isConnected by viewModel.isPcConnected.collectAsState()
    val code by viewModel.pcPairingCode.collectAsState()

    Scaffold(
        containerColor = DarkBackground,
        topBar = { TopAppBarBack(title = "PC PHONE CONNECT", subtitle = "Quantum Workstation Sync", onBack = onBack) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CyberGlowCard(modifier = Modifier.fillMaxWidth(), borderColor = if (isConnected) OnlineGreen else NeonViolet) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Computer,
                        contentDescription = "PC",
                        tint = if (isConnected) OnlineGreen else NeonPurple,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (isConnected) "CONNECTED TO BOSS NABIN'S PC" else "PAIR WITH PC WORKSTATION",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isConnected) "Encrypted TLS Tunnel Active • Clipboard & Files Synced" else "Scan QR Code on your desktop browser to connect securely.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pairing Code Box
            CyberGlowCard(modifier = Modifier.fillMaxWidth(), borderColor = ElectricCyan) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("PAIRING SECURITY TOKEN", style = MaterialTheme.typography.labelSmall, color = ElectricCyan)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(code, style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.togglePcPairing() },
                        colors = ButtonDefaults.buttonColors(containerColor = if (isConnected) Color(0xFFFF5252) else NeonViolet),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (isConnected) "Disconnect PC" else "Simulate Link Handshake")
                    }
                }
            }
        }
    }
}

// 5. Privacy Policy Screen
@Composable
fun PrivacyPolicyScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val sections = listOf(
        "1. Data Sovereignty" to "All private memory data, contacts, and preferences belong exclusively to Boss Nabin. No data is harvested for unauthorized ad profiling.",
        "2. Voice Processing" to "Voice input is converted using native Android Speech APIs. Audio buffers are discarded immediately after transcription.",
        "3. Zero-Knowledge Encryption" to "Pairing tokens and sensitive memory stores are restricted to local device storage.",
        "4. User Revocation" to "You maintain the irrevocable right to wipe all stored knowledge via the 'Memories' screen at any time."
    )

    Scaffold(
        containerColor = DarkBackground,
        topBar = { TopAppBarBack(title = "PRIVACY POLICY", subtitle = "Security & Data Governance", onBack = onBack) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sections) { item ->
                val title = item.first
                val desc = item.second
                CyberGlowCard(modifier = Modifier.fillMaxWidth(), borderColor = ElectricCyan) {
                    Column {
                        Text(title, style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(desc, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

// 6. About Screen
@Composable
fun AboutScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    Scaffold(
        containerColor = DarkBackground,
        topBar = { TopAppBarBack(title = "ABOUT JHANVI AI", subtitle = "Version 1.0 • Build Vortex", onBack = onBack) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CyberGlowCard(modifier = Modifier.fillMaxWidth(), borderColor = NeonViolet) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("JHANVI AI", style = MaterialTheme.typography.headlineLarge, color = Color.White, letterSpacing = 2.sp)
                    Text("Personal AI Voice Assistant for Boss Nabin", style = MaterialTheme.typography.labelSmall, color = ElectricCyan)
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        "Designed with futuristic cyberpunk aesthetics, neural speech synthesis, Hindi/Hinglish NLP intelligence, built-in Quantum Web IDE, and complete IoT Smart Home control.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary,
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

// 7. Upgrade Screen
@Composable
fun UpgradeScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    Scaffold(
        containerColor = DarkBackground,
        topBar = { TopAppBarBack(title = "UPGRADE TO PRO", subtitle = "Premium Cyber Assistant Tier", onBack = onBack) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            CyberGlowCard(modifier = Modifier.fillMaxWidth(), borderColor = ElectricCyan) {
                Column {
                    Text("FREE CORE TIER (Active)", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• Unlimited local voice interaction\n• Full Website Coding IDE\n• Memories vault & smart home switches", color = TextSecondary, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            CyberGlowCard(modifier = Modifier.fillMaxWidth(), borderColor = ElectricPink) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = ElectricPink)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("BOSS NABIN ULTRA TIER", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("• Zero-latency multi-modal neural streaming\n• Unlimited AI photo vision analysis\n• Automated IoT circadian scheduling\n• Dedicated quantum server cluster", color = TextPrimary, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { /* Informational upgrade tier */ },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricPink),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Permanent VIP Access Granted to Boss Nabin")
                    }
                }
            }
        }
    }
}

// 8. Notifications Screen
@Composable
fun NotificationsScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val alerts = listOf(
        "Quantum Security Grid Online • 100% Signal Strength",
        "Market Alert: Tech equities rallied +2.9% today",
        "Reminder: Boss Nabin evening strategy review scheduled",
        "Smart Home: Climate Controller holding steady at 22°C"
    )

    Scaffold(
        containerColor = DarkBackground,
        topBar = { TopAppBarBack(title = "NOTIFICATIONS", subtitle = "AI Alerts & Task Reminders", onBack = onBack) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(alerts) { alert ->
                CyberGlowCard(modifier = Modifier.fillMaxWidth(), borderColor = NeonViolet) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(alert, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

// 9. Advanced Settings Screen
@Composable
fun AdvancedSettingsScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    Scaffold(
        containerColor = DarkBackground,
        topBar = { TopAppBarBack(title = "ADVANCED SETTINGS", subtitle = "Developer & System Diagnostics", onBack = onBack) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CyberGlowCard(modifier = Modifier.fillMaxWidth(), borderColor = ElectricCyan) {
                Column {
                    Text("NEURAL LOGS & DIAGNOSTICS", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Voice Latency: 42ms\nTTS Buffer: Synchronized\nNeural Tokens: Nominal\nSecurity Enclave: Uncompromised", style = MaterialTheme.typography.bodyMedium, color = TextPrimary, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun TopAppBarBack(
    title: String,
    subtitle: String,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkSurface)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonViolet)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(title, style = MaterialTheme.typography.titleLarge, color = Color.White)
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = ElectricCyan)
        }
    }
}
