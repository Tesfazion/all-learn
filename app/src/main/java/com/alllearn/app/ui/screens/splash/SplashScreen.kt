package com.alllearn.app.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alllearn.app.ui.theme.GradientEnd
import com.alllearn.app.ui.theme.GradientStart
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToAuth: () -> Unit) {

    // ── animation states ───────────────────────────────────────────────────
    var logoVisible   by remember { mutableStateOf(false) }
    var textVisible   by remember { mutableStateOf(false) }
    var taglineVisible by remember { mutableStateOf(false) }
    var dotsVisible   by remember { mutableStateOf(false) }

    val logoAlpha   by animateFloatAsState(if (logoVisible)   1f else 0f, tween(700), label = "la")
    val logoScale   by animateFloatAsState(if (logoVisible)   1f else 0.4f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMediumLow), label = "ls")
    val textAlpha   by animateFloatAsState(if (textVisible)   1f else 0f, tween(600), label = "ta")
    val textOffsetY by animateFloatAsState(if (textVisible)   0f else 30f, tween(600), label = "to")
    val tagAlpha    by animateFloatAsState(if (taglineVisible) 1f else 0f, tween(600), label = "ga")
    val dotsAlpha   by animateFloatAsState(if (dotsVisible)   1f else 0f, tween(500), label = "da")

    // pulsing ring on logo
    val pulse = rememberInfiniteTransition(label = "pulse")
    val pulseScale by pulse.animateFloat(
        initialValue = 1f, targetValue = 1.18f,
        animationSpec = infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "ps"
    )

    // animated progress dots
    val dotProgress = rememberInfiniteTransition(label = "dots")
    val dotOffset1 by dotProgress.animateFloat(0f, 1f, infiniteRepeatable(tween(600, 0),   RepeatMode.Reverse), label = "d1")
    val dotOffset2 by dotProgress.animateFloat(0f, 1f, infiniteRepeatable(tween(600, 200), RepeatMode.Reverse), label = "d2")
    val dotOffset3 by dotProgress.animateFloat(0f, 1f, infiniteRepeatable(tween(600, 400), RepeatMode.Reverse), label = "d3")

    LaunchedEffect(Unit) {
        logoVisible = true
        delay(400)
        textVisible = true
        delay(300)
        taglineVisible = true
        delay(200)
        dotsVisible = true
        delay(1800)
        onNavigateToAuth()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0A1628), Color(0xFF0F2144), Color(0xFF162D5A))
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        // ── decorative blurred blobs ────────────────────────────────────────
        Box(
            modifier = Modifier
                .size(280.dp)
                .offset(x = (-90).dp, y = (-140).dp)
                .background(GradientStart.copy(alpha = 0.18f), CircleShape)
                .blur(60.dp)
        )
        Box(
            modifier = Modifier
                .size(240.dp)
                .offset(x = 100.dp, y = 160.dp)
                .background(GradientEnd.copy(alpha = 0.14f), CircleShape)
                .blur(60.dp)
        )

        // ── main content ────────────────────────────────────────────────────
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {

            // pulsing halo + icon
            Box(contentAlignment = Alignment.Center) {
                // outer ring
                if (logoVisible) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .scale(pulseScale)
                            .background(
                                Brush.radialGradient(
                                    listOf(Color.White.copy(alpha = 0.08f), Color.Transparent)
                                ),
                                CircleShape
                            )
                    )
                }
                // icon container
                Box(
                    modifier = Modifier
                        .alpha(logoAlpha)
                        .scale(logoScale)
                        .size(88.dp)
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF38BDF8), Color(0xFFA78BFA))
                            ),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = "All Learn Logo",
                        modifier = Modifier.size(44.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            // App name
            Text(
                text = "All Learn",
                style = MaterialTheme.typography.displayMedium.copy(
                    brush = Brush.linearGradient(listOf(Color.White, Color(0xFFB0C8FF))),
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-1).sp
                ),
                modifier = Modifier
                    .alpha(textAlpha)
                    .offset(y = textOffsetY.dp)
            )

            Spacer(Modifier.height(10.dp))

            // Tagline
            Text(
                text = "AI-powered learning built\naround your deadline & hours",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF93C5FD),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                ),
                modifier = Modifier.alpha(tagAlpha)
            )

            Spacer(Modifier.height(52.dp))

            // Three animated dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.alpha(dotsAlpha)
            ) {
                listOf(dotOffset1, dotOffset2, dotOffset3).forEach { dot ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .offset(y = (-6 * dot).dp)
                            .background(
                                Color.White.copy(alpha = 0.4f + 0.6f * dot),
                                CircleShape
                            )
                    )
                }
            }
        }

        // version badge at bottom
        Text(
            text = "v1.0  •  Free Plan",
            style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.3f)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )
    }
}
