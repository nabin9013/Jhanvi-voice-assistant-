package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.telephony.SmsManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.SmsItem
import com.example.ui.components.CyberGlowCard
import com.example.ui.theme.CardGlowBorder
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.ElectricPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.JhanviViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MessagesScreen(
    viewModel: JhanviViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    val smsList by viewModel.smsList.collectAsState()
    val smsPermissionGranted by viewModel.smsPermissionGranted.collectAsState()
    val settings by viewModel.settings.collectAsState()

    var showDraftDialog by remember { mutableStateOf(false) }
    var draftRecipient by remember { mutableStateOf("") }
    var draftMessage by remember { mutableStateOf("") }
    var confirmSendDialog by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val readGranted = permissions[android.Manifest.permission.READ_SMS] ?: false
        if (readGranted) {
            viewModel.loadSmsMessages()
        } else {
            Toast.makeText(context, "READ_SMS permission is required to view messages", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.READ_SMS,
                android.Manifest.permission.SEND_SMS
            )
        )
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
                    IconButton(onClick = onBack, modifier = Modifier.testTag("messages_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonViolet)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("SMS MESSAGES", style = MaterialTheme.typography.titleLarge, color = Color.White)
                        Text("Authorized Access Only • User Confirmation Protected", style = MaterialTheme.typography.labelSmall, color = ElectricCyan)
                    }
                }

                IconButton(onClick = { viewModel.loadSmsMessages() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = ElectricCyan)
                }
            }
        },
        floatingActionButton = {
            Button(
                onClick = { showDraftDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.testTag("draft_sms_fab")
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Draft SMS", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Draft Reply / SMS", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Privacy Warning Banner
            CyberGlowCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = ElectricCyan
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = "Security", tint = ElectricCyan, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Jhanvi Assistant accesses SMS only with your explicit permission and NEVER sends any message without your final confirmation.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!smsPermissionGranted && smsList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Sms, contentDescription = "SMS", tint = TextSecondary, modifier = Modifier.size(54.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("SMS Permission Required", color = TextPrimary, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Grant SMS permission to allow Jhanvi to read incoming alerts and draft messages.", color = TextSecondary, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                permissionLauncher.launch(
                                    arrayOf(
                                        android.Manifest.permission.READ_SMS,
                                        android.Manifest.permission.SEND_SMS
                                    )
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonViolet)
                        ) {
                            Text("Grant Permission")
                        }
                    }
                }
            } else if (smsList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No SMS messages found on device.", color = TextSecondary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(smsList, key = { it.id }) { sms ->
                        SmsCardItem(
                            sms = sms,
                            onReadAloud = {
                                viewModel.voiceService.speak(
                                    "Message from ${sms.address}: ${sms.body}",
                                    settings.speechPitch,
                                    settings.speechSpeed
                                )
                            },
                            onReply = {
                                draftRecipient = sms.address
                                draftMessage = "Hello, this is Boss Nabin."
                                showDraftDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    // Draft SMS Dialog
    if (showDraftDialog) {
        AlertDialog(
            onDismissRequest = { showDraftDialog = false },
            containerColor = DarkSurface,
            title = { Text("Draft Message (Boss Nabin)", color = Color.White, style = MaterialTheme.typography.titleLarge) },
            text = {
                Column {
                    OutlinedTextField(
                        value = draftRecipient,
                        onValueChange = { draftRecipient = it },
                        label = { Text("Recipient Phone Number") },
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
                        value = draftMessage,
                        onValueChange = { draftMessage = it },
                        label = { Text("Message Body") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
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
                        showDraftDialog = false
                        confirmSendDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonViolet)
                ) {
                    Text("Review & Send")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDraftDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }

    // Confirmation Security Dialog (Never send without user confirmation)
    if (confirmSendDialog) {
        AlertDialog(
            onDismissRequest = { confirmSendDialog = false },
            containerColor = DarkSurface,
            title = { Text("Confirm Send Action", color = ElectricPink, style = MaterialTheme.typography.titleLarge) },
            text = {
                Text(
                    "Boss Nabin, do you explicitly authorize Jhanvi to send this SMS to $draftRecipient?\n\n\"$draftMessage\"",
                    color = TextPrimary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        confirmSendDialog = false
                        try {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("smsto:$draftRecipient")
                                putExtra("sms_body", draftMessage)
                            }
                            context.startActivity(intent)
                            Toast.makeText(context, "Opened SMS app for Boss Nabin", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(context, "Could not launch SMS: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonViolet)
                ) {
                    Text("Confirm & Open SMS")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { confirmSendDialog = false }) {
                    Text("Abort", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
fun SmsCardItem(
    sms: SmsItem,
    onReadAloud: () -> Unit,
    onReply: () -> Unit
) {
    val dateStr = remember(sms.date) {
        val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        sdf.format(Date(sms.date))
    }

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
                Text(sms.address, style = MaterialTheme.typography.titleMedium, color = ElectricCyan, fontWeight = FontWeight.Bold)
                Text(dateStr, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(sms.body, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onReadAloud) {
                    Icon(Icons.Default.VolumeUp, contentDescription = "Read Aloud", tint = ElectricPink)
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onReply) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Reply", tint = NeonPurple)
                }
            }
        }
    }
}
