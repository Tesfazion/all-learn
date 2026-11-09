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
import com.alllearn.app.data.model.SkillLevel
import com.alllearn.app.ui.components.AllLearnTopAppBar

@Composable
fun SkillLevelScreen(
    area: String,
    topic: String,
    onLevelSelected: (SkillLevel, String) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedLevel by remember { mutableStateOf<SkillLevel?>(null) }
    var levelNote by remember { mutableStateOf("") }

    val levels = listOf(
        Triple(SkillLevel.BEGINNER, stringResource(R.string.beginner), stringResource(R.string.beginner_desc)),
        Triple(SkillLevel.INTERMEDIATE, stringResource(R.string.intermediate), stringResource(R.string.intermediate_desc)),
        Triple(SkillLevel.ADVANCED, stringResource(R.string.advanced), stringResource(R.string.advanced_desc))
    )

    Scaffold(
        topBar = {
            AllLearnTopAppBar(
                title = stringResource(R.string.skill_level_title),
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            Surface(tonalElevation = 4.dp) {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Button(
                        onClick = {
                            if (selectedLevel != null) {
                                onLevelSelected(selectedLevel!!, levelNote.take(200))
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = selectedLevel != null
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
                text = "Topic: $topic",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            levels.forEach { (level, title, desc) ->
                val isSelected = selectedLevel == level
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedLevel = level },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { selectedLevel = level }
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 44.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = levelNote,
                onValueChange = { if (it.length <= 200) levelNote = it },
                label = { Text(stringResource(R.string.level_note_label)) },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                shape = RoundedCornerShape(12.dp),
                supportingText = { Text("${levelNote.length} / 200") }
            )
        }
    }
}
