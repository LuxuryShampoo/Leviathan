package shampoo.luxury.leviathan.components.layouts

import androidx.compose.runtime.*
import co.touchlab.kermit.Logger
import shampoo.luxury.leviathan.theme.ThemeManager
import shampoo.luxury.leviathan.theme.ThemeManager.DEFAULT_THEME
import shampoo.luxury.leviathan.theme.ThemeManager.themeInputStream
import xyz.malefic.compose.theming.MaleficTheme

@Composable
fun ThemedApp(content: @Composable () -> Unit) {
    var themeChangeCount by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        ThemeManager.themeChanges.connect { _ ->
            themeChangeCount++
        }
    }

    val themeStream by remember(themeChangeCount) { mutableStateOf(themeInputStream) }

    themeStream?.let { stream ->
        Logger.d("Recomposing with theme stream: $stream")
        MaleficTheme(stream) {
            content()
        }
    } ?: ThemeManager.updateTheme(DEFAULT_THEME)
}
