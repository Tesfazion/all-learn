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
fun WeeklyHoursScreen(
    area: String,
    topic: String,
    level: String,
    levelNote: String,
    deadlineWeeks: Int,
    onHoursSelected: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedHours by remember { mutableStateOf(7) } // default 7 hrs/week
    var customHoursText by remember { mutableStateOf("7") }
    var isCustom by remember { mutableStateOf(false) }

    val daysList = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val selectedDays = remember { mutableStateListOf("Mon", "Wed", "Fri") }

    val quickOptions = listOf(3, 5, 7, 10, 15)
    val totalHours = selectedHours * deadlineWeeks

    val aiEngine = remember { AiCurriculumEngine() }
    val computedDate = aiEngine.computeEndDate(deadlineWeeks)

    Scaffold(
        topBar = {
            AllLearnTopAppBar(
                title = stringResource(R.string.weekly_hours_title),
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            Surface(tonalElevation = 4.dp) {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Button(
                        onClick = { onHoursSelected(selectedHours) },
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
            Text(
                text = "Target: $deadlineWeeks Weeks",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            quickOptions.forEach { hours ->
                val isSelected = !isCustom && selectedHours == hours
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            isCustom = false
                            selectedHours = hours
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = {
                                isCustom = false
                                selectedHours = hours
                            }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            stringResource(R.string.hours_per_week_suffix, hours),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Study Days Selection
            Text(
                text = stringResource(R.string.study_days_label),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                daysList.forEach { day ->
                    val isChecked = selectedDays.contains(day)
                    FilterChip(
                        selected = isChecked,
                        onClick = {
                            if (isChecked) selectedDays.remove(day) else selectedDays.add(day)
                        },
                        label = { Text(day) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = stringResource(R.string.summary_total_hours, totalHours, computedDate),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )

                    if (totalHours < 20) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.low_hours_warning),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}
