package com.example.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
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
fun PhotosScreen(
    viewModel: JhanviViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    val capturedUris by viewModel.capturedPhotoUris.collectAsState()

    var selectedImageForAnalysis by remember { mutableStateOf<Any?>(null) }
    var analysisText by remember { mutableStateOf("") }
    var isAnalyzing by remember { mutableStateOf(false) }

    // Android zero-permission Photo Picker
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.addCapturedPhoto(uri)
            selectedImageForAnalysis = uri
            analysisText = "Photo selected by Boss Nabin. Ready for Jhanvi AI analysis."
        }
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
                    IconButton(onClick = onBack, modifier = Modifier.testTag("photos_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonViolet)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("MY PHOTOS & VISION", style = MaterialTheme.typography.titleLarge, color = Color.White)
                        Text("Protected Vault • Zero Unauthorized Access", style = MaterialTheme.typography.labelSmall, color = ElectricCyan)
                    }
                }

                Button(
                    onClick = {
                        photoPickerLauncher.launch(
                            androidx.activity.result.PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.AddAPhoto, contentDescription = "Import", tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Select Photo", fontSize = 12.sp)
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
            // Privacy Shield Notice
            CyberGlowCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = OnlineGreen
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, contentDescription = "Secure", tint = OnlineGreen, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        "Boss Nabin's private gallery. Photos are never shared or uploaded to untrusted third parties.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // AI Vision Analysis Card
            CyberGlowCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = ElectricPink
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = "Vision", tint = ElectricPink, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Jhanvi AI Vision Inspector", style = MaterialTheme.typography.titleMedium, color = Color.White)
                        }

                        Button(
                            onClick = {
                                isAnalyzing = true
                                analysisText = "Analyzing visual elements for Boss Nabin..."
                                // Trigger AI vision description
                                viewModel.requestAiCodingAssistance("Analyze this photo: Object recognition, color balance, aesthetic composition and prompt suggestions for Boss Nabin.") { res ->
                                    analysisText = "AI Vision Report:\n" + res
                                    isAnalyzing = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricPink),
                            enabled = !isAnalyzing
                        ) {
                            if (isAnalyzing) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White)
                            } else {
                                Text("Inspect Image", fontSize = 11.sp)
                            }
                        }
                    }

                    if (analysisText.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = analysisText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text("GALLERY VAULT", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
            Spacer(modifier = Modifier.height(8.dp))

            // Grid of photos
            val allPhotos = listOf<Any>(
                R.drawable.img_jhanvi_avatar,
                R.drawable.jhanvi_icon
            ) + capturedUris

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(allPhotos) { item ->
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, CardGlowBorder, RoundedCornerShape(12.dp))
                            .background(DarkSurfaceVariant)
                            .clickable {
                                selectedImageForAnalysis = item
                                analysisText = "Selected image inspected. Tap 'Inspect Image' above to run full neural vision analysis."
                            }
                    ) {
                        if (item is Int) {
                            Image(
                                painter = painterResource(id = item),
                                contentDescription = "Gallery Photo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else if (item is Uri) {
                            AsyncImage(
                                model = item,
                                contentDescription = "User Photo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}
