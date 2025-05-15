package com.example.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview


val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

private val BloodDarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val BloodLightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

val BloodTypography = Typography()

private val CameraXLightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),
    onPrimary = Color.White,
    secondary = Color(0xFF03DAC6),
    onSecondary = Color.Black
)

private val CameraXDarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),
    onPrimary = Color.Black,
    secondary = Color(0xFF03DAC6),
    onSecondary = Color.Black
)

val CameraXTypography = Typography()

@Composable
fun BloodAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> BloodDarkColorScheme
        else -> BloodLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = BloodTypography,
        content = content
    )
}

@Composable
fun CameraXTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) CameraXDarkColorScheme else CameraXLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CameraXTypography,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun CameraXPreview() {
    CameraXTheme {
    }
}

@Preview(showBackground = true)
@Composable
fun BloodAppPreview() {
    BloodAppTheme {
    }
}
