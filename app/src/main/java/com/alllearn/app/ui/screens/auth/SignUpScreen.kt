package com.alllearn.app.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alllearn.app.R
import com.alllearn.app.ui.components.GradientButton
import com.alllearn.app.ui.components.PasswordInputField
import com.alllearn.app.ui.theme.GradientEnd
import com.alllearn.app.ui.theme.GradientStart
import com.alllearn.app.ui.viewmodel.AuthUiState
import com.alllearn.app.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel,
    onSignUpSuccess: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    var email           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var contentVisible  by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    val isValid = email.isNotBlank()
            && password.length >= 8
            && password.any { it.isLetter() }
            && password.any { it.isDigit() }
            && password == confirmPassword

    LaunchedEffect(Unit) { delay(80); contentVisible = true }
    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) { viewModel.resetState(); onSignUpSuccess() }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.38f)
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF0A1628), Color(0xFF0F2144), Color(0xFF162D5A))
                    )
                )
        ) {
            Box(Modifier.size(200.dp).offset((-60).dp, (-60).dp)
                .background(GradientStart.copy(0.20f), CircleShape).blur(55.dp))
            Box(Modifier.size(160.dp).align(Alignment.BottomEnd).offset(40.dp, 40.dp)
                .background(GradientEnd.copy(0.17f), CircleShape).blur(55.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize().padding(bottom = 16.dp)
            ) {
                Box(
                    Modifier.size(72.dp)
                        .background(
                            Brush.linearGradient(listOf(Color(0xFF38BDF8), Color(0xFFA78BFA))),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AutoStories, null,
                        Modifier.size(36.dp), tint = Color.White)
                }
                Spacer(Modifier.height(14.dp))
                Text(
                    stringResource(R.string.create_account),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        brush = Brush.linearGradient(listOf(Color.White, Color(0xFFB0C8FF))),
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }
        }

        // card sheet
        AnimatedVisibility(
            visible  = contentVisible,
            enter    = fadeIn() + slideInVertically(initialOffsetY = { it / 3 }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.68f)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 24.dp)
                    .padding(top = 28.dp, bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        "Create your account",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = email, onValueChange = { email = it },
                        label = { Text(stringResource(R.string.email)) },
                        leadingIcon = { Icon(Icons.Default.Email, null, tint = MaterialTheme.colorScheme.primary) },
                        singleLine  = true,
                        modifier    = Modifier.fillMaxWidth(),
                        shape       = RoundedCornerShape(14.dp)
                    )

                    PasswordInputField(
                        value = password, onValueChange = { password = it },
                        showChecklist       = true,
                        confirmPasswordValue = confirmPassword,
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = MaterialTheme.colorScheme.primary) }
                    )

                    PasswordInputField(
                        value  = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label  = stringResource(R.string.confirm_password),
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = MaterialTheme.colorScheme.primary) }
                    )

                    AnimatedVisibility(uiState is AuthUiState.Error) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                            shape  = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                (uiState as? AuthUiState.Error)?.message ?: "",
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }

                    GradientButton(
                        text      = stringResource(R.string.sign_up),
                        onClick   = { viewModel.signUp(email, password, confirmPassword) },
                        modifier  = Modifier.fillMaxWidth(),
                        enabled   = isValid,
                        isLoading = uiState is AuthUiState.Loading
                    )

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Text(
                            "Already have an account?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        TextButton(onClick = onNavigateToSignIn) {
                            Text("Sign In", fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }
}
