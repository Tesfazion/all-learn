package com.alllearn.app.ui.screens.onboarding

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
import com.alllearn.app.data.model.Course
import com.alllearn.app.data.model.SkillLevel
import com.alllearn.app.ui.components.AllLearnTopAppBar
import com.alllearn.app.ui.components.LoadingSkeletonCard
import com.alllearn.app.ui.viewmodel.OnboardingViewModel
import com.alllearn.app.ui.viewmodel.OutlineState

@Composable
fun OutlinePreviewScreen(
    viewModel: OnboardingViewModel,
    userId: String,
    area: String,
    topic: String,
    levelKey: String,
    levelNote: String,
    deadlineWeeks: Int,
    hoursPerWeek: Int,
    onApproveCourse: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val outlineState by viewModel.outlineState.collectAsState()
    var showRegenerateDialog by remember { mutableStateOf(false) }
    var changePrompt by remember { mutableStateOf("") }

    val skillLevel = remember {
        try { SkillLevel.entries.first { it.key == levelKey } } catch (e: Exception) { SkillLevel.BEGINNER }
    }

    LaunchedEffect(Unit) {
        if (outlineState is OutlineState.Idle) {
            viewModel.generateOutline(
                userId = userId,
                area = area,
                topic = topic,
                level = skillLevel,
                levelNote = levelNote,
                deadlineWeeks = deadlineWeeks,
                hoursPerWeek = hoursPerWeek
            )
        }
    }

    Scaffold(
        topBar = {
            AllLearnTopAppBar(
                title = stringResource(R.string.outline_preview_title),
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = outlineState) {
                is OutlineState.Loading, OutlineState.Idle -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        LoadingSkeletonCard()
                    }
                }
                is OutlineState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            viewModel.generateOutline(
                                userId = userId,
                                area = area,
                                topic = topic,
                                level = skillLevel,
                                levelNote = levelNote,
                                deadlineWeeks = deadlineWeeks,
                                hoursPerWeek = hoursPerWeek
                            )
                        }) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }
                is OutlineState.Success -> {
                    val course = state.course
                    val outline = course.outline

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = outline.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${outline.totalWeeks} Weeks • ${outline.hoursPerWeek} Hours/Week",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Phases
                        Text(
                            text = "Learning Phases",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        outline.phases.forEach { phase ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = stringResource(R.string.phase_n, phase.phase, phase.title),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = stringResource(R.string.goal_label, phase.goal),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = stringResource(R.string.milestone_project_label, phase.milestoneProject),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Weeks List
                        Text(
                            text = "Weekly Curriculum",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        outline.weeks.forEach { week ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Week ${week.week}: ${week.title}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    week.lessons.forEach { lesson ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("• ${lesson.title}", style = MaterialTheme.typography.bodyMedium)
                                            Text("${lesson.minutes} mins", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = { showRegenerateDialog = true },
                                modifier = Modifier.weight(1f).height(50.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(stringResource(R.string.regenerate_outline))
                            }

                            Button(
                                onClick = { onApproveCourse(course.id) },
                                modifier = Modifier.weight(1f).height(50.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(stringResource(R.string.approve_and_start))
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    if (showRegenerateDialog) {
                        AlertDialog(
                            onDismissRequest = { showRegenerateDialog = false },
                            title = { Text(stringResource(R.string.regenerate_outline)) },
                            text = {
                                Column {
                                    Text(
                                        text = stringResource(R.string.regenerate_prompt_label),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    OutlinedTextField(
                                        value = changePrompt,
                                        onValueChange = { changePrompt = it },
                                        placeholder = { Text(stringResource(R.string.regenerate_prompt_hint)) },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    if (course.regenerationsUsed >= 3) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = stringResource(R.string.regen_limit_reached),
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        showRegenerateDialog = false
                                        viewModel.regenerateOutline(course, changePrompt)
                                    },
                                    enabled = course.regenerationsUsed < 3
                                ) {
                                    Text("Regenerate")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showRegenerateDialog = false }) {
                                    Text(stringResource(R.string.cancel))
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
