package com.alllearn.app.ui.screens.lesson

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alllearn.app.R
import com.alllearn.app.data.model.LessonItem
import com.alllearn.app.ui.components.AllLearnTopAppBar
import com.alllearn.app.ui.components.ReportMistakeDialog
import com.alllearn.app.ui.viewmodel.DashboardViewModel
import com.alllearn.app.ui.viewmodel.LessonViewModel

@Composable
fun LessonDetailScreen(
    courseId: String,
    lessonId: String,
    topic: String,
    viewModel: LessonViewModel,
    dashboardViewModel: DashboardViewModel,
    onOpenQuiz: (String) -> Unit,
    onOpenBot: (String, String) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val lessonDetail by viewModel.lessonDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var showReportDialog by remember { mutableStateOf(false) }
    var reportSubmittedMsg by remember { mutableStateOf<String?>(null) }

    var resolvedLesson by remember(courseId, lessonId) {
        mutableStateOf<LessonItem?>(null)
    }
    var resolvedTopic by remember(courseId, topic) { mutableStateOf(topic) }

    LaunchedEffect(courseId, lessonId) {
        dashboardViewModel.getCourseFlow(courseId).collect { course ->
            val match = course?.outline?.weeks
                ?.flatMap { it.lessons }
                ?.firstOrNull { it.id == lessonId }

            resolvedLesson = match ?: LessonItem(
                id = lessonId,
                weekNumber = 1,
                title = "Lesson Detail",
                minutes = 45
            )
            resolvedTopic = course?.topic ?: topic
        }
    }

    val safeLesson = resolvedLesson ?: LessonItem(
        id = lessonId,
        weekNumber = 1,
        title = "Lesson Detail",
        minutes = 45
    )

    LaunchedEffect(safeLesson.id, resolvedTopic) {
        viewModel.loadLesson(courseId, safeLesson, resolvedTopic)
    }

    Scaffold(
        topBar = {
            AllLearnTopAppBar(
                title = safeLesson.title,
                onBackClick = onBackClick,
                actions = {
                    IconButton(onClick = { onOpenBot(courseId, safeLesson.id) }) {
                        Icon(Icons.Default.SmartToy, contentDescription = "Ask Helper Bot", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 8.dp) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { showReportDialog = true },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.ReportProblem, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Report")
                    }

                    Button(
                        onClick = {
                            viewModel.markAsDone(safeLesson.id)
                            onOpenQuiz(safeLesson.id)
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Start Quiz")
                    }
                }
            }
        }
    ) { padding ->
        if (isLoading || lessonDetail == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val detail = lessonDetail!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = detail.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Estimated Time: ${detail.minutes} mins • Last Verified: ${detail.lastVerified}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Divider()

                Text(
                    text = "Learning Goals",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                detail.learningGoals.forEach { goal ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(goal, style = MaterialTheme.typography.bodyLarge)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = detail.contentMarkdown,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Sources Section
                Text(
                    text = stringResource(R.string.sources_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                detail.sources.forEach { source ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(source.url))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    // Handle missing browser
                                }
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = source.title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Icon(Icons.Default.OpenInNew, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }

                if (reportSubmittedMsg != null) {
                    Text(
                        text = reportSubmittedMsg!!,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (showReportDialog) {
                ReportMistakeDialog(
                    onDismiss = { showReportDialog = false },
                    onSubmit = { msg ->
                        showReportDialog = false
                        reportSubmittedMsg = "Thank you! Your report has been recorded."
                    }
                )
            }
        }
    }
}
