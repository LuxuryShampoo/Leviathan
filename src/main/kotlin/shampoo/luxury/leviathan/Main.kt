package shampoo.luxury.leviathan

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition.Aligned
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import shampoo.luxury.leviathan.components.AppRoot
import shampoo.luxury.leviathan.theme.ThemeManager
import shampoo.luxury.leviathan.theme.ThemeManager.DEFAULT_THEME
import shampoo.luxury.leviathan.theme.ThemeManager.themeInputStream
import shampoo.luxury.leviathan.wrap.Whisper
import shampoo.luxury.leviathan.wrap.setupTrayIcon
import xyz.malefic.compose.comps.precompose.NavWindow
import xyz.malefic.compose.nav.RouteManager
import xyz.malefic.compose.nav.config.MalefiConfigLoader
import xyz.malefic.compose.theming.MaleficTheme
import xyz.malefic.ext.stream.grass
import java.awt.Toolkit.getDefaultToolkit

fun main() =
    application {
        val scope = CoroutineScope(IO)
        Whisper.initialize("hello")
        setupTrayIcon(scope)
        val screenSize = getDefaultToolkit().screenSize
        val windowWidth = (screenSize.width * 0.3).dp
        val windowHeight = (screenSize.height * 0.7).dp

        NavWindow(
            ::exitApplication,
            rememberWindowState(
                size = DpSize(windowWidth, windowHeight),
                position = Aligned(BottomEnd),
            ),
            title = "Leviathan",
        ) {
            AppRoot {
                RouteManager.initialize(
                    composableMap,
                    grass("/routes.mcf")!!,
                    MalefiConfigLoader(),
                )

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
                        NavigationMenu()
                    }
                } ?: ThemeManager.updateTheme(DEFAULT_THEME)
            }
        }
    }
