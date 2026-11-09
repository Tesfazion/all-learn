package com.alllearn.app.ui.screens.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
fun SelectAreaScreen(
    onAreaSelected: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var customAreaText by remember { mutableStateOf("") }
    var selectedArea by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val otherSpecifyText = stringResource(R.string.other_specify)
    val defaultAreas = listOf(
        "Programming", "Psychology", "Food & Culinary",
        "Business", "Languages", "Mathematics",
        "Design", "Health & Fitness", otherSpecifyText
    )

    val filteredAreas = defaultAreas.filter {
        it.contains(searchQuery, ignoreCase = true)
    }

    val aiEngine = remember { AiCurriculumEngine() }

    Scaffold(
        topBar = {
            AllLearnTopAppBar(
                title = stringResource(R.string.select_area_title),
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            Surface(tonalElevation = 4.dp) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = {
                            val finalArea = if (selectedArea == otherSpecifyText) {
                                customAreaText.trim()
                            } else {
                                selectedArea ?: ""
                            }

                            if (finalArea.length !in 2..60) {
                                errorMessage = "Area name must be between 2 and 60 characters."
                                return@Button
                            }

                            if (!aiEngine.isTopicSafe(finalArea)) {
                                errorMessage = "Please enter a valid educational area."
                                return@Button
                            }

                            errorMessage = null
                            onAreaSelected(finalArea)
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = selectedArea != null && (selectedArea != otherSpecifyText || customAreaText.trim().length >= 2)
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
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search areas...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredAreas) { area ->
                    val isSelected = selectedArea == area
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .clickable {
                                selectedArea = area
                                errorMessage = null
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        border = if (isSelected) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.primary)) else null
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = area,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            if (selectedArea == otherSpecifyText) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = customAreaText,
                    onValueChange = { customAreaText = it },
                    label = { Text("Enter your area of study") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
