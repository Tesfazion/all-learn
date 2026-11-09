package com.alllearn.app.ui.screens.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alllearn.app.R
import com.alllearn.app.data.model.ThemeAccent
import com.alllearn.app.ui.viewmodel.AccountSetupViewModel

@Composable
fun AccountSetupScreen(
    viewModel: AccountSetupViewModel,
    onSetupComplete: () -> Unit
) {
    val selectedSource by viewModel.selectedHeardSource.collectAsState()
    val selectedAccent by viewModel.selectedAccent.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    var customOtherText by remember { mutableStateOf("") }

    val heardFriendText = stringResource(R.string.heard_friend)
    val heardSocialText = stringResource(R.string.heard_social)
    val heardSearchText = stringResource(R.string.heard_search)
    val heardSchoolText = stringResource(R.string.heard_school)
    val heardOtherText = stringResource(R.string.heard_other)
    val heardOptions = listOf(heardFriendText, heardSocialText, heardSearchText, heardSchoolText, heardOtherText)

    Scaffold(
        bottomBar = {
            Surface(
                tonalElevation = 4.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { viewModel.saveSetup(onSetupComplete) }) {
                        Text(stringResource(R.string.skip_btn))
                    }
                    Button(
                        onClick = {
                            val source = if (selectedSource == heardOtherText) {
                                customOtherText.ifBlank { "Other" }
                            } else {
                                selectedSource
                            }
                            viewModel.setHeardSource(source)
                            viewModel.saveSetup(onSetupComplete)
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(stringResource(R.string.continue_btn))
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = stringResource(R.string.setup_heard_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.setup_heard_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                heardOptions.forEach { option ->
                    val isSelected = selectedSource == option
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.setHeardSource(option) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        border = if (isSelected) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.primary)) else null
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { viewModel.setHeardSource(option) }
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                if (selectedSource == heardOtherText) {
                    OutlinedTextField(
                        value = customOtherText,
                        onValueChange = { customOtherText = it },
                        label = { Text("Please specify") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Divider()

            Text(
                text = stringResource(R.string.setup_theme_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ThemeAccent.entries.forEach { accent ->
                    val isSelected = selectedAccent == accent
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(android.graphics.Color.parseColor(accent.hexColor)))
                            .border(
                                width = if (isSelected) 3.dp else 0.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable { viewModel.setAccent(accent) }
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.dark_mode),
                    style = MaterialTheme.typography.titleMedium
                )
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { viewModel.toggleDarkMode(it) }
                )
            }
        }
    }
}
