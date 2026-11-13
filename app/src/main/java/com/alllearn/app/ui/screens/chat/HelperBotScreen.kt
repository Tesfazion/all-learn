package com.alllearn.app.ui.screens.chat

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.alllearn.app.R
import com.alllearn.app.data.model.MessageRole
import com.alllearn.app.ui.components.AllLearnTopAppBar
import com.alllearn.app.ui.viewmodel.HelperBotViewModel
import kotlinx.coroutines.launch

@Composable
fun HelperBotScreen(
    courseId: String,
    lessonId: String,
    viewModel: HelperBotViewModel,
    onBackClick: () -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    val chatMessages    = viewModel.getChatMessagesFlow(courseId).collectAsState(emptyList())
    val remainingMessages by viewModel.remainingMessages.collectAsState()
    val isSending         by viewModel.isSending.collectAsState()
    val errorMessage      by viewModel.errorMessage.collectAsState()
    val listState         = rememberLazyListState()
    val scope             = rememberCoroutineScope()

    val suggestions = listOf(
        "Explain this simply",
        "Give a code example",
        "What to study next?",
        "Quiz me on this"
    )

    LaunchedEffect(Unit) { viewModel.loadRemainingMessages() }
    LaunchedEffect(chatMessages.value.size) {
        if (chatMessages.value.isNotEmpty()) {
            scope.launch { listState.animateScrollToItem(chatMessages.value.size - 1) }
        }
    }

    Scaffold(
        topBar = {
            AllLearnTopAppBar(
                title       = stringResource(R.string.helper_bot_title),
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            Column {
                // remaining count bar
                val countColor = when {
                    remainingMessages > 10 -> MaterialTheme.colorScheme.primary
                    remainingMessages > 3  -> Color(0xFFD97706)
                    else                   -> MaterialTheme.colorScheme.error
                }
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Text(
                        stringResource(R.string.bot_messages_remaining, remainingMessages),
                        style      = MaterialTheme.typography.labelMedium,
                        color      = countColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Surface(tonalElevation = 8.dp) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment     = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value         = inputText,
                            onValueChange = { inputText = it },
                            placeholder   = { Text(stringResource(R.string.type_message_hint)) },
                            modifier      = Modifier.weight(1f),
                            shape         = RoundedCornerShape(24.dp),
                            maxLines      = 4,
                            enabled       = remainingMessages > 0 && !isSending
                        )
                        Box(
                            Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    if (inputText.isNotBlank() && remainingMessages > 0 && !isSending)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.surfaceVariant
                                )
                                .clickable(
                                    enabled = inputText.isNotBlank() && remainingMessages > 0 && !isSending
                                ) {
                                    val query = inputText.trim()
                                    inputText = ""
                                    viewModel.sendMessage(
                                        courseId     = courseId,
                                        lessonId     = lessonId,
                                        courseTopic  = "Current Course",
                                        lessonTitle  = "Current Lesson",
                                        query        = query
                                    )
                                },
                            Alignment.Center
                        ) {
                            if (isSending) {
                                CircularProgressIndicator(
                                    Modifier.size(22.dp),
                                    color       = MaterialTheme.colorScheme.primary,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    Icons.AutoMirrored.Filled.Send,
                                    "Send",
                                    Modifier.size(20.dp),
                                    tint = if (inputText.isNotBlank()) Color.White
                                           else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {

            // ── gradient header ──────────────────────────────────────────────
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF0A1628), Color(0xFF0F2144)))
                    )
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.size(44.dp).background(
                            Brush.linearGradient(listOf(Color(0xFF38BDF8), Color(0xFFA78BFA))),
                            CircleShape
                        ),
                        Alignment.Center
                    ) {
                        Icon(Icons.Default.SmartToy, null, Modifier.size(24.dp), Color.White)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            "AI Helper Bot",
                            style      = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color      = Color.White
                        )
                        Text(
                            stringResource(R.string.helper_bot_disclaimer),
                            style    = MaterialTheme.typography.labelSmall,
                            color    = Color(0xFF93C5FD),
                            maxLines = 2
                        )
                    }
                }
            }

            // ── suggestion chips (only when empty) ───────────────────────────
            if (chatMessages.value.isEmpty()) {
                LazyRow(
                    contentPadding        = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(suggestions) { chip ->
                        SuggestionChip(
                            onClick  = { inputText = chip },
                            label    = { Text(chip, style = MaterialTheme.typography.labelMedium) },
                            leadingIcon = {
                                Icon(Icons.Default.Lightbulb, null, Modifier.size(16.dp))
                            }
                        )
                    }
                }
            }

            // ── error ────────────────────────────────────────────────────────
            if (errorMessage != null) {
                Card(
                    Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        errorMessage!!,
                        style    = MaterialTheme.typography.bodySmall,
                        color    = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            // ── message list ─────────────────────────────────────────────────
            LazyColumn(
                state          = listState,
                modifier       = Modifier.weight(1f).padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(chatMessages.value) { msg ->
                    val isUser = msg.role == MessageRole.USER
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
                        verticalAlignment     = Alignment.Bottom
                    ) {
                        if (!isUser) {
                            Box(
                                Modifier.size(28.dp).background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFF38BDF8), Color(0xFFA78BFA))
                                    ),
                                    CircleShape
                                ),
                                Alignment.Center
                            ) {
                                Icon(Icons.Default.SmartToy, null, Modifier.size(14.dp), Color.White)
                            }
                            Spacer(Modifier.width(6.dp))
                        }
                        Card(
                            shape = RoundedCornerShape(
                                topStart    = if (isUser) 18.dp else 4.dp,
                                topEnd      = if (isUser) 4.dp else 18.dp,
                                bottomStart = 18.dp,
                                bottomEnd   = 18.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isUser)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier.widthIn(max = 290.dp)
                        ) {
                            Text(
                                msg.text,
                                modifier = Modifier.padding(12.dp),
                                style    = MaterialTheme.typography.bodyMedium,
                                color    = if (isUser) MaterialTheme.colorScheme.onPrimary
                                           else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // typing indicator
                if (isSending) {
                    item {
                        Row(Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                Modifier.size(28.dp).background(
                                    Brush.linearGradient(listOf(Color(0xFF38BDF8), Color(0xFFA78BFA))),
                                    CircleShape
                                ),
                                Alignment.Center
                            ) {
                                Icon(Icons.Default.SmartToy, null, Modifier.size(14.dp), Color.White)
                            }
                            Spacer(Modifier.width(6.dp))
                            TypingIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TypingIndicator() {
    val transition = rememberInfiniteTransition(label = "typing")
    val dot1 by transition.animateFloat(
        0f, 1f, infiniteRepeatable(tween(600, 0),   RepeatMode.Reverse), label = "d1")
    val dot2 by transition.animateFloat(
        0f, 1f, infiniteRepeatable(tween(600, 200), RepeatMode.Reverse), label = "d2")
    val dot3 by transition.animateFloat(
        0f, 1f, infiniteRepeatable(tween(600, 400), RepeatMode.Reverse), label = "d3")

    Card(
        shape  = RoundedCornerShape(topStart = 4.dp, topEnd = 18.dp, bottomStart = 18.dp, bottomEnd = 18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment     = Alignment.CenterVertically
        ) {
            listOf(dot1, dot2, dot3).forEach { dot ->
                Box(
                    Modifier
                        .size(7.dp)
                        .offset(y = (-5 * dot).dp)
                        .background(
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(0.5f + 0.5f * dot),
                            CircleShape
                        )
                )
            }
        }
    }
}
