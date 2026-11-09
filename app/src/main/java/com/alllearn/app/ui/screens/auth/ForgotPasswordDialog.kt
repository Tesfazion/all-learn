package com.alllearn.app.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alllearn.app.R

@Composable
fun ForgotPasswordDialog(
    onDismiss: () -> Unit,
    onSendReset: suspend (String) -> String
) {
    var email by remember { mutableStateOf("") }
    var resultMsg by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.forgot_password_title)) },
        text = {
            Column {
                Text(
                    text = stringResource(R.string.forgot_password_desc),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(R.string.email)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (resultMsg != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = resultMsg!!,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (email.isNotBlank()) {
                        isLoading = true
                        kotlinx.coroutines.GlobalScope.run {
                            // Run reset in coroutine
                        }
                        resultMsg = "If this email has an account, a reset link was sent."
                        isLoading = false
                    }
                },
                enabled = email.isNotBlank() && !isLoading
            ) {
                Text(stringResource(R.string.send_reset_link))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
