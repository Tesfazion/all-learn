package com.alllearn.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.alllearn.app.data.model.ThemeAccent

@Composable
fun AllLearnTheme(
    accent: ThemeAccent = ThemeAccent.BLUE,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val primaryColor = if (darkTheme) {
        when (accent) {
            ThemeAccent.BLUE -> BluePrimaryDark
            ThemeAccent.EMERALD -> EmeraldPrimaryDark
            ThemeAccent.PURPLE -> PurplePrimaryDark
            ThemeAccent.AMBER -> AmberPrimaryDark
            ThemeAccent.ROSE -> RosePrimaryDark
        }
    } else {
        when (accent) {
            ThemeAccent.BLUE -> BluePrimary
            ThemeAccent.EMERALD -> EmeraldPrimary
            ThemeAccent.PURPLE -> PurplePrimary
            ThemeAccent.AMBER -> AmberPrimary
            ThemeAccent.ROSE -> RosePrimary
        }
    }

    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = primaryColor,
            background = DarkBackground,
            surface = DarkSurface,
            onSurface = DarkOnSurface,
            surfaceVariant = Color(0xFF334155),
            onSurfaceVariant = Color(0xFFCBD5E1)
        )
    } else {
        lightColorScheme(
            primary = primaryColor,
            background = LightBackground,
            surface = LightSurface,
            onSurface = LightOnSurface,
            surfaceVariant = Color(0xFFF1F5F9),
            onSurfaceVariant = Color(0xFF475569)
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
