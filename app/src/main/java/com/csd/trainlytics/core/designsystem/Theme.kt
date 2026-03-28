package com.csd.trainlytics.core.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = Tertiary,
    onSecondary = OnSurface,
    tertiary = Tertiary,
    onTertiary = OnSurface,
    tertiaryContainer = TertiaryContainer,
    error = Error,
    errorContainer = ErrorContainer,
    background = Surface,
    onBackground = OnSurface,
    surface = Surface,
    onSurface = OnSurface,
    onSurfaceVariant = OnSurfaceVariant,
    surfaceContainer = SurfaceContainer,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest,
    outline = OutlineVariant,
    outlineVariant = OutlineVariant,
    inverseSurface = OnSurface,
    inverseOnSurface = Surface,
    scrim = Surface
)

@Composable
fun TrainlyticsTheme(
    content: @Composable () -> Unit
) {
    // Trainlytics is dark-mode-first; no light variant
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = TrainlyticsTypography,
        content = content
    )
}
