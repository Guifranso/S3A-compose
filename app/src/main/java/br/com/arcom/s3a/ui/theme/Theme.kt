package br.com.arcom.s3a.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

private val DarkColorPalette = darkColorScheme(
    primary = PrimaryColorDark,
    onPrimary = OnPrimaryColorDark,
    primaryContainer = PrimaryColorContainerDark,
    onPrimaryContainer = OnPrimaryColorContainerDark,
    secondary = SecondaryColorDark,
    onSecondary = OnSecondaryColorDark,
    secondaryContainer = SecondaryColorContainerDark,
    onSecondaryContainer = OnSecondaryColorContainerDark,
    error = ErrorColorDark,
    onError = OnErrorColorDark,
    errorContainer = ErrorColorContainerDark,
    onErrorContainer = OnErrorColorContainerDark,
    background = BackgroundColorDark,
    onBackground = OnBackgroundColorDark,
    surface = SurfaceColorDark,
    onSurface = OnSurfaceColorDark
)

private val LightColorPalette = lightColorScheme(
    primary = PrimaryColorLight,
    onPrimary = OnPrimaryColorLight,
    primaryContainer = PrimaryColorContainerLight,
    onPrimaryContainer = OnPrimaryColorContainerLight,
    secondary = SecondaryColorLight,
    onSecondary = OnSecondaryColorLight,
    secondaryContainer = SecondaryColorContainerLight,
    onSecondaryContainer = OnSecondaryColorContainerLight,
    error = ErrorColorLight,
    onError = OnErrorColorLight,
    errorContainer = ErrorColorContainerLight,
    onErrorContainer = OnErrorColorContainerLight,
    background = BackgroundColorLight,
    onBackground = OnBackgroundColorLight,
    surface = SurfaceColorLight,
    onSurface = OnSurfaceColorLight
)


@Composable
fun S3aTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorPalette
        else -> LightColorPalette
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = S3aTypography,
        content = content
    )
}