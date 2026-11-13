package com.alllearn.app.ui.screens.progress

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alllearn.app.R
import com.alllearn.app.ui.components.*
import com.alllearn.app.ui.theme.*
import com.alllearn.app.ui.viewmodel.ProgressViewModel

@Composable
fun ProgressScreen(
    viewModel: ProgressViewModel,
    onNavigate: (String) -> Unit,
    onOpenBot: () -> Unit
) {
    val streakCount by viewModel.streakCount.collectAsState()

    // Animated counters (cosmetic — real data wired to Room later)
    val transition = rememberInfiniteTransition(label = "prog")
    val ringAnim by animateFloatAsState(0.33f, tween(1200, easing = FastOutSlowInEasing), label = "ra")

    // Week heatmap data (7 days, 0=no study, 1=partial, 2=full)
    val weekActivity = listOf(2, 1, 2, 0, 2, 2, 1)
    val days         = listOf("M", "T", "W", "T", "F", "S", "S")

    Scaffold(
        topBar = {
            AllLearnTopAppBar(title = stringResource(R.string.nav_progress))
        },
        bottomBar = {
            AllLearnBottomBar(currentRoute = "progress",
                onNavigate = onNavigate, onOpenBot = onOpenBot)
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

            // ── hero gradient header ─────────────────────────────────────────
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
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Your Progress", style = MaterialTheme.typography.headlineSmall.copy(
                            brush = Brush.linearGradient(listOf(Color.White, Color(0xFFB0C8FF)))
                        ), fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        Text("Keep up the great work!",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF93C5FD))
                    }
                    // streak badge
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressRing(
                            progress     = (streakCount.toFloat() / 30f).coerceIn(0f, 1f),
                            size         = 72,
                            strokeWidth  = 7f,
                            primaryColor = Color(0xFFF97316),
                            trackColor   = Color.White.copy(0.15f)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.LocalFireDepartment, null,
                                    Modifier.size(18.dp), Color(0xFFF97316))
                                Text("$streakCount",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White)
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                        Text("day streak", style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF93C5FD))
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── weekly calendar heatmap ──────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape    = RoundedCornerShape(16.dp),
                colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("This Week", style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        days.zip(weekActivity).forEach { (day, activity) ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            when (activity) {
                                                2 -> MaterialTheme.colorScheme.primary
                                                1 -> MaterialTheme.colorScheme.primary.copy(0.4f)
                                                else -> MaterialTheme.colorScheme.surfaceVariant
                                            }
                                        ),
                                    Alignment.Center
                                ) {
                                    if (activity > 0)
                                        Icon(Icons.Default.Check, null, Modifier.size(16.dp),
                                            if (activity == 2) MaterialTheme.colorScheme.onPrimary
                                            else MaterialTheme.colorScheme.primary)
                                }
                                Text(day, style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("5 of 7 days studied",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("Great consistency!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── course progress ring row ─────────────────────────────────────
            Text("Course Overview",
                style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(10.dp))

            Row(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProgressRingCard(
                    label    = "Lessons",
                    value    = "12 / 36",
                    progress = 0.33f,
                    color    = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                ProgressRingCard(
                    label    = "Quizzes",
                    value    = "8 / 20",
                    progress = 0.4f,
                    color    = Color(0xFF059669),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(16.dp))

            // ── stats grid ───────────────────────────────────────────────────
            Text("All-time Stats",
                style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(10.dp))

            Row(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(Icons.Default.Schedule, Color(0xFF0369A1), "5 / 7h",
                    "Hours this week", Modifier.weight(1f))
                StatCard(Icons.Default.Quiz, Color(0xFF7C3AED), "88%",
                    "Quiz average", Modifier.weight(1f))
                StatCard(Icons.Default.EmojiEvents, Color(0xFFF59E0B), "2",
                    "Milestones", Modifier.weight(1f))
            }

            Spacer(Modifier.height(24.dp))

            // ── motivational tip ─────────────────────────────────────────────
            Card(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape  = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.TipsAndUpdates, null, Modifier.size(24.dp),
                        MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(12.dp))
                    Text(
                        "You're in the top 20% of learners with a $streakCount-day streak. " +
                        "Completing 2 more lessons this week will put you ahead of schedule!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ProgressRingCard(
    label: String,
    value: String,
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    val animProg by animateFloatAsState(progress, tween(1200, easing = FastOutSlowInEasing), label = "prc")
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CircularProgressRing(
                progress     = animProg,
                size         = 70,
                strokeWidth  = 8f,
                primaryColor = color,
                trackColor   = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = color
                )
            }
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
