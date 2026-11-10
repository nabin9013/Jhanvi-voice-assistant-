package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CardGlowBorder
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.ElectricPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.OnlineGreen
import kotlin.math.sin

@Composable
fun CyberGlowCard(
    modifier: Modifier = Modifier,
    borderColor: Color = CardGlowBorder,
    backgroundColor: Color = DarkSurfaceVariant.copy(alpha = 0.85f),
    cornerRadius: Dp = 16.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(cornerRadius),
                ambientColor = borderColor.copy(alpha = 0.4f),
                spotColor = borderColor.copy(alpha = 0.6f)
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
            .border(
                width = 1.2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        borderColor.copy(alpha = 0.9f),
                        ElectricCyan.copy(alpha = 0.4f),
                        NeonViolet.copy(alpha = 0.8f)
                    )
                ),
                shape = RoundedCornerShape(cornerRadius)
            )
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            )
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun VoiceWaveformVisualizer(
    isListening: Boolean,
    isSpeaking: Boolean,
    audioLevel: Float,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 8.dp)
    ) {
        val width = size.width
        val height = size.height
        val midY = height / 2f
        val points = 60
        val step = width / points

        val active = isListening || isSpeaking
        val baseAmp = if (active) (height * 0.35f * (0.3f + audioLevel * 0.7f)) else (height * 0.08f)

        // Draw glowing background glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(NeonViolet.copy(alpha = if (active) 0.35f else 0.1f), Color.Transparent),
                center = Offset(width / 2f, midY),
                radius = width * 0.4f
            )
        )

        // Draw multiple sine wave harmonics
        val path1 = Path()
        val path2 = Path()

        for (i in 0..points) {
            val x = i * step
            val angle = (i.toFloat() / points) * 4 * Math.PI.toFloat() + phase
            val y1 = midY + sin(angle) * baseAmp * sin((i.toFloat() / points) * Math.PI.toFloat())
            val y2 = midY - sin(angle * 1.5f + 1f) * (baseAmp * 0.7f) * sin((i.toFloat() / points) * Math.PI.toFloat())

            if (i == 0) {
                path1.moveTo(x, y1)
                path2.moveTo(x, y2)
            } else {
                path1.lineTo(x, y1)
                path2.lineTo(x, y2)
            }
        }

        drawPath(
            path = path1,
            brush = Brush.horizontalGradient(
                listOf(ElectricCyan, NeonViolet, NeonPurple, ElectricPink)
            ),
            style = Stroke(width = if (active) 3.5.dp.toPx() else 2.dp.toPx(), cap = StrokeCap.Round)
        )

        drawPath(
            path = path2,
            brush = Brush.horizontalGradient(
                listOf(NeonPurple, ElectricPink, ElectricCyan)
            ),
            style = Stroke(width = if (active) 2.5.dp.toPx() else 1.5.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Composable
fun FuturisticMicButton(
    isListening: Boolean,
    isAssistantOn: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.22f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        // Outer pulsing ring when listening
        if (isListening && isAssistantOn) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(NeonViolet.copy(alpha = 0.5f), Color.Transparent)
                        )
                    )
            )
        }

        Surface(
            onClick = onClick,
            shape = CircleShape,
            color = when {
                !isAssistantOn -> Color(0xFF2B203B)
                isListening -> ElectricPink
                else -> NeonViolet
            },
            shadowElevation = 16.dp,
            modifier = Modifier
                .size(64.dp)
                .testTag("main_mic_button")
                .border(
                    width = 2.dp,
                    brush = Brush.sweepGradient(
                        listOf(ElectricCyan, NeonViolet, ElectricPink, ElectricCyan)
                    ),
                    shape = CircleShape
                )
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = when {
                        !isAssistantOn -> Icons.Default.MicOff
                        isListening -> Icons.Default.Mic
                        else -> Icons.Default.Mic
                    },
                    contentDescription = "Microphone",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun StatusBadge(
    isOnline: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurfaceVariant.copy(alpha = 0.7f))
            .border(0.8.dp, CardGlowBorder.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(if (isOnline) OnlineGreen else Color.Red)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = if (isOnline) "JHANVI // ONLINE" else "SYSTEM // OFFLINE",
            style = MaterialTheme.typography.labelSmall,
            color = if (isOnline) OnlineGreen else Color(0xFFFF6666),
            fontSize = 11.sp
        )
    }
}
