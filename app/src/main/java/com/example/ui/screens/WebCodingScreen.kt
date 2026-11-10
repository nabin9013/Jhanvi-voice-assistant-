package com.example.ui.screens

import android.webkit.WebView
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
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
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.JhanviViewModel

@Composable
fun WebCodingScreen(
    viewModel: JhanviViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    val webProject by viewModel.webProject.collectAsState()

    var htmlCode by remember { mutableStateOf(webProject.html) }
    var cssCode by remember { mutableStateOf(webProject.css) }
    var jsCode by remember { mutableStateOf(webProject.js) }

    var selectedTab by remember { mutableIntStateOf(0) } // 0=HTML, 1=CSS, 2=JS, 3=PREVIEW
    var aiAssistancePrompt by remember { mutableStateOf("") }
    var aiAssistanceResult by remember { mutableStateOf("") }
    var isGeneratingAiCode by remember { mutableStateOf(false) }

    fun getCombinedHtml(): String {
        return """
            $htmlCode
            <style>
            $cssCode
            </style>
            <script>
            $jsCode
            </script>
        """.trimIndent()
    }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack, modifier = Modifier.testTag("coding_back_button")) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonViolet)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text("WEBSITE CODING IDE", style = MaterialTheme.typography.titleLarge, color = Color.White)
                            Text("HTML • CSS • JS Live Compiler", style = MaterialTheme.typography.labelSmall, color = ElectricCyan)
                        }
                    }

                    Row {
                        IconButton(
                            onClick = {
                                viewModel.updateWebCode(htmlCode, cssCode, jsCode)
                                Toast.makeText(context, "Project Saved Successfully", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(Icons.Default.Save, contentDescription = "Save Project", tint = ElectricCyan)
                        }
                        IconButton(
                            onClick = {
                                val sendIntent = android.content.Intent().apply {
                                    action = android.content.Intent.ACTION_SEND
                                    putExtra(android.content.Intent.EXTRA_TEXT, getCombinedHtml())
                                    type = "text/plain"
                                }
                                context.startActivity(android.content.Intent.createChooser(sendIntent, "Export Source Code"))
                            }
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "Export Code", tint = NeonPurple)
                        }
                    }
                }

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = DarkSurfaceContainer,
                    contentColor = NeonViolet
                ) {
                    listOf("HTML", "CSS", "JS", "⚡ LIVE PREVIEW").forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    title,
                                    color = if (selectedTab == index) ElectricCyan else TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(12.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    // HTML Editor
                    CodeEditorArea(
                        label = "HTML5 Document Structure",
                        code = htmlCode,
                        onCodeChange = { htmlCode = it }
                    )
                }
                1 -> {
                    // CSS Editor
                    CodeEditorArea(
                        label = "Cascading Style Sheets (CSS3)",
                        code = cssCode,
                        onCodeChange = { cssCode = it }
                    )
                }
                2 -> {
                    // JS Editor
                    CodeEditorArea(
                        label = "JavaScript Logic (ES6)",
                        code = jsCode,
                        onCodeChange = { jsCode = it }
                    )
                }
                3 -> {
                    // Live Preview in WebView
                    CyberGlowCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        borderColor = NeonViolet
                    ) {
                        AndroidView(
                            factory = { ctx ->
                                WebView(ctx).apply {
                                    settings.javaScriptEnabled = true
                                    settings.domStorageEnabled = true
                                    loadDataWithBaseURL(null, getCombinedHtml(), "text/html", "UTF-8", null)
                                }
                            },
                            update = { webView ->
                                webView.loadDataWithBaseURL(null, getCombinedHtml(), "text/html", "UTF-8", null)
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // AI Coding Assistant Section
            CyberGlowCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = ElectricPink
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = "AI Coding", tint = ElectricPink, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Jhanvi AI Coding Assistant", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = aiAssistancePrompt,
                            onValueChange = { aiAssistancePrompt = it },
                            placeholder = { Text("Ask Jhanvi: 'Add a glowing neon button'...", color = TextSecondary, fontSize = 12.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonViolet,
                                unfocusedBorderColor = CardGlowBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (aiAssistancePrompt.isNotBlank()) {
                                    isGeneratingAiCode = true
                                    viewModel.requestAiCodingAssistance(aiAssistancePrompt) { result ->
                                        aiAssistanceResult = result
                                        isGeneratingAiCode = false
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
                            enabled = !isGeneratingAiCode
                        ) {
                            if (isGeneratingAiCode) {
                                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White)
                            } else {
                                Text("Ask AI")
                            }
                        }
                    }

                    if (aiAssistanceResult.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = aiAssistanceResult,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary,
                            modifier = Modifier
                                .background(DarkSurfaceVariant)
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CodeEditorArea(
    label: String,
    code: String,
    onCodeChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = ElectricCyan, modifier = Modifier.padding(bottom = 4.dp))
        OutlinedTextField(
            value = code,
            onValueChange = onCodeChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            textStyle = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp,
                color = Color(0xFF80FFEA)
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonViolet,
                unfocusedBorderColor = CardGlowBorder.copy(alpha = 0.6f),
                focusedContainerColor = DarkSurface,
                unfocusedContainerColor = DarkSurface
            )
        )
    }
}
