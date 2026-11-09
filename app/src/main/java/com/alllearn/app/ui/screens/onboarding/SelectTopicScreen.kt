package com.alllearn.app.ui.screens.onboarding

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
import com.alllearn.app.data.remote.AiCurriculumEngine
import com.alllearn.app.ui.components.AllLearnTopAppBar

@Composable
fun SelectTopicScreen(
    area: String,
    onTopicSelected: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedTopic by remember { mutableStateOf<String?>(null) }
    var customTopicText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val otherSpecifyText = stringResource(R.string.other_specify)
    val defaultSubTopics = when (area.lowercase()) {
        "programming" -> listOf("C++", "Python", "Java", "JavaScript", "Kotlin", "Web Development", "Data Structures & Algorithms")
        "psychology" -> listOf("Cognitive Psychology", "Behavioral Psychology", "Developmental Psychology", "Social Psychology")
        "business" -> listOf("Entrepreneurship", "Digital Marketing", "Financial Management", "Project Management")
        "languages" -> listOf("English Fluency", "Amharic Basics", "Spanish", "French", "Mandarin")
        "mathematics" -> listOf("Algebra & Calculus", "Statistics & Probability", "Linear Algebra", "Discrete Math")
        "design" -> listOf("UI/UX Design", "Graphic Design", "3D Animation", "Design Systems")
        "health & fitness" -> listOf("Nutrition & Dietetics", "Strength Training", "Mental Health & Mindfulness", "Kinesiology")
        else -> listOf("Fundamentals of $area", "Practical $area Applications", "Advanced $area Concepts")
    } + listOf(otherSpecifyText)

    val aiEngine = remember { AiCurriculumEngine() }

    Scaffold(
        topBar = {
            AllLearnTopAppBar(
                title = stringResource(R.string.select_topic_title),
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            Surface(tonalElevation = 4.dp) {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Button(
                        onClick = {
                            val finalTopic = if (selectedTopic == otherSpecifyText) {
                                customTopicText.trim()
                            } else {
                                selectedTopic ?: ""
                            }

                            if (finalTopic.length !in 2..60) {
                                errorMessage = "Topic must be between 2 and 60 characters."
                                return@Button
                            }

                            if (!aiEngine.isTopicSafe(finalTopic)) {
                                errorMessage = "Please enter a valid topic."
                                return@Button
                            }

                            onTopicSelected(finalTopic)
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = selectedTopic != null && (selectedTopic != otherSpecifyText || customTopicText.trim().length >= 2)
                    ) {
                        Text(stringResource(R.string.continue_btn), style = MaterialTheme.typography.titleLarge)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Area: $area",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            defaultSubTopics.forEach { topic ->
                val isSelected = selectedTopic == topic
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedTopic = topic
                            errorMessage = null
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { selectedTopic = topic }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = topic,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            if (selectedTopic == otherSpecifyText) {
                OutlinedTextField(
                    value = customTopicText,
                    onValueChange = { customTopicText = it },
                    label = { Text("Specify your topic") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
