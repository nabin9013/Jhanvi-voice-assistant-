package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CardGlowBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.ElectricPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.OnlineGreen
import com.example.ui.theme.TextSecondary

@Composable
fun JhanviBottomNav(
    currentScreen: String,
    isAssistantOn: Boolean,
    onNavigate: (String) -> Unit,
    onToggleAssistant: () -> Unit,
    onOpenDrawer: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(28.dp),
        color = DarkSurface.copy(alpha = 0.95f),
        shadowElevation = 18.dp,
        border = androidx.compose.foundation.BorderStroke(
            width = 1.2.dp,
            brush = Brush.horizontalGradient(
                listOf(NeonViolet, CardGlowBorder.copy(alpha = 0.7f), ElectricCyan)
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Home
            BottomNavItem(
                icon = Icons.Default.Home,
                label = "Home",
                isSelected = currentScreen == "home",
                onClick = { onNavigate("home") },
                testTag = "nav_home"
            )

            // 2. Scanner
            BottomNavItem(
                icon = Icons.Default.QrCodeScanner,
                label = "Scanner",
                isSelected = currentScreen == "scanner",
                onClick = { onNavigate("scanner") },
                testTag = "nav_scanner"
            )

            // 3. ON / OFF Assistant
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onToggleAssistant() }
                    .padding(4.dp)
                    .testTag("nav_toggle_assistant")
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(if (isAssistantOn) OnlineGreen.copy(alpha = 0.2f) else Color(0xFFFF5252).copy(alpha = 0.2f))
                        .border(1.dp, if (isAssistantOn) OnlineGreen else Color(0xFFFF5252), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PowerSettingsNew,
                        contentDescription = "Assistant Toggle",
                        tint = if (isAssistantOn) OnlineGreen else Color(0xFFFF5252),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = if (isAssistantOn) "ON" else "OFF",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isAssistantOn) OnlineGreen else Color(0xFFFF5252),
                    fontSize = 10.sp
                )
            }

            // 4. Memories
            BottomNavItem(
                icon = Icons.Default.Storage,
                label = "Memories",
                isSelected = currentScreen == "memories",
                onClick = { onNavigate("memories") },
                testTag = "nav_memories"
            )

            // 5. Chat
            BottomNavItem(
                icon = Icons.Default.Chat,
                label = "Chat",
                isSelected = currentScreen == "chat",
                onClick = { onNavigate("chat") },
                testTag = "nav_chat"
            )

            // 6. Messages
            BottomNavItem(
                icon = Icons.Default.Sms,
                label = "Messages",
                isSelected = currentScreen == "messages",
                onClick = { onNavigate("messages") },
                testTag = "nav_messages"
            )

            // 7. Three-line Menu (☰)
            BottomNavItem(
                icon = Icons.Default.Menu,
                label = "Menu",
                isSelected = false,
                onClick = { onOpenDrawer() },
                testTag = "nav_menu_drawer"
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .testTag(testTag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) NeonViolet else TextSecondary,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) ElectricCyan else TextSecondary,
            fontSize = 10.sp
        )
    }
}
