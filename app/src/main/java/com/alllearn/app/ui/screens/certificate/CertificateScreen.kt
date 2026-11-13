package com.alllearn.app.ui.screens.certificate

import android.content.Intent
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alllearn.app.R
import com.alllearn.app.ui.components.AllLearnTopAppBar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@Composable
fun CertificateScreen(
    courseTitle: String,
    learnerName: String = "Learner",
    onBackClick: () -> Unit
) {
    val context       = LocalContext.current
    val issueDate     = remember { SimpleDateFormat("MMMM dd, yyyy", Locale.US).format(Date()) }
    val certificateId = remember { "AL-${UUID.randomUUID().toString().take(8).uppercase()}" }
    val goldStart     = Color(0xFFF59E0B)
    val goldEnd       = Color(0xFFD97706)

    Scaffold(
        topBar = {
            AllLearnTopAppBar(title = "Certificate of Completion", onBackClick = onBackClick)
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ── gold-bordered certificate card ───────────────────────────────
            Box(
                Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        val strokeWidth = 3.dp.toPx()
                        val radius      = 20.dp.toPx()
                        drawRoundRect(
                            brush        = Brush.linearGradient(
                                listOf(goldStart, Color(0xFFFFD700), goldEnd, Color(0xFFFFD700), goldStart)
                            ),
                            cornerRadius = CornerRadius(radius),
                            style        = Stroke(strokeWidth)
                        )
                    }
            ) {
                Card(
                    Modifier.fillMaxWidth().padding(3.dp),
                    shape  = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        Modifier.padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // star row
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            repeat(5) {
                                Icon(Icons.Default.Star, null, Modifier.size(16.dp), tint = goldStart)
                            }
                        }

                        // trophy circle
                        Box(
                            Modifier.size(80.dp).background(
                                Brush.linearGradient(
                                    listOf(Color(0xFFFEF3C7), Color(0xFFFDE68A))
                                ),
                                CircleShape
                            ),
                            Alignment.Center
                        ) {
                            Icon(Icons.Default.WorkspacePremium, null,
                                Modifier.size(44.dp), tint = Color(0xFFB45309))
                        }

                        Text(
                            stringResource(R.string.app_name),
                            style      = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color      = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            "CERTIFICATE OF COMPLETION",
                            style      = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign  = TextAlign.Center,
                            letterSpacing = 2.sp
                        )

                        HorizontalDivider(
                            Modifier.fillMaxWidth(0.6f),
                            color = goldStart.copy(0.5f)
                        )

                        Text(
                            "This is proudly presented to",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            learnerName,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                brush = Brush.linearGradient(
                                    listOf(Color(0xFFB45309), Color(0xFFF59E0B))
                                )
                            ),
                            fontWeight = FontWeight.ExtraBold
                        )

                        Text(
                            "for successfully completing all modules, projects, and quizzes in",
                            style     = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color     = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            courseTitle,
                            style      = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color      = MaterialTheme.colorScheme.primary,
                            textAlign  = TextAlign.Center
                        )

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Issue Date",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(issueDate, style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Verification ID",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(certificateId, style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold)
                            }
                        }

                        // bottom star row
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            repeat(5) {
                                Icon(Icons.Default.Star, null, Modifier.size(14.dp),
                                    tint = goldStart.copy(0.6f))
                            }
                        }
                    }
                }
            }

            // ── action buttons ───────────────────────────────────────────────
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick  = { /* stub — download feature in Phase 2 */ },
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape    = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Download, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Save")
                }
                Button(
                    onClick  = {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, "My All Learn Certificate – $courseTitle")
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "🎓 I completed $courseTitle on All Learn!\n" +
                                "Verification ID: $certificateId"
                            )
                        }
                        context.startActivity(Intent.createChooser(intent, "Share Certificate"))
                    },
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape    = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Share, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Share")
                }
            }
        }
    }
}
