package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.MarketItem
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
fun MarketScreen(
    viewModel: JhanviViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val marketItems by viewModel.marketItems.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var aiMarketBrief by remember { mutableStateOf("") }
    var isBriefLoading by remember { mutableStateOf(false) }

    val filteredItems = remember(marketItems, searchQuery) {
        if (searchQuery.isBlank()) marketItems
        else marketItems.filter { it.symbol.contains(searchQuery, true) || it.name.contains(searchQuery, true) }
    }

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
                    IconButton(onClick = onBack, modifier = Modifier.testTag("market_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonViolet)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("MARKET & FINANCE", style = MaterialTheme.typography.titleLarge, color = Color.White)
                        Text("Live Intelligence & Price Comparison", style = MaterialTheme.typography.labelSmall, color = ElectricCyan)
                    }
                }

                IconButton(
                    onClick = {
                        isBriefLoading = true
                        viewModel.requestAiCodingAssistance("Boss Nabin needs a live executive market intelligence brief on global markets (NIFTY, SENSEX, AI Tech equities, and Bitcoin). Provide concise bulleted highlights.") { brief ->
                            aiMarketBrief = brief
                            isBriefLoading = false
                        }
                    }
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh Market", tint = ElectricCyan)
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
            // Search field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search tickers, companies, or crypto...", color = TextSecondary, fontSize = 13.sp) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonViolet,
                    unfocusedBorderColor = CardGlowBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(14.dp))

            // AI Market Brief Card
            CyberGlowCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = OnlineGreen
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("AI EXECUTIVE MARKET BRIEF", style = MaterialTheme.typography.labelSmall, color = OnlineGreen, fontWeight = FontWeight.Bold)
                        Button(
                            onClick = {
                                isBriefLoading = true
                                viewModel.requestAiCodingAssistance("Analyze current economic and tech trends for Boss Nabin's portfolio.") { res ->
                                    aiMarketBrief = res
                                    isBriefLoading = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = OnlineGreen.copy(alpha = 0.25f)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (isBriefLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(14.dp), color = OnlineGreen)
                            } else {
                                Text("Update Brief", color = OnlineGreen, fontSize = 11.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (aiMarketBrief.isNotBlank()) aiMarketBrief else "Boss Nabin, markets are showing robust momentum in semiconductors and cloud infrastructure. NIFTY and SENSEX hold positive gains.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("GLOBAL ASSET TICKERS", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredItems) { item ->
                    MarketTickerRow(item = item)
                }
            }
        }
    }
}

@Composable
fun MarketTickerRow(item: MarketItem) {
    CyberGlowCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = if (item.isPositive) OnlineGreen.copy(alpha = 0.5f) else Color(0xFFFF5252).copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(item.symbol, style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                Text(item.name, style = MaterialTheme.typography.bodyMedium, color = TextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(item.category, style = MaterialTheme.typography.labelSmall, color = ElectricCyan, fontSize = 10.sp)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(item.price, style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (item.isPositive) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Filled.TrendingDown,
                        contentDescription = null,
                        tint = if (item.isPositive) OnlineGreen else Color(0xFFFF5252),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        item.change,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (item.isPositive) OnlineGreen else Color(0xFFFF5252),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
