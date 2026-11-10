package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.DocumentItem
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
fun DocumentsScreen(
    viewModel: JhanviViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    val documents by viewModel.documents.collectAsState()

    var showCreateDocDialog by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newContent by remember { mutableStateOf("") }
    var newCategory by remember { mutableStateOf("General") }

    var selectedDocForSummary by remember { mutableStateOf<DocumentItem?>(null) }
    var summaryText by remember { mutableStateOf("") }
    var isSummarizing by remember { mutableStateOf(false) }

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
                    IconButton(onClick = onBack, modifier = Modifier.testTag("docs_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonViolet)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("DOCUMENTS & OCR", style = MaterialTheme.typography.titleLarge, color = Color.White)
                        Text("PDFs • Smart Notes • AI Summaries", style = MaterialTheme.typography.labelSmall, color = ElectricCyan)
                    }
                }

                Button(
                    onClick = { showCreateDocDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "New Doc", tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("New Doc", fontSize = 12.sp)
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
            // Scanner / OCR Action Card
            CyberGlowCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = ElectricCyan
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CameraAlt, contentDescription = "Scanner", tint = ElectricCyan, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Camera OCR Document Scanner", style = MaterialTheme.typography.titleMedium, color = Color.White)
                            Text("Instant text extraction & summarization", style = MaterialTheme.typography.bodyMedium, color = TextSecondary, fontSize = 12.sp)
                        }
                    }

                    Button(
                        onClick = {
                            viewModel.navigateTo("scanner")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan.copy(alpha = 0.25f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Launch Scanner", color = ElectricCyan, fontSize = 11.sp)
                    }
                }
            }

            // AI Summary Viewer
            if (selectedDocForSummary != null) {
                Spacer(modifier = Modifier.height(14.dp))
                CyberGlowCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = ElectricPink
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("AI SUMMARY // ${selectedDocForSummary?.title}", style = MaterialTheme.typography.labelSmall, color = ElectricPink, fontWeight = FontWeight.Bold)
                            IconButton(onClick = { selectedDocForSummary = null; summaryText = "" }) {
                                Text("✕", color = TextSecondary)
                            }
                        }

                        if (isSummarizing) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = ElectricPink)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("Jhanvi is analyzing document contents for Boss Nabin...", color = TextSecondary, fontSize = 13.sp)
                            }
                        } else {
                            Text(summaryText, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, fontSize = 13.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("SECURE STORED DOCUMENTS", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(documents, key = { it.id }) { doc ->
                    CyberGlowCard(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = CardGlowBorder.copy(alpha = 0.6f)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Description, contentDescription = "Document", tint = NeonPurple)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(doc.title, style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                                Text(doc.date, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(doc.fullText, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, maxLines = 3)

                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Category: ${doc.category}", style = MaterialTheme.typography.labelSmall, color = ElectricCyan)

                                Row {
                                    Button(
                                        onClick = {
                                            selectedDocForSummary = doc
                                            isSummarizing = true
                                            viewModel.summarizeDocument(doc) { sum ->
                                                summaryText = sum
                                                isSummarizing = false
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("AI Summarize", fontSize = 11.sp)
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    IconButton(
                                        onClick = {
                                            val sendIntent = android.content.Intent().apply {
                                                action = android.content.Intent.ACTION_SEND
                                                putExtra(android.content.Intent.EXTRA_TITLE, doc.title)
                                                putExtra(android.content.Intent.EXTRA_TEXT, doc.fullText)
                                                type = "text/plain"
                                            }
                                            context.startActivity(android.content.Intent.createChooser(sendIntent, "Share Document"))
                                        }
                                    ) {
                                        Icon(Icons.Default.Share, contentDescription = "Share", tint = TextSecondary)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showCreateDocDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDocDialog = false },
            containerColor = DarkSurface,
            title = { Text("Create New Document", color = Color.White) },
            text = {
                Column {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonViolet,
                            unfocusedBorderColor = CardGlowBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newCategory,
                        onValueChange = { newCategory = it },
                        label = { Text("Category (e.g. Strategy, Finance, Personal)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonViolet,
                            unfocusedBorderColor = CardGlowBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newContent,
                        onValueChange = { newContent = it },
                        label = { Text("Document Content") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonViolet,
                            unfocusedBorderColor = CardGlowBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTitle.isNotBlank() && newContent.isNotBlank()) {
                            viewModel.addDocument(newTitle, newContent, newCategory)
                            showCreateDocDialog = false
                            newTitle = ""
                            newContent = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonViolet)
                ) {
                    Text("Save Document")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showCreateDocDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}
