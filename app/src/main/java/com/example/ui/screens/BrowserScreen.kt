package com.example.ui.screens

import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.CardGlowBorder
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceContainer
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.ElectricPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.JhanviViewModel

@Composable
fun BrowserScreen(
    viewModel: JhanviViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    var currentUrlInput by remember { mutableStateOf(viewModel.browserUrl.value) }
    var isLoading by remember { mutableStateOf(false) }

    val bookmarks by viewModel.browserBookmarks.collectAsState()
    val isListening by viewModel.isListening.collectAsState()

    var tabs by remember { mutableStateOf(listOf("https://www.google.com", "https://news.google.com")) }
    var activeTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurface)
            ) {
                // Top control bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("browser_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonViolet)
                    }

                    IconButton(
                        onClick = { webViewInstance?.goBack() },
                        enabled = webViewInstance?.canGoBack() == true
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Browser Back", tint = if (webViewInstance?.canGoBack() == true) ElectricCyan else TextSecondary)
                    }

                    IconButton(
                        onClick = { webViewInstance?.goForward() },
                        enabled = webViewInstance?.canGoForward() == true
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Browser Forward", tint = if (webViewInstance?.canGoForward() == true) ElectricCyan else TextSecondary)
                    }

                    OutlinedTextField(
                        value = currentUrlInput,
                        onValueChange = { currentUrlInput = it },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("browser_url_input"),
                        placeholder = { Text("Search or type URL...", color = TextSecondary, fontSize = 12.sp) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonViolet,
                            unfocusedBorderColor = CardGlowBorder.copy(alpha = 0.5f),
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            cursorColor = ElectricCyan
                        ),
                        shape = RoundedCornerShape(20.dp),
                        trailingIcon = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { viewModel.toggleListening() },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Mic,
                                        contentDescription = "Voice Search",
                                        tint = if (isListening) ElectricPink else NeonPurple,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        val target = if (currentUrlInput.startsWith("http://") || currentUrlInput.startsWith("https://")) {
                                            currentUrlInput
                                        } else {
                                            "https://www.google.com/search?q=" + android.net.Uri.encode(currentUrlInput)
                                        }
                                        webViewInstance?.loadUrl(target)
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Search, contentDescription = "Search", tint = ElectricCyan, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    )

                    IconButton(onClick = { webViewInstance?.reload() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reload", tint = ElectricCyan)
                    }
                }

                // Browser Tabs Row
                ScrollableTabRow(
                    selectedTabIndex = activeTabIndex,
                    containerColor = DarkSurfaceContainer,
                    edgePadding = 8.dp,
                    divider = {}
                ) {
                    tabs.forEachIndexed { index, tabUrl ->
                        Tab(
                            selected = activeTabIndex == index,
                            onClick = {
                                activeTabIndex = index
                                currentUrlInput = tabUrl
                                webViewInstance?.loadUrl(tabUrl)
                            },
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = tabUrl.replace("https://", "").take(14) + "...",
                                        color = if (activeTabIndex == index) ElectricCyan else TextSecondary,
                                        fontSize = 11.sp
                                    )
                                    if (tabs.size > 1) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = "Close tab",
                                            tint = TextSecondary,
                                            modifier = Modifier
                                                .size(14.dp)
                                                .clickable {
                                                    val newTabs = tabs.toMutableList()
                                                    newTabs.removeAt(index)
                                                    tabs = newTabs
                                                    activeTabIndex = (index - 1).coerceAtLeast(0)
                                                    val nextUrl = tabs[activeTabIndex]
                                                    currentUrlInput = nextUrl
                                                    webViewInstance?.loadUrl(nextUrl)
                                                }
                                        )
                                    }
                                }
                            }
                        )
                    }

                    // Add new tab button
                    IconButton(
                        onClick = {
                            val newUrl = "https://www.google.com"
                            tabs = tabs + newUrl
                            activeTabIndex = tabs.size - 1
                            currentUrlInput = newUrl
                            webViewInstance?.loadUrl(newUrl)
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "New Tab", tint = NeonViolet)
                    }
                }

                // Quick Bookmarks Bar
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(bookmarks) { (url, label) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(DarkSurfaceVariant)
                                .border(0.8.dp, CardGlowBorder.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                .clickable {
                                    currentUrlInput = url
                                    webViewInstance?.loadUrl(url)
                                }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Bookmark, contentDescription = null, tint = ElectricPink, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(label, color = TextPrimary, fontSize = 11.sp)
                        }
                    }
                }

                if (isLoading) {
                    androidx.compose.material3.LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = NeonViolet,
                        trackColor = DarkSurface
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AndroidView(
                factory = { ctx ->
                    WebView(ctx).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.builtInZoomControls = true
                        settings.displayZoomControls = false

                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                isLoading = newProgress < 100
                            }
                        }

                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                isLoading = true
                                url?.let { currentUrlInput = it }
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                isLoading = false
                                url?.let { currentUrlInput = it }
                            }
                        }

                        loadUrl(tabs[activeTabIndex])
                        webViewInstance = this
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
