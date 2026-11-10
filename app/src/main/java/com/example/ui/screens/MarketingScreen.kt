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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CyberGlowCard
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
import com.example.viewmodel.JhanviViewModel

@Composable
fun MarketingScreen(
    viewModel: JhanviViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    var businessTopic by remember { mutableStateOf("") }
    var selectedGoal by remember { mutableStateOf("Social Media Viral Copy") }
    var generatedCampaign by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(false) }

    val campaignGoals = listOf(
        "Social Media Viral Copy",
        "High-Conversion Ad Copy",
        "SEO Strategy & Keywords",
        "PR & Brand Launch Blueprint",
        "Influencer Outreach Script"
    )

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurface)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("marketing_back_button")) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonViolet)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("MARKETING & GROWTH", style = MaterialTheme.typography.titleLarge, color = Color.White)
                    Text("AI Campaign Strategist for Boss Nabin", style = MaterialTheme.typography.labelSmall, color = ElectricCyan)
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
            CyberGlowCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonViolet
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Campaign, contentDescription = "Marketing", tint = NeonViolet, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Campaign Objective", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = businessTopic,
                        onValueChange = { businessTopic = it },
                        placeholder = { Text("Enter product, business, or venture name...", color = TextSecondary, fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonViolet,
                            unfocusedBorderColor = CardGlowBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Select Target Strategy:", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        campaignGoals.take(3).forEach { goal ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (selectedGoal == goal) NeonViolet else DarkSurfaceVariant)
                                    .border(1.dp, CardGlowBorder, RoundedCornerShape(12.dp))
                                    .clickable { selectedGoal = goal }
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    goal.take(14) + "...",
                                    color = if (selectedGoal == goal) Color.White else TextPrimary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (businessTopic.isNotBlank()) {
                                isGenerating = true
                                val prompt = "Boss Nabin needs a high-impact $selectedGoal for his venture '$businessTopic'. Include catchy hooks, psychological urgency, Hindi/English target hashtags, and bulleted action steps."
                                viewModel.requestAiCodingAssistance(prompt) { res ->
                                    generatedCampaign = res
                                    isGenerating = false
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isGenerating
                    ) {
                        if (isGenerating) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White)
                        } else {
                            Text("Generate AI Strategy", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (generatedCampaign.isNotBlank()) {
                CyberGlowCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    borderColor = ElectricCyan
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("AI GENERATED STRATEGY", style = MaterialTheme.typography.labelSmall, color = ElectricCyan, fontWeight = FontWeight.Bold)
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                    clipboard.setPrimaryClip(android.content.ClipData.newPlainText("Marketing", generatedCampaign))
                                    Toast.makeText(context, "Copied campaign strategy", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = TextSecondary, modifier = Modifier.size(18.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            item {
                                Text(
                                    text = generatedCampaign,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Lightbulb, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Ready to craft viral campaigns for Boss Nabin.", color = TextSecondary, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
