package com.alllearn.app.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alllearn.app.R

// ─────────────────────────────────────────────────────────────────────────────
// TopAppBar
// ─────────────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllLearnTopAppBar(
    title: String,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button_desc)
                    )
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Password input (now with optional leadingIcon)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun PasswordInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = stringResource(R.string.password),
    modifier: Modifier = Modifier,
    showChecklist: Boolean = false,
    confirmPasswordValue: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }

    val hasMinLength   = value.length >= 8
    val hasLetter      = value.any { it.isLetter() }
    val hasNumber      = value.any { it.isDigit() }
    val matchesConfirm = confirmPasswordValue == null ||
            (value == confirmPasswordValue && value.isNotEmpty())

    Column(modifier = modifier) {
        OutlinedTextField(
            value          = value,
            onValueChange  = onValueChange,
            label          = { Text(label) },
            singleLine     = true,
            modifier       = Modifier.fillMaxWidth(),
            shape          = RoundedCornerShape(14.dp),
            leadingIcon    = leadingIcon,
            visualTransformation = if (passwordVisible) VisualTransformation.None
                                   else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility
                                      else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )

        if (showChecklist) {
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                PasswordRuleItem(stringResource(R.string.pwd_rule_length), hasMinLength)
                PasswordRuleItem(stringResource(R.string.pwd_rule_letter),  hasLetter)
                PasswordRuleItem(stringResource(R.string.pwd_rule_number),  hasNumber)
                if (confirmPasswordValue != null) {
                    PasswordRuleItem(stringResource(R.string.pwd_rule_match), matchesConfirm)
                }
            }
        }
    }
}

@Composable
fun PasswordRuleItem(label: String, isSatisfied: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AnimatedContent(targetState = isSatisfied, label = "pwdRule") { ok ->
            Icon(
                imageVector = if (ok) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (ok) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            text  = label,
            fontSize = 12.sp,
            color = if (isSatisfied) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Gradient primary button
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    val primary   = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary

    Button(
        onClick  = onClick,
        enabled  = enabled && !isLoading,
        modifier = modifier.height(54.dp),
        shape    = RoundedCornerShape(14.dp),
        colors   = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (enabled && !isLoading)
                        Brush.linearGradient(listOf(primary, secondary.copy(alpha = 0.85f)))
                    else
                        Brush.linearGradient(listOf(Color.Gray.copy(0.4f), Color.Gray.copy(0.4f))),
                    RoundedCornerShape(14.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color.White,
                    strokeWidth = 2.5.dp
                )
            } else {
                Text(
                    text  = text,
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = Color.White,
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Loading / skeleton card
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun LoadingSkeletonCard(
    text: String    = stringResource(R.string.building_plan_title),
    subText: String = stringResource(R.string.building_plan_sub)
) {
    // animated shimmer alpha
    val shimmer = rememberInfiniteTransition(label = "shimmer")
    val alpha by shimmer.animateFloat(
        0.4f, 1f,
        infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "sa"
    )

    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape    = RoundedCornerShape(20.dp),
        colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                modifier  = Modifier.size(52.dp),
                color     = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp
            )
            Text(text, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(subText, style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha))

            // skeleton lines
            repeat(3) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f - it * 0.15f)
                        .height(10.dp)
                        .background(
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f * alpha),
                            RoundedCornerShape(5.dp)
                        )
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Animated circular progress ring (used in dashboard & progress screen)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun CircularProgressRing(
    progress: Float,          // 0f..1f
    size: Int = 80,
    strokeWidth: Float = 10f,
    primaryColor: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color   = MaterialTheme.colorScheme.surfaceVariant,
    content: @Composable BoxScope.() -> Unit = {}
) {
    val animProg by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "ring"
    )
    Box(
        modifier = Modifier
            .size(size.dp)
            .drawBehind {
                val radius = (this.size.minDimension - strokeWidth) / 2f
                val center = Offset(this.size.width / 2f, this.size.height / 2f)
                // track
                drawArc(
                    color      = trackColor,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter  = false,
                    style      = Stroke(strokeWidth, cap = StrokeCap.Round)
                )
                // fill
                drawArc(
                    color      = primaryColor,
                    startAngle = -90f,
                    sweepAngle = 360f * animProg,
                    useCenter  = false,
                    style      = Stroke(strokeWidth, cap = StrokeCap.Round)
                )
            },
        contentAlignment = Alignment.Center,
        content = content
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Settings section / row helpers
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text  = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
fun SettingsRow(
    icon: ImageVector,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    title: String,
    subtitle: String? = null,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(iconTint.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            if (subtitle != null) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        if (trailing != null) trailing()
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Bottom navigation bar (with animated indicator)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun AllLearnBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onOpenBot: () -> Unit
) {
    val items = listOf(
        Triple("home",     Icons.Default.Home,         stringResource(R.string.nav_home)),
        Triple("courses",  Icons.Default.LibraryBooks,  stringResource(R.string.nav_courses)),
        Triple("progress", Icons.Default.BarChart,      stringResource(R.string.nav_progress)),
        Triple("settings", Icons.Default.Settings,      stringResource(R.string.nav_settings))
    )

    Box(modifier = Modifier.fillMaxWidth()) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 0.5.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
        ) {
            items.forEach { (route, icon, label) ->
                NavigationBarItem(
                    selected   = currentRoute == route,
                    onClick    = { onNavigate(route) },
                    icon = {
                        Icon(icon, contentDescription = label,
                            modifier = Modifier.size(if (currentRoute == route) 26.dp else 22.dp))
                    },
                    label      = { Text(label, style = MaterialTheme.typography.labelSmall) },
                    colors     = NavigationBarItemDefaults.colors(
                        selectedIconColor   = MaterialTheme.colorScheme.primary,
                        selectedTextColor   = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor      = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }

        // Floating AI bot button
        SmallFloatingActionButton(
            onClick          = onOpenBot,
            modifier         = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-12).dp, y = (-22).dp),
            containerColor   = MaterialTheme.colorScheme.primary,
            contentColor     = Color.White,
            shape            = CircleShape
        ) {
            Icon(Icons.Default.SmartToy, contentDescription = "AI Helper Bot",
                modifier = Modifier.size(20.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Report mistake dialog
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun ReportMistakeDialog(onDismiss: () -> Unit, onSubmit: (String) -> Unit) {
    var message by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        title = { Text(stringResource(R.string.report_mistake_title), fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text(stringResource(R.string.report_mistake_desc), style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value         = message,
                    onValueChange = { message = it },
                    placeholder   = { Text("Describe the error or source mismatch…") },
                    modifier      = Modifier.fillMaxWidth(),
                    shape         = RoundedCornerShape(12.dp),
                    maxLines      = 4
                )
            }
        },
        confirmButton = {
            Button(onClick = { if (message.isNotBlank()) onSubmit(message) },
                enabled = message.isNotBlank(),
                shape   = RoundedCornerShape(10.dp)) {
                Text("Submit Report")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Stat card (used in dashboard & progress)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun StatCard(
    icon: ImageVector,
    iconTint: Color,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(iconTint.copy(alpha = 0.14f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
            }
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Empty & error states
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun EmptyState(
    icon: ImageVector = Icons.Default.School,
    title: String,
    subtitle: String? = null,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(32.dp)
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.primary)
        }
        Spacer(Modifier.height(20.dp))
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        if (subtitle != null) {
            Spacer(Modifier.height(8.dp))
            Text(subtitle, style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(24.dp))
            Button(onClick = onAction, shape = RoundedCornerShape(12.dp)) {
                Text(actionLabel)
            }
        }
    }
}
