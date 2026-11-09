package com.alllearn.app.ui.screens.checkin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alllearn.app.R
import com.alllearn.app.data.model.AdjustmentChoice

@Composable
fun WeeklyCheckInDialog(
    weekNumber: Int,
    completionRate: Int,
    onDismiss: () -> Unit,
    onApplyAdjustment: (AdjustmentChoice) -> Unit
) {
    var selectedChoice by remember {
        mutableStateOf(if (completionRate >= 70) AdjustmentChoice.CONTINUE else AdjustmentChoice.COMPRESS)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.checkin_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = stringResource(R.string.checkin_question, weekNumber),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.completion_rate_msg, completionRate),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                if (completionRate < 70) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "You're behind schedule. How would you like to adjust your remaining plan?",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Option 1: Compress
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedChoice = AdjustmentChoice.COMPRESS },
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedChoice == AdjustmentChoice.COMPRESS) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedChoice == AdjustmentChoice.COMPRESS,
                                onClick = { selectedChoice = AdjustmentChoice.COMPRESS }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(R.string.adjust_choice_compress), style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    // Option 2: Extend
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedChoice = AdjustmentChoice.EXTEND },
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedChoice == AdjustmentChoice.EXTEND) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedChoice == AdjustmentChoice.EXTEND,
                                onClick = { selectedChoice = AdjustmentChoice.EXTEND }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(R.string.adjust_choice_extend), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onApplyAdjustment(selectedChoice) }) {
                Text(stringResource(R.string.apply_adjustment))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
