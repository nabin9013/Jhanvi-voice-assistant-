package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.CyberGlowCard
import com.example.ui.components.FuturisticMicButton
import com.example.ui.components.StatusBadge
import com.example.ui.components.VoiceWaveformVisualizer
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
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.JhanviViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: JhanviViewModel,
    onOpenDrawer: () -> Unit
) {
    val settings by viewModel.settings.collectAsState()
    val isListening by viewModel.isListening.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val waveformLevel by viewModel.audioWaveformLevel.collectAsState()
    val speechError by viewModel.speechError.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()

    val lastJhanviMessage = chatMessages.lastOrNull { it.sender == com.example.data.models.MessageSender.JHANVI }

    // Hologram rotation animation
    val holoRotation = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        holoRotation.animateTo(
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(24000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    val currentTime = remember {
        val sdf = SimpleDateFormat("EEE, dd MMM yyyy • hh:mm a", Locale.getDefault())
        sdf.format(Date())
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Ambient background glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(NeonViolet.copy(alpha = 0.22f), Color.Transparent),
                            center = Offset(size.width / 2f, size.height * 0.28f),
                            radius = size.width * 0.7f
                        )
                    )
                }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 96.dp)
        ) {
            // Header: ☰ Drawer Button, JHANVI AI Title & Live Clock
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onOpenDrawer,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(DarkSurfaceVariant)
                        .border(1.dp, CardGlowBorder.copy(alpha = 0.6f), CircleShape)
                        .testTag("menu_drawer_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu Drawer",
                        tint = NeonViolet
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "JHANVI AI",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = currentTime,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }

                StatusBadge(isOnline = settings.isAssistantOn)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 3D Avatar with Animated Glowing Holographic Rings
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer rotating cyber ring
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .rotate(holoRotation.value)
                        .border(
                            width = 2.dp,
                            brush = Brush.sweepGradient(
                                listOf(ElectricCyan, NeonViolet, Color.Transparent, ElectricPink, ElectricCyan)
                            ),
                            shape = CircleShape
                        )
                )

                // Avatar Frame
                Box(
                    modifier = Modifier
                        .size(208.dp)
                        .clip(CircleShape)
                        .border(3.dp, Brush.linearGradient(listOf(NeonViolet, ElectricCyan)), CircleShape)
                        .background(DarkSurface)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_jhanvi_avatar),
                        contentDescription = "Jhanvi AI 3D Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Speaking / Listening status overlay indicator
                    if (isListening || isSpeaking) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .background(DarkBackground.copy(alpha = 0.85f))
                                .padding(vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isListening) "LISTENING..." else "SPEAKING...",
                                color = if (isListening) ElectricPink else ElectricCyan,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Welcome Message & Subtitle
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Hello Boss Nabin,",
                    style = MaterialTheme.typography.titleLarge,
                    color = ElectricCyan,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "मैं आपकी क्या मदद कर सकती हूँ?",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Voice Waveform Visualizer
            VoiceWaveformVisualizer(
                isListening = isListening,
                isSpeaking = isSpeaking,
                audioLevel = waveformLevel,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Speech Error or Status
            if (speechError != null) {
                Text(
                    text = speechError ?: "",
                    color = Color(0xFFFF7070),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 4.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Latest Response Snippet Card with Repeat and Stop Speech controls
            if (lastJhanviMessage != null) {
                CyberGlowCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    borderColor = NeonViolet
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "JHANVI RESPONSE",
                                style = MaterialTheme.typography.labelSmall,
                                color = NeonPurple,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Row {
                                if (isSpeaking) {
                                    IconButton(
                                        onClick = { viewModel.stopSpeaking() },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Stop,
                                            contentDescription = "Stop Speech",
                                            tint = ElectricPink
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = { viewModel.repeatLastAnswer() },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "Repeat Speech",
                                        tint = ElectricCyan
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = lastJhanviMessage.text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary,
                            maxLines = 4
                        )
                    }
                }
            }

            // Voice Action Control Pill (Mic + Power + Repeat)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Assistant Power Toggle
                IconButton(
                    onClick = { viewModel.toggleAssistantState() },
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(if (settings.isAssistantOn) DarkSurfaceVariant else Color(0xFF33141E))
                        .border(
                            1.5.dp,
                            if (settings.isAssistantOn) OnlineGreen else Color.Red,
                            CircleShape
                        )
                        .testTag("power_assistant_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.PowerSettingsNew,
                        contentDescription = "Toggle Assistant ON/OFF",
                        tint = if (settings.isAssistantOn) OnlineGreen else Color.Red
                    )
                }

                // Central Mic Button
                FuturisticMicButton(
                    isListening = isListening,
                    isAssistantOn = settings.isAssistantOn,
                    onClick = { viewModel.toggleListening() }
                )

                // Repeat Answer Button
                IconButton(
                    onClick = { viewModel.repeatLastAnswer() },
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(DarkSurfaceVariant)
                        .border(1.5.dp, CardGlowBorder, CircleShape)
                        .testTag("repeat_answer_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Autorenew,
                        contentDescription = "Repeat Answer",
                        tint = ElectricCyan
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Navigation Hub (Cards for Advanced Features)
            Text(
                text = "SYSTEM MODULES",
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
            )

            // Grid of Primary Features
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    title = "Quantum Browser",
                    subtitle = "Search & Tabs",
                    icon = Icons.Default.Language,
                    accentColor = ElectricCyan,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo("browser") }
                )
                QuickActionCard(
                    title = "Website Coding",
                    subtitle = "HTML/CSS/JS IDE",
                    icon = Icons.Default.Code,
                    accentColor = NeonViolet,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo("coding") }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    title = "Jhanvi Home",
                    subtitle = "Smart IoT Devices",
                    icon = Icons.Default.Home,
                    accentColor = OnlineGreen,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo("jhanvi_home") }
                )
                QuickActionCard(
                    title = "Memories Vault",
                    subtitle = "Preferences & Rules",
                    icon = Icons.Default.Storage,
                    accentColor = NeonPurple,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo("memories") }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    title = "AI Chat",
                    subtitle = "Gemini Powered",
                    icon = Icons.Default.Chat,
                    accentColor = ElectricPink,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo("chat") }
                )
                QuickActionCard(
                    title = "Messages (SMS)",
                    subtitle = "Draft & Read Aloud",
                    icon = Icons.Default.Sms,
                    accentColor = ElectricCyan,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo("messages") }
                )
            }
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    CyberGlowCard(
        modifier = modifier,
        borderColor = accentColor.copy(alpha = 0.7f),
        onClick = onClick
    ) {
        Column {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = accentColor,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                fontSize = 12.sp
            )
        }
    }
}
