package com.alllearn.app.ui.screens.lesson

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alllearn.app.R
import com.alllearn.app.data.model.Course
import com.alllearn.app.data.model.LessonItem
import com.alllearn.app.ui.components.AllLearnTopAppBar
import com.alllearn.app.ui.viewmodel.DashboardViewModel

@Composable
fun WeekViewScreen(
    courseId: String,
    viewModel: DashboardViewModel,
    onOpenLesson: (LessonItem) -> Unit,
    onOpenQuiz: (String) -> Unit,
    onOpenFlashcards: () -> Unit,
    onOpenCertificate: () -> Unit,
    onBackClick: () -> Unit
) {
    var course by remember { mutableStateOf<Course?>(null) }
    LaunchedEffect(courseId) { viewModel.getCourseFlow(courseId).collect { course = it } }

    Scaffold(
        topBar = {
            AllLearnTopAppBar(title = course?.topic ?: "Course Plan", onBackClick = onBackClick)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        if (course == null) {
            Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        val c = course!!
        val isNonProgramming = !c.area.equals("Programming", ignoreCase = true)
        val totalLessons = c.outline.weeks.sumOf { it.lessons.size }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // ── course header banner ─────────────────────────────────────────
            item {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF0A1628), Color(0xFF0F2144), Color(0xFF162D5A))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            c.outline.title,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                brush = Brush.linearGradient(listOf(Color.White, Color(0xFFB0C8FF)))
                            ),
                            fontWeight = FontWeight.Bold
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                            InfoPill(Icons.Default.DateRange, "${c.outline.totalWeeks} Weeks")
                            InfoPill(Icons.Default.Schedule, "${c.hoursPerWeek} hrs/wk")
                            InfoPill(Icons.Default.Book, "$totalLessons Lessons")
                        }
                        Spacer(Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress    = { 0f },
                            modifier    = Modifier.fillMaxWidth().height(5.dp).clip(CircleShape),
                            color       = Color(0xFF38BDF8),
                            trackColor  = Color.White.copy(0.15f)
                        )
                        Text(
                            "0 of $totalLessons lessons completed",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF93C5FD)
                        )
                    }
                }
            }

            // ── non-programming notice ───────────────────────────────────────
            if (isNonProgramming) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        colors   = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, null, Modifier.size(18.dp),
                                MaterialTheme.colorScheme.onTertiaryContainer)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                stringResource(R.string.non_programming_notice),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }
            }

            // ── phases + weeks ───────────────────────────────────────────────
            c.outline.phases.forEach { phase ->
                item {
                    PhaseHeader(
                        phaseNumber = phase.phase,
                        title       = phase.title,
                        milestone   = phase.milestoneProject
                    )
                }

                val phaseWeeks = c.outline.weeks.filter { it.week in phase.weeks }
                phaseWeeks.forEach { week ->
                    item {
                        WeekHeader(
                            weekNum        = week.week,
                            title          = week.title,
                            plannedMinutes = week.plannedMinutes
                        )
                    }
                    items(week.lessons) { lesson ->
                        LessonRow(lesson = lesson, onOpen = { onOpenLesson(lesson) })
                    }
                    if (week.hasQuiz) {
                        item {
                            QuizChip(
                                weekNum = week.week,
                                onClick = { onOpenQuiz(week.lessons.firstOrNull()?.id ?: "") }
                            )
                        }
                    }
                }
            }

            // ── completion actions ───────────────────────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Review & Complete",
                        style      = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(
                            onClick  = onOpenFlashcards,
                            modifier = Modifier.weight(1f).height(50.dp),
                            shape    = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Flip, null, Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(stringResource(R.string.flashcards))
                        }
                        Button(
                            onClick  = onOpenCertificate,
                            modifier = Modifier.weight(1f).height(50.dp),
                            shape    = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.WorkspacePremium, null, Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Certificate")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoPill(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, Modifier.size(13.dp), Color(0xFF93C5FD))
        Spacer(Modifier.width(3.dp))
        Text(text, style = MaterialTheme.typography.labelSmall, color = Color(0xFF93C5FD))
    }
}

@Composable
private fun PhaseHeader(phaseNumber: Int, title: String, milestone: String) {
    Card(
        modifier  = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(36.dp).background(MaterialTheme.colorScheme.primary, CircleShape),
                Alignment.Center
            ) {
                Text(
                    "$phaseNumber",
                    style      = MaterialTheme.typography.labelLarge,
                    color      = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    title,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color      = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Flag, null, Modifier.size(13.dp),
                        MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(4.dp))
                    Text(milestone, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
        }
    }
}

@Composable
private fun WeekHeader(weekNum: Int, title: String, plannedMinutes: Int) {
    Row(
        Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(26.dp).background(
                MaterialTheme.colorScheme.primary.copy(0.15f), CircleShape
            ),
            Alignment.Center
        ) {
            Text(
                "$weekNum",
                style      = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(Modifier.width(10.dp))
        Column {
            Text(
                "Week $weekNum: $title",
                style      = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "${plannedMinutes / 60}h ${plannedMinutes % 60}m planned",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun LessonRow(lesson: LessonItem, onOpen: () -> Unit) {
    val isDone = lesson.status == "done"
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable(onClick = onOpen),
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(
            containerColor = if (isDone) MaterialTheme.colorScheme.primaryContainer
                             else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(34.dp).background(
                    if (isDone) MaterialTheme.colorScheme.primary.copy(0.15f)
                    else MaterialTheme.colorScheme.surfaceVariant,
                    CircleShape
                ),
                Alignment.Center
            ) {
                Icon(
                    imageVector = if (isDone) Icons.Default.CheckCircle
                                  else Icons.Default.PlayCircle,
                    contentDescription = null,
                    tint = if (isDone) MaterialTheme.colorScheme.primary
                           else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    lesson.title,
                    style      = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines   = 2
                )
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Timer, null, Modifier.size(12.dp),
                        MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.width(3.dp))
                    Text("${lesson.minutes} min", style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    if (isDone) {
                        Spacer(Modifier.width(10.dp))
                        Text("✓ Completed", style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold)
                    }
                }
            }
            Icon(Icons.Default.ChevronRight, null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun QuizChip(weekNum: Int, onClick: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Quiz, null, Modifier.size(18.dp),
                MaterialTheme.colorScheme.secondary)
            Spacer(Modifier.width(8.dp))
            Text(
                "Week $weekNum Quiz",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.ArrowForward, null, Modifier.size(16.dp),
                MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
