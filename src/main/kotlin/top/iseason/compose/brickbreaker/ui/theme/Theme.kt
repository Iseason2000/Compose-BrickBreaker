package top.iseason.compose.brickbreaker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//暗色配色
private val DarkColorPalette = darkColors(
    primary = Color(137, 137, 137),
    primaryVariant = Color(255, 114, 153),
    secondary = Color(241, 250, 253),
    secondaryVariant = Color(0xFF018786),
    background = Color(12, 17, 23)
)

//亮色配色
private val LightColorPalette = lightColors(
    primary = Color(0, 176, 226),
    primaryVariant = Color(255, 114, 153),
    secondary = Color(241, 250, 253),
    secondaryVariant = Color(0xFF018786),
    background = Color.White
)

@Composable
fun MyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkColorPalette else LightColorPalette,
        content = content
    )
}