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
    val colorScheme = if (darkTheme) {
        when (accent) {
            ThemeAccent.BLUE -> darkColorScheme(
                primary           = BluePrimaryDark,
                onPrimary         = Color(0xFF001829),
                primaryContainer  = BlueContainerDark,
                onPrimaryContainer= BluePrimaryDark,
                secondary         = BlueSecondary,
                background        = DarkBackground,
                surface           = DarkSurface,
                onSurface         = DarkOnSurface,
                surfaceVariant    = DarkSurfaceVariant,
                onSurfaceVariant  = DarkOnSurfaceVariant,
                error             = ErrorRed,
                outline           = Color(0xFF334155),
                outlineVariant    = Color(0xFF1E293B)
            )
            ThemeAccent.EMERALD -> darkColorScheme(
                primary           = EmeraldPrimaryDark,
                onPrimary         = Color(0xFF00200E),
                primaryContainer  = EmeraldContainerDark,
                onPrimaryContainer= EmeraldPrimaryDark,
                background        = DarkBackground,
                surface           = DarkSurface,
                onSurface         = DarkOnSurface,
                surfaceVariant    = DarkSurfaceVariant,
                onSurfaceVariant  = DarkOnSurfaceVariant,
                error             = ErrorRed,
                outline           = Color(0xFF334155),
                outlineVariant    = Color(0xFF1E293B)
            )
            ThemeAccent.PURPLE -> darkColorScheme(
                primary           = PurplePrimaryDark,
                onPrimary         = Color(0xFF1A0040),
                primaryContainer  = PurpleContainerDark,
                onPrimaryContainer= PurplePrimaryDark,
                background        = DarkBackground,
                surface           = DarkSurface,
                onSurface         = DarkOnSurface,
                surfaceVariant    = DarkSurfaceVariant,
                onSurfaceVariant  = DarkOnSurfaceVariant,
                error             = ErrorRed,
                outline           = Color(0xFF334155),
                outlineVariant    = Color(0xFF1E293B)
            )
            ThemeAccent.AMBER -> darkColorScheme(
                primary           = AmberPrimaryDark,
                onPrimary         = Color(0xFF2D1500),
                primaryContainer  = AmberContainerDark,
                onPrimaryContainer= AmberPrimaryDark,
                background        = DarkBackground,
                surface           = DarkSurface,
                onSurface         = DarkOnSurface,
                surfaceVariant    = DarkSurfaceVariant,
                onSurfaceVariant  = DarkOnSurfaceVariant,
                error             = ErrorRed,
                outline           = Color(0xFF334155),
                outlineVariant    = Color(0xFF1E293B)
            )
            ThemeAccent.ROSE -> darkColorScheme(
                primary           = RosePrimaryDark,
                onPrimary         = Color(0xFF2D0010),
                primaryContainer  = RoseContainerDark,
                onPrimaryContainer= RosePrimaryDark,
                background        = DarkBackground,
                surface           = DarkSurface,
                onSurface         = DarkOnSurface,
                surfaceVariant    = DarkSurfaceVariant,
                onSurfaceVariant  = DarkOnSurfaceVariant,
                error             = ErrorRed,
                outline           = Color(0xFF334155),
                outlineVariant    = Color(0xFF1E293B)
            )
        }
    } else {
        when (accent) {
            ThemeAccent.BLUE -> lightColorScheme(
                primary           = BluePrimary,
                onPrimary         = Color.White,
                primaryContainer  = BlueContainer,
                onPrimaryContainer= Color(0xFF004466),
                secondary         = BlueSecondary,
                background        = LightBackground,
                surface           = LightSurface,
                onSurface         = LightOnSurface,
                surfaceVariant    = LightSurfaceVariant,
                onSurfaceVariant  = LightOnSurfaceVariant,
                error             = ErrorRed,
                outline           = Color(0xFFCBD5E1),
                outlineVariant    = Color(0xFFE2E8F0)
            )
            ThemeAccent.EMERALD -> lightColorScheme(
                primary           = EmeraldPrimary,
                onPrimary         = Color.White,
                primaryContainer  = EmeraldContainer,
                onPrimaryContainer= Color(0xFF004D2A),
                background        = LightBackground,
                surface           = LightSurface,
                onSurface         = LightOnSurface,
                surfaceVariant    = LightSurfaceVariant,
                onSurfaceVariant  = LightOnSurfaceVariant,
                error             = ErrorRed,
                outline           = Color(0xFFCBD5E1),
                outlineVariant    = Color(0xFFE2E8F0)
            )
            ThemeAccent.PURPLE -> lightColorScheme(
                primary           = PurplePrimary,
                onPrimary         = Color.White,
                primaryContainer  = PurpleContainer,
                onPrimaryContainer= Color(0xFF3B0A6B),
                background        = LightBackground,
                surface           = LightSurface,
                onSurface         = LightOnSurface,
                surfaceVariant    = LightSurfaceVariant,
                onSurfaceVariant  = LightOnSurfaceVariant,
                error             = ErrorRed,
                outline           = Color(0xFFCBD5E1),
                outlineVariant    = Color(0xFFE2E8F0)
            )
            ThemeAccent.AMBER -> lightColorScheme(
                primary           = AmberPrimary,
                onPrimary         = Color.White,
                primaryContainer  = AmberContainer,
                onPrimaryContainer= Color(0xFF5A2900),
                background        = LightBackground,
                surface           = LightSurface,
                onSurface         = LightOnSurface,
                surfaceVariant    = LightSurfaceVariant,
                onSurfaceVariant  = LightOnSurfaceVariant,
                error             = ErrorRed,
                outline           = Color(0xFFCBD5E1),
                outlineVariant    = Color(0xFFE2E8F0)
            )
            ThemeAccent.ROSE -> lightColorScheme(
                primary           = RosePrimary,
                onPrimary         = Color.White,
                primaryContainer  = RoseContainer,
                onPrimaryContainer= Color(0xFF5A001A),
                background        = LightBackground,
                surface           = LightSurface,
                onSurface         = LightOnSurface,
                surfaceVariant    = LightSurfaceVariant,
                onSurfaceVariant  = LightOnSurfaceVariant,
                error             = ErrorRed,
                outline           = Color(0xFFCBD5E1),
                outlineVariant    = Color(0xFFE2E8F0)
            )
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}
