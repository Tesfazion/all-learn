package com.alllearn.app.ui.screens.lesson

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val context      = LocalContext.current
    val lessonDetail by viewModel.lessonDetail.collectAsState()
    val isLoading    by viewModel.isLoading.collectAsState()

    var showReportDialog  by remember { mutableStateOf(false) }
    var reportSubmitted   by remember { mutableStateOf(false) }
    var resolvedLesson    by remember(courseId, lessonId) { mutableStateOf<LessonItem?>(null) }
    var resolvedTopic     by remember(courseId, topic) { mutableStateOf(topic) }

    // reading scroll state for progress bar
    val scrollState = rememberScrollState()
    val readProgress = if (scrollState.maxValue > 0)
        scrollState.value.toFloat() / scrollState.maxValue.toFloat() else 0f
    val animProg by animateFloatAsState(readProgress, tween(200), label = "rp")

    LaunchedEffect(courseId, lessonId) {
        dashboardViewModel.getCourseFlow(courseId).collect { course ->
            resolvedLesson = course?.outline?.weeks
                ?.flatMap { it.lessons }
                ?.firstOrNull { it.id == lessonId }
                ?: LessonItem(id = lessonId, weekNumber = 1, title = "Lesson", minutes = 45)
            resolvedTopic = course?.topic ?: topic
        }
    }

    val safeLesson = resolvedLesson
        ?: LessonItem(id = lessonId, weekNumber = 1, title = "Lesson", minutes = 45)

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
                        Icon(Icons.Default.SmartToy, "AI Helper",
                            tint = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        },
        bottomBar = {
            // reading progress + action buttons
            Column {
                LinearProgressIndicator(
                    progress    = { animProg },
                    modifier    = Modifier.fillMaxWidth().height(3.dp),
                    color       = MaterialTheme.colorScheme.primary,
                    trackColor  = MaterialTheme.colorScheme.outlineVariant
                )
                Surface(tonalElevation = 8.dp) {
                    Row(
                        Modifier.fillMaxWidth().padding(14.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick  = { showReportDialog = true },
                            modifier = Modifier.weight(1f).height(50.dp),
                            shape    = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.ReportProblem, null, Modifier.size(18.dp))
                            Spacer(Modifier.width(5.dp))
                            Text("Report")
                        }
                        Button(
                            onClick  = {
                                viewModel.markAsDone(safeLesson.id)
                                onOpenQuiz(safeLesson.id)
                            },
                            modifier = Modifier.weight(2f).height(50.dp),
                            shape    = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Quiz, null, Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Take Quiz", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        if (isLoading || lessonDetail == null) {
            Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(12.dp))
                    Text("Loading lesson…", style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            return@Scaffold
        }

        val detail = lessonDetail!!

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
        ) {

            // ── hero section ─────────────────────────────────────────────────
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF0A1628), Color(0xFF0F2144))
                        )
                    )
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // badges row
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        LessonBadge(Icons.Default.Timer, "${detail.minutes} min")
                        LessonBadge(Icons.Default.Verified, "Verified ${detail.lastVerified}")
                        if (detail.needsReview) {
                            LessonBadge(Icons.Default.Warning, "Needs Review",
                                badgeColor = Color(0xFFF97316))
                        }
                    }
                    Text(
                        detail.title,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            brush = Brush.linearGradient(listOf(Color.White, Color(0xFFB0C8FF)))
                        ),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                // ── learning goals ───────────────────────────────────────────
                AnimatedVisibility(detail.learningGoals.isNotEmpty(),
                    enter = fadeIn() + slideInVertically()) {
                    Card(
                        shape  = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.TrackChanges, null, Modifier.size(20.dp),
                                    MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(8.dp))
                                Text("Learning Goals", style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer)
                            }
                            detail.learningGoals.forEachIndexed { i, goal ->
                                Row(verticalAlignment = Alignment.Top) {
                                    Box(
                                        Modifier.size(22.dp)
                                            .background(MaterialTheme.colorScheme.primary, CircleShape),
                                        Alignment.Center
                                    ) {
                                        Text("${i+1}", style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(Modifier.width(10.dp))
                                    Text(goal, style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                // ── content ──────────────────────────────────────────────────
                Text("Lesson Content", style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)

                // Render markdown-like sections by splitting on "##"
                val sections = detail.contentMarkdown
                    .split(Regex("(?=##\\s)"))
                    .filter { it.isNotBlank() }

                sections.forEach { section ->
                    val lines = section.trim().lines()
                    val heading = lines.firstOrNull()?.removePrefix("##")?.removePrefix("#")?.trim()
                        ?: ""
                    val body = lines.drop(1).joinToString("\n").trim()

                    if (heading.isNotEmpty()) {
                        ContentSection(heading = heading, body = body)
                    } else {
                        // No heading — just render as body text
                        Card(
                            shape  = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text(section.trim(),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(16.dp),
                                lineHeight = 26.sp)
                        }
                    }
                }

                // ── sources ──────────────────────────────────────────────────
                Text(stringResource(R.string.sources_title),
                    style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                detail.sources.forEach { source ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable {
                            try {
                                context.startActivity(
                                    Intent(Intent.ACTION_VIEW, Uri.parse(source.url))
                                )
                            } catch (_: Exception) {}
                        },
                        shape    = RoundedCornerShape(12.dp),
                        colors   = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            Modifier.padding(14.dp),
                            verticalAlignment   = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    Modifier.size(36.dp)
                                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                                    Alignment.Center
                                ) {
                                    Icon(Icons.Default.MenuBook, null, Modifier.size(18.dp),
                                        MaterialTheme.colorScheme.primary)
                                }
                                Spacer(Modifier.width(10.dp))
                                Column {
                                    Text(source.title, style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary)
                                    Text(source.type.uppercase(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Icon(Icons.Default.OpenInNew, null, Modifier.size(16.dp),
                                MaterialTheme.colorScheme.primary)
                        }
                    }
                }

                // report confirmation
                if (reportSubmitted) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, null, Modifier.size(18.dp),
                                MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.width(8.dp))
                            Text(stringResource(R.string.report_submitted),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))
            }
        }

        if (showReportDialog) {
            ReportMistakeDialog(
                onDismiss = { showReportDialog = false },
                onSubmit  = { showReportDialog = false; reportSubmitted = true }
            )
        }
    }
}

@Composable
private fun LessonBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    badgeColor: Color = Color(0xFF38BDF8)
) {
    Row(
        Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(badgeColor.copy(0.18f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, Modifier.size(12.dp), badgeColor)
        Spacer(Modifier.width(4.dp))
        Text(text, style = MaterialTheme.typography.labelSmall, color = badgeColor)
    }
}

@Composable
private fun ContentSection(heading: String, body: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.width(4.dp).height(20.dp)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(2.dp)))
            Spacer(Modifier.width(8.dp))
            Text(heading, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        }
        if (body.isNotBlank()) {
            Card(
                shape  = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(body, style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(14.dp), lineHeight = 22.sp)
            }
        }
    }
}
