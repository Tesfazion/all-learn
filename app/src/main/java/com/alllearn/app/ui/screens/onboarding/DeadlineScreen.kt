package com.alllearn.app.ui.screens.onboarding

import androidx.compose.foundation.clickable
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
import com.alllearn.app.data.remote.AiCurriculumEngine
import com.alllearn.app.ui.components.AllLearnTopAppBar

@Composable
fun DeadlineScreen(
    area: String,
    topic: String,
    level: String,
    levelNote: String,
    onDeadlineSelected: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedWeeks by remember { mutableStateOf(12) } // default 3 months
    var isCustom by remember { mutableStateOf(false) }
    var customWeeksText by remember { mutableStateOf("12") }

    val aiEngine = remember { AiCurriculumEngine() }
    val computedDate = aiEngine.computeEndDate(selectedWeeks)

    val options = listOf(
        Pair(stringResource(R.string.duration_1m), 4),
        Pair(stringResource(R.string.duration_2m), 8),
        Pair(stringResource(R.string.duration_3m), 12),
        Pair(stringResource(R.string.duration_6m), 24)
    )

    Scaffold(
        topBar = {
            AllLearnTopAppBar(
                title = stringResource(R.string.deadline_title),
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            Surface(tonalElevation = 4.dp) {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Button(
                        onClick = { onDeadlineSelected(selectedWeeks) },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp)
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
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            options.forEach { (label, weeks) ->
                val isSelected = !isCustom && selectedWeeks == weeks
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            isCustom = false
                            selectedWeeks = weeks
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    isCustom = false
                                    selectedWeeks = weeks
                                }
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        }
                        Text(
                            stringResource(R.string.weeks_count, weeks),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Custom Option
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isCustom = true },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCustom) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = isCustom,
                            onClick = { isCustom = true }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(stringResource(R.string.duration_custom), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    }

                    if (isCustom) {
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = customWeeksText,
                            onValueChange = {
                                customWeeksText = it
                                val parsed = it.toIntOrNull()
                                if (parsed != null && parsed in 2..52) {
                                    selectedWeeks = parsed
                                }
                            },
                            label = { Text("Duration in weeks (2 - 52)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = stringResource(R.string.target_date_summary, computedDate),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}
