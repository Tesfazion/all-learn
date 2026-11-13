package com.alllearn.app.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.alllearn.app.R
import com.alllearn.app.data.model.ThemeAccent
import com.alllearn.app.ui.components.*
import com.alllearn.app.ui.navigation.Screen
import com.alllearn.app.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateToPaywall: () -> Unit,
    onSignOut: () -> Unit,
    onNavigate: (String) -> Unit,
    onOpenBot: () -> Unit,
    userEmail: String = ""
) {
    val currentAccentName by viewModel.currentAccent.collectAsState()
    val isDarkMode        by viewModel.isDarkMode.collectAsState()
    var showDeleteConfirm  by remember { mutableStateOf(false) }
    var notificationsOn    by remember { mutableStateOf(true) }

    val currentAccent = remember(currentAccentName) {
        try { ThemeAccent.valueOf(currentAccentName) } catch (e: Exception) { ThemeAccent.BLUE }
    }
    val emailInitial = userEmail.firstOrNull()?.uppercaseChar()?.toString() ?: "A"

    Scaffold(
        topBar = { AllLearnTopAppBar(title = stringResource(R.string.settings_title)) },
        bottomBar = {
            AllLearnBottomBar(currentRoute = "settings", onNavigate = onNavigate, onOpenBot = onOpenBot)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())) {

            // ── profile header (brand navy gradient) ─────────────────────────
            Box(
                Modifier.fillMaxWidth()
                    .background(Brush.linearGradient(listOf(Color(0xFF060D1F), Color(0xFF0D1A35))))
                    .padding(24.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.size(60.dp).background(
                            Brush.linearGradient(listOf(Color(0xFF38BDF8), Color(0xFF8B5CF6))),
                            CircleShape
                        ),
                        Alignment.Center
                    ) {
                        Text(emailInitial, style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            if (userEmail.isNotBlank()) userEmail else "All Learn User",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold, color = Color.White
                        )
                        Spacer(Modifier.height(4.dp))
                        Box(
                            Modifier.clip(RoundedCornerShape(20.dp))
                                .background(Color.White.copy(0.15f))
                                .padding(horizontal = 10.dp, vertical = 3.dp)
                        ) {
                            Text("Free Plan", style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF93C5FD))
                        }
                    }
                }
            }

            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(0.dp)) {

                // upgrade banner
                Card(
                    Modifier.fillMaxWidth().clickable { onNavigateToPaywall() },
                    shape  = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, null, Modifier.size(28.dp),
                            MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(stringResource(R.string.upgrade_pro),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Text("Unlock unlimited courses, bot & certificates",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                        Icon(Icons.Default.ChevronRight, null,
                            tint = MaterialTheme.colorScheme.primary)
                    }
                }

                Spacer(Modifier.height(20.dp))

                // ── Appearance ───────────────────────────────────────────────
                SettingsSectionHeader("Appearance")
                Card(
                    Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(Modifier.padding(horizontal = 16.dp)) {
                        SettingsRow(
                            icon     = Icons.Default.DarkMode,
                            title    = stringResource(R.string.dark_mode),
                            subtitle = if (isDarkMode) "Dark theme active" else "Light theme active",
                            trailing = {
                                Switch(checked = isDarkMode,
                                    onCheckedChange = { viewModel.updateTheme(currentAccent, it) })
                            }
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Column(Modifier.padding(vertical = 12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    Modifier.size(38.dp).background(
                                        MaterialTheme.colorScheme.primary.copy(0.12f), CircleShape
                                    ), Alignment.Center
                                ) {
                                    Icon(Icons.Default.Palette, null, Modifier.size(20.dp),
                                        MaterialTheme.colorScheme.primary)
                                }
                                Spacer(Modifier.width(14.dp))
                                Text(stringResource(R.string.accent_color),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium)
                            }
                            Spacer(Modifier.height(12.dp))
                            Row(
                                Modifier.fillMaxWidth().padding(start = 52.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                ThemeAccent.entries.forEach { accent ->
                                    val isSelected = currentAccent == accent
                                    Box(
                                        Modifier.size(40.dp).clip(CircleShape)
                                            .background(Color(android.graphics.Color.parseColor(accent.hexColor)))
                                            .then(if (isSelected) Modifier.border(3.dp, MaterialTheme.colorScheme.onSurface, CircleShape) else Modifier)
                                            .clickable { viewModel.updateTheme(accent, isDarkMode) },
                                        Alignment.Center
                                    ) {
                                        if (isSelected) Icon(Icons.Default.Check, null, Modifier.size(18.dp), Color.White)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                // ── Notifications ────────────────────────────────────────────
                SettingsSectionHeader("Notifications")
                Card(
                    Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(Modifier.padding(horizontal = 16.dp)) {
                        SettingsRow(
                            icon     = Icons.Default.Notifications,
                            title    = "Study Reminders",
                            subtitle = if (notificationsOn) "Daily reminder enabled" else "Reminders off",
                            trailing = { Switch(checked = notificationsOn, onCheckedChange = { notificationsOn = it }) }
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        SettingsRow(
                            icon = Icons.Default.AccessTime, title = "Reminder Time", subtitle = "8:00 PM",
                            onClick = {},
                            trailing = { Icon(Icons.Default.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) }
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                // ── AI Configuration (BYOK) ──────────────────────────────────
                BYOKSection(viewModel = viewModel)

                Spacer(Modifier.height(20.dp))

                // ── Account & Privacy ────────────────────────────────────────
                SettingsSectionHeader("Account & Privacy")
                Card(
                    Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(Modifier.padding(horizontal = 16.dp)) {
                        SettingsRow(
                            icon = Icons.Default.Lock, title = "Change Password", onClick = {},
                            trailing = { Icon(Icons.Default.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) }
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        SettingsRow(
                            icon = Icons.Default.PrivacyTip, title = stringResource(R.string.privacy_policy),
                            onClick = { onNavigate(Screen.PrivacyPolicy.route) },
                            trailing = { Icon(Icons.Default.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) }
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        SettingsRow(
                            icon = Icons.Default.Logout, iconTint = MaterialTheme.colorScheme.error,
                            title = stringResource(R.string.sign_out),
                            onClick = { viewModel.signOut(onSignOut) }
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                // ── Danger zone ──────────────────────────────────────────────
                SettingsSectionHeader("Danger Zone")
                Card(
                    Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(0.3f)),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Box(Modifier.padding(horizontal = 16.dp)) {
                        SettingsRow(
                            icon = Icons.Default.DeleteForever, iconTint = MaterialTheme.colorScheme.error,
                            title = stringResource(R.string.delete_account),
                            subtitle = "Permanently removes your data",
                            onClick = { showDeleteConfirm = true }
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))
                Text("All Learn v1.0.0  •  Built with ❤️",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(Modifier.height(16.dp))
            }
        }

        if (showDeleteConfirm) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirm = false },
                shape = RoundedCornerShape(20.dp),
                title = { Text(stringResource(R.string.delete_account), fontWeight = FontWeight.Bold) },
                text  = { Text("This permanently deletes your account, all courses, and progress. Cannot be undone.") },
                confirmButton = {
                    Button(onClick = { showDeleteConfirm = false; viewModel.deleteAccountAndData(onSignOut) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        shape = RoundedCornerShape(10.dp)) { Text("Delete Everything") }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirm = false }) { Text(stringResource(R.string.cancel)) }
                }
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// BYOK — Bring Your Own API Key section
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun BYOKSection(viewModel: SettingsViewModel) {
    val byokKey     by viewModel.byokKey.collectAsState()
    val byokEnabled by viewModel.byokEnabled.collectAsState()

    var keyInput   by remember(byokKey) { mutableStateOf(byokKey) }
    var keyVisible by remember { mutableStateOf(false) }
    var saved      by remember { mutableStateOf(false) }

    SettingsSectionHeader("AI Configuration")
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(38.dp).background(
                        MaterialTheme.colorScheme.primary.copy(0.12f), CircleShape
                    ), Alignment.Center
                ) {
                    Icon(Icons.Default.VpnKey, null, Modifier.size(20.dp),
                        MaterialTheme.colorScheme.primary)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("Bring Your Own API Key", style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold)
                    Text("Use your OpenRouter key instead of the app default",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = byokEnabled,
                    onCheckedChange = { on -> viewModel.saveBYOKKey(keyInput, on); saved = false }
                )
            }

            if (byokEnabled) {
                OutlinedTextField(
                    value = keyInput, onValueChange = { keyInput = it; saved = false },
                    label = { Text("Your OpenRouter API Key") },
                    placeholder = { Text("sk-or-v1-…") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    visualTransformation = if (keyVisible) VisualTransformation.None
                                           else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { keyVisible = !keyVisible }) {
                            Icon(
                                if (keyVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                "Toggle key visibility"
                            )
                        }
                    }
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (saved) {
                        Card(
                            Modifier.weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, null, Modifier.size(16.dp),
                                    MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(6.dp))
                                Text("Key saved & active",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    } else {
                        Button(
                            onClick = { viewModel.saveBYOKKey(keyInput, true); saved = true },
                            modifier = Modifier.weight(1f).height(44.dp),
                            shape = RoundedCornerShape(10.dp),
                            enabled = keyInput.startsWith("sk-")
                        ) { Text("Save Key") }
                    }
                    OutlinedButton(
                        onClick = { viewModel.clearBYOKKey(); keyInput = ""; saved = false },
                        modifier = Modifier.height(44.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) { Text("Clear") }
                }

                Text(
                    "Get a free key at openrouter.ai → Keys. " +
                    "Your key is stored only on this device and never sent to our servers.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
