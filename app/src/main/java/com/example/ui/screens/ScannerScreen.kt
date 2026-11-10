package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.ui.theme.OnlineGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.JhanviViewModel

@Composable
fun ScannerScreen(
    viewModel: JhanviViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }
    val context = LocalContext.current

    var scannedResult by remember { mutableStateOf<String?>(null) }
    var isSimulatingScan by remember { mutableStateOf(false) }

    // Laser scan animation
    val laserY = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        laserY.animateTo(
            targetValue = 240f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("scanner_back_button")) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonViolet)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("JHANVI CYBER SCANNER", style = MaterialTheme.typography.titleLarge, color = Color.White)
                    Text("QR Code, Barcode & OCR Document Reader", style = MaterialTheme.typography.labelSmall, color = ElectricCyan)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Viewfinder Target Frame
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(DarkSurfaceVariant)
                    .border(2.dp, Brush.linearGradient(listOf(ElectricCyan, NeonViolet, ElectricPink)), RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Corner targeting crosshairs
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = "Target",
                    tint = ElectricCyan.copy(alpha = 0.35f),
                    modifier = Modifier.size(140.dp)
                )

                // Animated glowing laser beam
                Box(
                    modifier = Modifier
                        .offset(y = (laserY.value - 120f).dp)
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color.Transparent, ElectricCyan, NeonViolet, Color.Transparent)
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Align QR code, barcode or document text within frame",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        isSimulatingScan = true
                        scannedResult = "JHANVI-SECURE-KEY: https://boss-nabin-portal.quantum?auth=ok"
                        Toast.makeText(context, "QR Decoded Successfully", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Scan QR Code", fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        scannedResult = "OCR Extracted Text:\n\"INVOICE #9821\nClient: Boss Nabin\nAmount: ₹45,000\nStatus: VERIFIED\""
                        viewModel.addDocument("Scanned Invoice 9821", "INVOICE #9821\nClient: Boss Nabin\nAmount: ₹45,000\nStatus: VERIFIED", "Scanned")
                        Toast.makeText(context, "Text extracted & saved to Documents", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan.copy(alpha = 0.25f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.DocumentScanner, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("OCR Text Scan", color = ElectricCyan, fontSize = 12.sp)
                }
            }

            if (scannedResult != null) {
                Spacer(modifier = Modifier.height(20.dp))
                CyberGlowCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = OnlineGreen
                ) {
                    Column {
                        Text("SCAN DECODED", style = MaterialTheme.typography.labelSmall, color = OnlineGreen, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(scannedResult ?: "", style = MaterialTheme.typography.bodyLarge, color = TextPrimary, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}
