package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CardGlowBorder
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.ElectricPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun JhanviDrawerContent(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = DarkBackground,
        drawerContentColor = TextPrimary,
        modifier = Modifier
            .width(310.dp)
            .fillMaxHeight()
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(NeonViolet, CardGlowBorder.copy(alpha = 0.5f), ElectricCyan)
                ),
                shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 20.dp, horizontal = 16.dp)
        ) {
            // Drawer Header with Avatar & Boss Nabin Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, NeonViolet, CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_jhanvi_avatar),
                        contentDescription = "Jhanvi Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "JHANVI AI",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "Dedicated to Boss Nabin",
                        style = MaterialTheme.typography.labelSmall,
                        color = ElectricCyan
                    )
                }
            }

            HorizontalDivider(
                color = CardGlowBorder.copy(alpha = 0.4f),
                thickness = 1.dp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Primary System Navigation
            Text(
                "SYSTEM DRAWER",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                modifier = Modifier.padding(start = 12.dp, bottom = 6.dp)
            )

            DrawerNavRow("Settings", Icons.Default.Settings, "settings", currentRoute, onNavigate)
            DrawerNavRow("Accessibility", Icons.Default.Accessibility, "accessibility", currentRoute, onNavigate)
            DrawerNavRow("Jhanvi Rules", Icons.Default.Gavel, "jhanvi_rules", currentRoute, onNavigate)
            DrawerNavRow("PC Phone", Icons.Default.Computer, "pc_phone", currentRoute, onNavigate)
            DrawerNavRow("Privacy Policy", Icons.Default.Security, "privacy_policy", currentRoute, onNavigate)
            DrawerNavRow("About / Information", Icons.Default.Info, "about", currentRoute, onNavigate)
            DrawerNavRow("Upgrade to Pro", Icons.Default.Star, "upgrade", currentRoute, onNavigate)
            DrawerNavRow("Notifications", Icons.Default.Notifications, "notifications", currentRoute, onNavigate)
            DrawerNavRow("Advanced Settings", Icons.Default.Tune, "advanced_settings", currentRoute, onNavigate)

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(
                color = CardGlowBorder.copy(alpha = 0.4f),
                thickness = 1.dp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                "CORE MODULES",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                modifier = Modifier.padding(start = 12.dp, bottom = 6.dp)
            )

            DrawerNavRow("My Photos", Icons.Default.PhotoLibrary, "photos", currentRoute, onNavigate)
            DrawerNavRow("Documents & OCR", Icons.Default.Description, "documents", currentRoute, onNavigate)
            DrawerNavRow("Marketing Strategy", Icons.Default.Campaign, "marketing", currentRoute, onNavigate)
            DrawerNavRow("Market & Finance", Icons.Default.TrendingUp, "market", currentRoute, onNavigate)
            DrawerNavRow("Jhanvi Smart Home", Icons.Default.Home, "jhanvi_home", currentRoute, onNavigate)
        }
    }
}

@Composable
fun DrawerNavRow(
    title: String,
    icon: ImageVector,
    route: String,
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val isSelected = currentRoute == route

    NavigationDrawerItem(
        label = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isSelected) ElectricCyan else TextPrimary,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) NeonViolet else TextSecondary
            )
        },
        selected = isSelected,
        onClick = { onNavigate(route) },
        shape = RoundedCornerShape(12.dp),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = DarkSurfaceVariant,
            unselectedContainerColor = Color.Transparent
        ),
        modifier = Modifier
            .padding(vertical = 2.dp)
            .testTag("drawer_item_$route")
    )
}
