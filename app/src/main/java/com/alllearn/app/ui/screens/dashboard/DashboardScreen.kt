package com.alllearn.app.ui.screens.dashboard

import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alllearn.app.R
import com.alllearn.app.data.model.Course
import com.alllearn.app.ui.components.*
import com.alllearn.app.ui.theme.*
import com.alllearn.app.ui.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    userId: String,
    onStartNewCourse: () -> Unit,
    onOpenCourse: (String) -> Unit,
    onOpenBot: (String) -> Unit,
    onNavigate: (String) -> Unit
) {
    val activeCourseId by viewModel.activeCourseId.collectAsState()
    val streakCount    by viewModel.streakCount.collectAsState()
    var activeCourse   by remember { mutableStateOf<Course?>(null) }

    LaunchedEffect(activeCourseId) {
        if (activeCourseId != null) {
            viewModel.getCourseFlow(activeCourseId!!).collect { activeCourse = it }
        } else {
            activeCourse = null
        }
    }

    // daily motivational messages (cycle by streak)
    val motivations = listOf(
        "Every lesson brings you closer to mastery. Keep going!",
        "Consistency beats talent. You're building something real.",
        "Learning is an investment that always pays off.",
        "One more lesson today keeps regret away.",
        "Your future self is cheering you on. Don't stop now!"
    )
    val motivationText = motivations[streakCount % motivations.size]

    Scaffold(
        topBar = {
            DashboardTopBar(streakCount = streakCount)
        },
        bottomBar = {
            AllLearnBottomBar(
                currentRoute = "home",
                onNavigate   = onNavigate,
                onOpenBot    = { onOpenBot(activeCourseId ?: "none") }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            // ── hero / active course card ────────────────────────────────────
            if (activeCourse != null) {
                ActiveCourseHeroCard(
                    course    = activeCourse!!,
                    onOpenCourse = { onOpenCourse(activeCourse!!.id) }
                )
            } else {
                NoCourseHeroCard(onStartNewCourse = onStartNewCourse)
            }

            Spacer(Modifier.height(20.dp))

            // ── motivational banner ──────────────────────────────────────────
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape  = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        motivationText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── quick stats row ──────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    icon     = Icons.Default.LocalFireDepartment,
                    iconTint = Color(0xFFF97316),
                    value    = "$streakCount",
                    label    = "Day Streak",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    icon     = Icons.Default.AutoStories,
                    iconTint = MaterialTheme.colorScheme.primary,
                    value    = if (activeCourse != null) "${activeCourse!!.outline.weeks.size}" else "—",
                    label    = "Total Weeks",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    icon     = Icons.Default.Schedule,
                    iconTint = Color(0xFF059669),
                    value    = if (activeCourse != null) "${activeCourse!!.hoursPerWeek}h" else "—",
                    label    = "Per Week",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(20.dp))

            // ── quick action chips ───────────────────────────────────────────
            Text(
                "Quick Actions",
                style    = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionChip(
                    icon  = Icons.Default.Add,
                    label = "New Course",
                    onClick = onStartNewCourse,
                    modifier = Modifier.weight(1f)
                )
                QuickActionChip(
                    icon  = Icons.Default.SmartToy,
                    label = "AI Helper",
                    onClick = { onOpenBot(activeCourseId ?: "none") },
                    modifier = Modifier.weight(1f)
                )
                QuickActionChip(
                    icon  = Icons.Default.BarChart,
                    label = "Progress",
                    onClick = { onNavigate("progress") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Top bar
// ─────────────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardTopBar(streakCount: Int) {
    TopAppBar(
        title = {
            Text(
                "All Learn",
                style      = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color      = MaterialTheme.colorScheme.primary
            )
        },
        actions = {
            Row(
                Modifier.padding(end = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    Icons.Default.LocalFireDepartment,
                    contentDescription = null,
                    tint = Color(0xFFF97316),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text  = "$streakCount",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFFF97316),
                    fontWeight = FontWeight.ExtraBold
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Active course hero card with animated progress ring
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun ActiveCourseHeroCard(course: Course, onOpenCourse: () -> Unit) {
    // mock progress: completed lessons / total lessons
    val totalLessons     = course.outline.weeks.sumOf { it.lessons.size }
    val completedLessons = 0  // real count wired via viewModel later
    val progressFraction = if (totalLessons > 0) completedLessons.toFloat() / totalLessons else 0f
    val pct              = (progressFraction * 100).toInt()

    val primary = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF0A1628), Color(0xFF0F2144), Color(0xFF162D5A))
                )
            )
            .clickable { onOpenCourse() }
    ) {
        // subtle blob
        Box(
            Modifier.size(180.dp).align(Alignment.TopEnd).offset(40.dp, (-40).dp)
                .background(primary.copy(0.14f), CircleShape).blur(50.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // text section
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "CURRENT COURSE",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF93C5FD),
                    letterSpacing = 2.sp
                )
                Text(
                    course.outline.title,
                    style    = MaterialTheme.typography.headlineSmall.copy(
                        brush = Brush.linearGradient(listOf(Color.White, Color(0xFFB0C8FF)))
                    ),
                    fontWeight   = FontWeight.Bold,
                    maxLines     = 2,
                    overflow     = TextOverflow.Ellipsis
                )
                Text(
                    "${course.outline.totalWeeks} weeks • ${course.hoursPerWeek} hrs/week",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF93C5FD)
                )
                Spacer(Modifier.height(4.dp))
                // mini continue button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(listOf(Color(0xFF38BDF8), Color(0xFFA78BFA)))
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PlayArrow, null, Modifier.size(16.dp), tint = Color.White)
                        Spacer(Modifier.width(4.dp))
                        Text("Continue", style = MaterialTheme.typography.labelLarge,
                            color = Color.White)
                    }
                }
            }

            Spacer(Modifier.width(16.dp))

            // progress ring
            CircularProgressRing(
                progress     = progressFraction,
                size         = 90,
                strokeWidth  = 9f,
                primaryColor = Color(0xFF38BDF8),
                trackColor   = Color.White.copy(0.15f)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "$pct%",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        "done",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF93C5FD)
                    )
                }
            }
        }

        // bottom deadline strip
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.Black.copy(0.25f))
                .padding(horizontal = 20.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, null, Modifier.size(13.dp), Color(0xFF93C5FD))
                Spacer(Modifier.width(5.dp))
                Text(
                    "Target: ${course.targetEndDate}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF93C5FD)
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// No-course hero card
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun NoCourseHeroCard(onStartNewCourse: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF0A1628), Color(0xFF0F2144), Color(0xFF162D5A))
                )
            )
    ) {
        Box(Modifier.size(200.dp).align(Alignment.TopEnd).offset(60.dp, (-60).dp)
            .background(GradientEnd.copy(0.14f), CircleShape).blur(55.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.School, null, Modifier.size(42.dp), Color(0xFF93C5FD))
            Spacer(Modifier.height(12.dp))
            Text(
                "Start your first course",
                style = MaterialTheme.typography.headlineSmall.copy(
                    brush = Brush.linearGradient(listOf(Color.White, Color(0xFFB0C8FF)))
                ),
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Build a learning plan that fits your deadline & schedule",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF93C5FD)
            )
            Spacer(Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF38BDF8), Color(0xFFA78BFA)))
                    )
                    .clickable { onStartNewCourse() }
                    .padding(horizontal = 24.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Add, null, Modifier.size(18.dp), Color.White)
                    Spacer(Modifier.width(6.dp))
                    Text("Create Course", style = MaterialTheme.typography.labelLarge, color = Color.White)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Quick action chip
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun QuickActionChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape    = RoundedCornerShape(14.dp),
        colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = label,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp))
            }
            Text(label, style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface)
        }
    }
}
