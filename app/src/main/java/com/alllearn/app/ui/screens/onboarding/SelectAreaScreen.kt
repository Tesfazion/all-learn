package com.alllearn.app.ui.screens.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.alllearn.app.data.remote.AiCurriculumEngine
import com.alllearn.app.ui.components.AllLearnTopAppBar
import com.alllearn.app.ui.components.GradientButton

@Composable
fun SelectAreaScreen(
    onAreaSelected: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var searchQuery    by remember { mutableStateOf("") }
    var customAreaText by remember { mutableStateOf("") }
    var selectedArea   by remember { mutableStateOf<String?>(null) }
    var errorMessage   by remember { mutableStateOf<String?>(null) }

    val otherText = stringResource(R.string.other_specify)
    val aiEngine  = remember { AiCurriculumEngine() }

    // Each area: name, emoji, accent colour
    val areas = listOf(
        Triple("Programming",    "💻", Color(0xFF0369A1)),
        Triple("Psychology",     "🧠", Color(0xFF7C3AED)),
        Triple("Food & Culinary","🍳", Color(0xFFD97706)),
        Triple("Business",       "📈", Color(0xFF059669)),
        Triple("Languages",      "🌍", Color(0xFF0EA5E9)),
        Triple("Mathematics",    "🔢", Color(0xFFE11D48)),
        Triple("Design",         "🎨", Color(0xFFEC4899)),
        Triple("Health & Fitness","💪",Color(0xFF16A34A)),
        Triple(otherText,        "✏️", Color(0xFF64748B))
    )

    val filteredAreas = areas.filter {
        it.first.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            AllLearnTopAppBar(
                title       = stringResource(R.string.select_area_title),
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            Surface(tonalElevation = 4.dp) {
                Column(Modifier.padding(16.dp)) {
                    if (errorMessage != null) {
                        Text(
                            errorMessage!!,
                            color    = MaterialTheme.colorScheme.error,
                            style    = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    GradientButton(
                        text    = stringResource(R.string.continue_btn),
                        onClick = {
                            val finalArea = if (selectedArea == otherText)
                                customAreaText.trim()
                            else
                                selectedArea ?: ""

                            if (finalArea.length !in 2..60) {
                                errorMessage = "Area name must be 2–60 characters."
                                return@GradientButton
                            }
                            if (!aiEngine.isTopicSafe(finalArea)) {
                                errorMessage = "Please enter a valid educational area."
                                return@GradientButton
                            }
                            errorMessage = null
                            onAreaSelected(finalArea)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled  = selectedArea != null &&
                                (selectedArea != otherText || customAreaText.trim().length >= 2)
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            // search bar
            OutlinedTextField(
                value         = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder   = { Text("Search areas…") },
                leadingIcon   = {
                    Icon(Icons.Default.Search, null, tint = MaterialTheme.colorScheme.primary)
                },
                modifier      = Modifier.fillMaxWidth(),
                shape         = RoundedCornerShape(14.dp),
                singleLine    = true
            )

            Spacer(Modifier.height(16.dp))

            // area grid
            LazyVerticalGrid(
                columns               = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement   = Arrangement.spacedBy(12.dp),
                modifier              = Modifier.weight(1f)
            ) {
                items(filteredAreas) { (name, emoji, accentColor) ->
                    val isSelected = selectedArea == name
                    val bgColor by animateColorAsState(
                        targetValue   = if (isSelected) accentColor else MaterialTheme.colorScheme.surface,
                        animationSpec = tween(300),
                        label         = "bg_$name"
                    )

                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(bgColor)
                            .then(
                                if (isSelected)
                                    Modifier.border(
                                        2.dp,
                                        Brush.linearGradient(
                                            listOf(Color.White.copy(0.6f), accentColor)
                                        ),
                                        RoundedCornerShape(18.dp)
                                    )
                                else
                                    Modifier.border(
                                        1.dp,
                                        MaterialTheme.colorScheme.outlineVariant,
                                        RoundedCornerShape(18.dp)
                                    )
                            )
                            .clickable { selectedArea = name; errorMessage = null }
                            .padding(14.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(emoji, fontSize = 24.sp)
                            Text(
                                name,
                                style      = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color      = if (isSelected) Color.White
                                             else MaterialTheme.colorScheme.onSurface,
                                maxLines   = 2
                            )
                        }
                        // selected checkmark
                        if (isSelected) {
                            Box(
                                Modifier
                                    .align(Alignment.TopEnd)
                                    .size(22.dp)
                                    .background(Color.White.copy(0.25f), CircleShape),
                                Alignment.Center
                            ) {
                                Icon(Icons.Default.Check, null, Modifier.size(14.dp), Color.White)
                            }
                        }
                    }
                }
            }

            // custom area text field
            if (selectedArea == otherText) {
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value         = customAreaText,
                    onValueChange = { if (it.length <= 60) customAreaText = it },
                    label         = { Text("Enter your area of study") },
                    modifier      = Modifier.fillMaxWidth(),
                    shape         = RoundedCornerShape(14.dp),
                    singleLine    = true,
                    supportingText = {
                        Text(
                            "${customAreaText.length}/60",
                            color = if (customAreaText.length >= 55) MaterialTheme.colorScheme.error
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}
