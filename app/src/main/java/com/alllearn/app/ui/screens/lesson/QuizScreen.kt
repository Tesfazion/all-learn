package com.alllearn.app.ui.screens.lesson

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alllearn.app.R
import com.alllearn.app.ui.components.AllLearnTopAppBar
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
    val selectedAnswers = remember { mutableStateMapOf<String, String>() }
    var isSubmitted by remember { mutableStateOf(false) }
    var calculatedScore by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            AllLearnTopAppBar(
                title = stringResource(R.string.quiz_title),
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            Surface(tonalElevation = 8.dp) {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    if (!isSubmitted) {
                        Button(
                            onClick = {
                                val quizList = lessonDetail?.quiz ?: emptyList()
                                var score = 0
                                quizList.forEach { q ->
                                    if (selectedAnswers[q.id] == q.correctOptionId) {
                                        score++
                                    }
                                }
                                calculatedScore = score
                                isSubmitted = true
                                viewModel.submitQuizScore(lessonId, score, quizList.size)
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            enabled = selectedAnswers.size == (lessonDetail?.quiz?.size ?: 0)
                        ) {
                            Text(stringResource(R.string.submit_quiz), style = MaterialTheme.typography.titleLarge)
                        }
                    } else {
                        Button(
                            onClick = onQuizComplete,
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Continue", style = MaterialTheme.typography.titleLarge)
                        }
                    }
                }
            }
        }
    ) { padding ->
        val quizList = lessonDetail?.quiz ?: emptyList()

        if (quizList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No quiz available for this lesson.")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (isSubmitted) {
                    val percentage = (calculatedScore * 100) / quizList.size
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(R.string.quiz_score_msg, calculatedScore, quizList.size, percentage),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                quizList.forEachIndexed { index, question ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "${index + 1}. ${question.question}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            question.options.forEach { option ->
                                val isSelected = selectedAnswers[question.id] == option.id
                                val isCorrect = option.id == question.correctOptionId

                                val bgColor = when {
                                    !isSubmitted -> if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                                    isCorrect -> MaterialTheme.colorScheme.primaryContainer
                                    isSelected && !isCorrect -> MaterialTheme.colorScheme.errorContainer
                                    else -> MaterialTheme.colorScheme.surface
                                }

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable(enabled = !isSubmitted) {
                                            selectedAnswers[question.id] = option.id
                                        },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = CardDefaults.cardColors(containerColor = bgColor)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = { if (!isSubmitted) selectedAnswers[question.id] = option.id }
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(option.text, style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }

                            if (isSubmitted) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Explanation: ${question.explanation}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
