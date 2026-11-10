package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.DeviceType
import com.example.data.models.SmartDevice
import com.example.ui.components.CyberGlowCard
import com.example.ui.theme.CardGlowBorder
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.ElectricPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.OnlineGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.JhanviViewModel

@Composable
fun JhanviHomeScreen(
    viewModel: JhanviViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    val devices by viewModel.smartDevices.collectAsState()
    val isListening by viewModel.isListening.collectAsState()

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurface)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("jhanvi_home_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonViolet)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("JHANVI SMART HOME", style = MaterialTheme.typography.titleLarge, color = Color.White)
                        Text("Boss Nabin's IoT Automation Hub", style = MaterialTheme.typography.labelSmall, color = ElectricCyan)
                    }
                }

                IconButton(
                    onClick = { viewModel.toggleListening() },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(if (isListening) ElectricPink else NeonViolet)
                ) {
                    Icon(Icons.Default.Mic, contentDescription = "Voice Control", tint = Color.White)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Voice command instructions
            CyberGlowCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonViolet
            ) {
                Column {
                    Text("SMART VOICE AUTOMATION", style = MaterialTheme.typography.labelSmall, color = NeonPurple, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "You can speak: \"Turn on light\", \"Turn off light\", or \"Activate lockdown\". Jhanvi will trigger device relays instantly for Boss Nabin.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick master controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        devices.forEach { viewModel.toggleDevice(it.id, true) }
                        Toast.makeText(context, "All Devices Activated", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OnlineGreen.copy(alpha = 0.25f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Turn All ON", color = OnlineGreen, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        devices.forEach { viewModel.toggleDevice(it.id, false) }
                        Toast.makeText(context, "All Devices Deactivated", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252).copy(alpha = 0.25f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Turn All OFF", color = Color(0xFFFF5252), fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("CONNECTED IOT CONTROLLERS", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(devices, key = { it.id }) { device ->
                    SmartDeviceCard(
                        device = device,
                        onToggle = { viewModel.toggleDevice(device.id) },
                        onValueChange = { newVal -> viewModel.updateDeviceValue(device.id, newVal) }
                    )
                }
            }
        }
    }
}

@Composable
fun SmartDeviceCard(
    device: SmartDevice,
    onToggle: () -> Unit,
    onValueChange: (Int) -> Unit
) {
    val icon = when (device.type) {
        DeviceType.LIGHT -> Icons.Default.Lightbulb
        DeviceType.PLUG -> Icons.Default.Power
        DeviceType.AC -> Icons.Default.AcUnit
        DeviceType.SECURITY -> Icons.Default.Security
        DeviceType.ROBOT -> Icons.Default.SmartToy
    }

    val glowColor = if (device.isOn) OnlineGreen else Color(0xFF6B5885)

    CyberGlowCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = glowColor.copy(alpha = 0.8f)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (device.isOn) NeonViolet else DarkSurfaceVariant)
                            .border(1.dp, glowColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, contentDescription = null, tint = if (device.isOn) Color.White else TextSecondary)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(device.name, style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                        Text("${device.room} • ${if (device.isOn) "Online" else "Standby"}", style = MaterialTheme.typography.bodyMedium, color = if (device.isOn) OnlineGreen else TextSecondary, fontSize = 12.sp)
                    }
                }

                Switch(
                    checked = device.isOn,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = NeonViolet,
                        uncheckedThumbColor = TextSecondary,
                        uncheckedTrackColor = DarkSurfaceVariant
                    )
                )
            }

            if (device.isOn && (device.type == DeviceType.LIGHT || device.type == DeviceType.AC)) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (device.type == DeviceType.LIGHT) "Brightness: ${device.value}%" else "Target Temp: ${device.value}°C",
                        style = MaterialTheme.typography.labelSmall,
                        color = ElectricCyan,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Slider(
                        value = device.value.toFloat(),
                        onValueChange = { onValueChange(it.toInt()) },
                        valueRange = if (device.type == DeviceType.LIGHT) 10f..100f else 16f..30f,
                        modifier = Modifier.weight(1f),
                        colors = SliderDefaults.colors(
                            thumbColor = ElectricCyan,
                            activeTrackColor = NeonViolet,
                            inactiveTrackColor = DarkSurfaceVariant
                        )
                    )
                }
            }
        }
    }
}
