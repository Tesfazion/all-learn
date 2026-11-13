package com.alllearn.app.ui.screens.review

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alllearn.app.ui.components.AllLearnTopAppBar

data class FlashcardItem(val question: String, val answer: String)

@Composable
fun FlashcardScreen(courseTitle: String, onBackClick: () -> Unit) {

    val cards = remember {
        listOf(
            FlashcardItem(
                "What is the key advantage of structured weekly learning plans?",
                "They adapt automatically to learner deadlines and free weekly hours while grounding lessons in verified sources."
            ),
            FlashcardItem(
                "How does the AI Helper Bot assist your study session?",
                "Provides instant 24/7 context-aware explanations based on your current course topic and lesson sources."
            ),
            FlashcardItem(
                "What happens if you fall behind target study hours (<70%)?",
                "Weekly check-in offers two choices: Compress (trim lower priority lessons) or Extend (move target deadline back)."
            ),
            FlashcardItem(
                "What is the purpose of milestone projects?",
                "They consolidate everything learned in a phase through a hands-on applied challenge."
            ),
            FlashcardItem(
                "Why does All Learn ground lessons in trusted sources?",
                "To ensure accuracy — AI-generated content is verified against official documentation and reputable references."
            )
        )
    }

    var currentIndex by remember { mutableStateOf(0) }
    var isFlipped    by remember { mutableStateOf(false) }
    var easyCount    by remember { mutableStateOf(0) }
    var hardCount    by remember { mutableStateOf(0) }
    var isDone       by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue  = if (isFlipped) 180f else 0f,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label        = "flip"
    )

    Scaffold(
        topBar = {
            AllLearnTopAppBar(title = "Flashcards — $courseTitle", onBackClick = onBackClick)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        // ── completion screen ────────────────────────────────────────────────
        if (isDone) {
            Column(
                Modifier.fillMaxSize().padding(padding).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("🎉", fontSize = 56.sp)
                Spacer(Modifier.height(16.dp))
                Text("Review Complete!",
                    style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(
                    "${cards.size} cards  •  $easyCount easy  •  $hardCount hard",
                    style     = MaterialTheme.typography.bodyMedium,
                    color     = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick  = {
                        currentIndex = 0; isFlipped = false
                        easyCount = 0; hardCount = 0; isDone = false
                    },
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape    = RoundedCornerShape(14.dp)
                ) { Text("Review Again") }
                Spacer(Modifier.height(12.dp))
                OutlinedButton(
                    onClick  = onBackClick,
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape    = RoundedCornerShape(14.dp)
                ) { Text("Back to Course") }
            }
            return@Scaffold
        }

        val card = cards[currentIndex]

        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // progress dots + counter
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    cards.indices.forEach { i ->
                        Box(
                            Modifier
                                .size(if (i == currentIndex) 10.dp else 7.dp)
                                .background(
                                    if (i <= currentIndex) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surfaceVariant,
                                    CircleShape
                                )
                        )
                    }
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    "Card ${currentIndex + 1} of ${cards.size}",
                    style      = MaterialTheme.typography.labelLarge,
                    color      = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // ── flip card ────────────────────────────────────────────────────
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .graphicsLayer {
                        rotationY      = rotation
                        cameraDistance = 14f * density
                    }
                    .clickable { isFlipped = !isFlipped }
            ) {
                if (rotation <= 90f) {
                    // front — question
                    Card(
                        Modifier.fillMaxSize(),
                        shape     = RoundedCornerShape(24.dp),
                        colors    = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(
                            Modifier.fillMaxSize().padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                Modifier.size(40.dp).background(
                                    MaterialTheme.colorScheme.primaryContainer, CircleShape
                                ),
                                Alignment.Center
                            ) {
                                Text(
                                    "Q",
                                    style      = MaterialTheme.typography.titleLarge,
                                    color      = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                            Spacer(Modifier.height(16.dp))
                            Text(
                                card.question,
                                style      = MaterialTheme.typography.titleMedium,
                                textAlign  = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.height(20.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.TouchApp, null, Modifier.size(16.dp),
                                    MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(Modifier.width(5.dp))
                                Text(
                                    "Tap to reveal answer",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    // back — answer
                    Card(
                        Modifier.fillMaxSize().graphicsLayer { rotationY = 180f },
                        shape     = RoundedCornerShape(24.dp),
                        colors    = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(Modifier.fillMaxSize()) {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF0A1628), Color(0xFF162D5A))
                                        ),
                                        RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                                    ),
                                Alignment.Center
                            ) {
                                Text(
                                    "ANSWER",
                                    style         = MaterialTheme.typography.labelLarge,
                                    color         = Color.White,
                                    fontWeight    = FontWeight.ExtraBold,
                                    letterSpacing = 3.sp
                                )
                            }
                            Column(
                                Modifier.fillMaxSize().padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    card.answer,
                                    style     = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    color     = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }

            // ── difficulty buttons (shown after flip) or nav ─────────────────
            if (isFlipped) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            hardCount++
                            isFlipped = false
                            if (currentIndex < cards.size - 1) currentIndex++ else isDone = true
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape    = RoundedCornerShape(12.dp),
                        colors   = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) { Text("Hard 😓", fontWeight = FontWeight.SemiBold) }

                    Button(
                        onClick = {
                            easyCount++
                            isFlipped = false
                            if (currentIndex < cards.size - 1) currentIndex++ else isDone = true
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape    = RoundedCornerShape(12.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A))
                    ) { Text("Easy 😊", fontWeight = FontWeight.SemiBold, color = Color.White) }
                }
            } else {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick  = { if (currentIndex > 0) { isFlipped = false; currentIndex-- } },
                        enabled  = currentIndex > 0
                    ) {
                        Icon(
                            Icons.Default.ArrowBack, "Previous",
                            tint = if (currentIndex > 0) MaterialTheme.colorScheme.primary
                                   else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    OutlinedButton(
                        onClick = { isFlipped = !isFlipped },
                        shape   = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Flip, null, Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Flip Card")
                    }
                    IconButton(
                        onClick = {
                            if (currentIndex < cards.size - 1) {
                                isFlipped = false; currentIndex++
                            } else isDone = true
                        }
                    ) {
                        Icon(Icons.Default.ArrowForward, "Next",
                            tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
