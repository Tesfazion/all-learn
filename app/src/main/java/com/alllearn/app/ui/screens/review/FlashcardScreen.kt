package com.alllearn.app.ui.screens.review

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alllearn.app.ui.components.AllLearnTopAppBar

data class FlashcardItem(
    val question: String,
    val answer: String
)

@Composable
fun FlashcardScreen(
    courseTitle: String,
    onBackClick: () -> Unit
) {
    val cards = remember {
        listOf(
            FlashcardItem(
                question = "What is the key advantage of structured weekly learning plans?",
                answer = "Adapts automatically to learner deadlines and free weekly hours while grounding lessons in verified sources."
            ),
            FlashcardItem(
                question = "How does the AI Helper Bot assist your study session?",
                answer = "Provides instant 24/7 context-aware explanations based on your current course topic and sources."
            ),
            FlashcardItem(
                question = "What happens if you fall behind target study hours (<70%)?",
                answer = "Weekly check-in offers two choices: Compress (trim lower priority lessons) or Extend (move target deadline back)."
            )
        )
    }

    var currentIndex by remember { mutableStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "cardFlipAnimation"
    )

    Scaffold(
        topBar = {
            AllLearnTopAppBar(
                title = "Flashcard Review - $courseTitle",
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Card ${currentIndex + 1} of ${cards.size}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            val card = cards[currentIndex]

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .graphicsLayer {
                        rotationY = rotation
                        cameraDistance = 12f * density
                    }
                    .clickable { isFlipped = !isFlipped },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isFlipped) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (rotation <= 90f) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "QUESTION",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = card.question,
                                style = MaterialTheme.typography.headlineMedium,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Flip, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Tap to flip card", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.graphicsLayer { rotationY = 180f }
                        ) {
                            Text(
                                text = "ANSWER",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = card.answer,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (currentIndex > 0) {
                            isFlipped = false
                            currentIndex--
                        }
                    },
                    enabled = currentIndex > 0
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Previous")
                }

                Button(
                    onClick = { isFlipped = !isFlipped },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Flip Card")
                }

                IconButton(
                    onClick = {
                        if (currentIndex < cards.size - 1) {
                            isFlipped = false
                            currentIndex++
                        }
                    },
                    enabled = currentIndex < cards.size - 1
                ) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Next")
                }
            }
        }
    }
}
