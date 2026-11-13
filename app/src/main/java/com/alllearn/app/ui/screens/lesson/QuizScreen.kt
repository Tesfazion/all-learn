package com.alllearn.app.ui.screens.lesson

import androidx.compose.animation.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alllearn.app.R
import com.alllearn.app.ui.components.AllLearnTopAppBar
import com.alllearn.app.ui.components.CircularProgressRing
import com.alllearn.app.ui.viewmodel.LessonViewModel

@Composable
fun QuizScreen(
    courseId: String,
    lessonId: String,
    viewModel: LessonViewModel,
    onQuizComplete: () -> Unit,
    onBackClick: () -> Unit
) {
    val lessonDetail by viewModel.lessonDetail.collectAsState()
    val quizList      = lessonDetail?.quiz ?: emptyList()

    var currentIndex   by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var isAnswered     by remember { mutableStateOf(false) }
    var score          by remember { mutableStateOf(0) }
    var isComplete     by remember { mutableStateOf(false) }

    val progressFraction = if (quizList.isEmpty()) 0f
                           else currentIndex.toFloat() / quizList.size.toFloat()
    val animProg by animateFloatAsState(progressFraction, tween(400), label = "qp")
    val scorePct = if (quizList.isNotEmpty()) (score * 100) / quizList.size else 0

    Scaffold(
        topBar = {
            AllLearnTopAppBar(
                title       = stringResource(R.string.quiz_title),
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        if (quizList.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                Text("No quiz available for this lesson.")
            }
            return@Scaffold
        }

        // ── completion screen ────────────────────────────────────────────────
        if (isComplete) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val ringColor = when {
                    scorePct >= 80 -> Color(0xFF16A34A)
                    scorePct >= 50 -> Color(0xFFD97706)
                    else           -> MaterialTheme.colorScheme.error
                }
                CircularProgressRing(
                    progress     = scorePct / 100f,
                    size         = 130,
                    strokeWidth  = 12f,
                    primaryColor = ringColor,
                    trackColor   = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "$scorePct%",
                            style      = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color      = ringColor
                        )
                        Text(
                            "Score",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))
                Text(
                    when {
                        scorePct >= 80 -> "🎉"
                        scorePct >= 50 -> "👍"
                        else           -> "💪"
                    },
                    fontSize = 48.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    when {
                        scorePct >= 80 -> "Excellent work!"
                        scorePct >= 50 -> "Good effort!"
                        else           -> "Keep practicing!"
                    },
                    style      = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    stringResource(R.string.quiz_score_msg, score, quizList.size, scorePct),
                    style     = MaterialTheme.typography.bodyLarge,
                    color     = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(32.dp))
                Button(
                    onClick  = onQuizComplete,
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape    = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        "Continue Learning",
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp)
                    )
                }
            }
            return@Scaffold
        }

        // ── active question ──────────────────────────────────────────────────
        val question = quizList[currentIndex]

        Column(Modifier.fillMaxSize().padding(padding)) {

            // progress header
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        "Question ${currentIndex + 1} of ${quizList.size}",
                        style      = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "Score: $score",
                            style      = MaterialTheme.typography.labelMedium,
                            color      = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress   = { animProg },
                    modifier   = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                    color      = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // question card
                Card(
                    Modifier.fillMaxWidth(),
                    shape  = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                Modifier.size(32.dp).background(
                                    MaterialTheme.colorScheme.primary, CircleShape
                                ),
                                Alignment.Center
                            ) {
                                Text(
                                    "${currentIndex + 1}",
                                    style      = MaterialTheme.typography.labelLarge,
                                    color      = MaterialTheme.colorScheme.onPrimary,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                            Spacer(Modifier.width(10.dp))
                            Text(
                                "Question",
                                style      = MaterialTheme.typography.labelLarge,
                                color      = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Text(
                            question.question,
                            style      = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color      = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                // answer options
                question.options.forEach { option ->
                    val isSelected = selectedAnswer == option.id
                    val isCorrect  = option.id == question.correctOptionId

                    val bgColor = when {
                        !isAnswered && isSelected  -> MaterialTheme.colorScheme.primaryContainer
                        !isAnswered                -> MaterialTheme.colorScheme.surface
                        isCorrect                  -> Color(0xFF16A34A).copy(0.15f)
                        isSelected && !isCorrect   -> MaterialTheme.colorScheme.errorContainer
                        else                       -> MaterialTheme.colorScheme.surface
                    }
                    val borderColor = when {
                        !isAnswered && isSelected  -> MaterialTheme.colorScheme.primary
                        isAnswered && isCorrect    -> Color(0xFF16A34A)
                        isAnswered && isSelected && !isCorrect -> MaterialTheme.colorScheme.error
                        else                       -> MaterialTheme.colorScheme.outlineVariant
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth().clickable(enabled = !isAnswered) {
                            selectedAnswer = option.id
                        },
                        shape    = RoundedCornerShape(14.dp),
                        colors   = CardDefaults.cardColors(containerColor = bgColor),
                        border   = CardDefaults.outlinedCardBorder()
                            .copy(brush = SolidColor(borderColor))
                    ) {
                        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = isSelected,
                                onClick  = { if (!isAnswered) selectedAnswer = option.id }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                option.text,
                                style      = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                modifier   = Modifier.weight(1f)
                            )
                            if (isAnswered && isCorrect) {
                                Icon(Icons.Default.CheckCircle, null, Modifier.size(20.dp),
                                    Color(0xFF16A34A))
                            }
                            if (isAnswered && isSelected && !isCorrect) {
                                Icon(Icons.Default.Cancel, null, Modifier.size(20.dp),
                                    MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }

                // explanation card
                AnimatedVisibility(
                    visible = isAnswered,
                    enter   = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
                ) {
                    Card(
                        Modifier.fillMaxWidth(),
                        shape  = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(Modifier.padding(14.dp)) {
                            Icon(Icons.Default.Info, null, Modifier.size(18.dp),
                                MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text(
                                    stringResource(R.string.explanation_title),
                                    style      = MaterialTheme.typography.labelLarge,
                                    color      = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    question.explanation,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.weight(1f))

                // action button
                Button(
                    onClick = {
                        if (!isAnswered) {
                            if (selectedAnswer == question.correctOptionId) score++
                            isAnswered = true
                        } else {
                            if (currentIndex < quizList.size - 1) {
                                currentIndex++
                                selectedAnswer = null
                                isAnswered     = false
                            } else {
                                viewModel.submitQuizScore(lessonId, score, quizList.size)
                                isComplete = true
                            }
                        }
                    },
                    enabled  = selectedAnswer != null || isAnswered,
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape    = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        when {
                            !isAnswered                     -> "Confirm Answer"
                            currentIndex < quizList.size - 1 -> "Next Question"
                            else                            -> "See Results"
                        },
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp)
                    )
                }
            }
        }
    }
}
