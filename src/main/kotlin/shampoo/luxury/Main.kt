package shampoo.luxury

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
import shampoo.luxury.theme.ThemeManager
import xyz.malefic.compose.comps.precompose.NavWindow
import xyz.malefic.compose.nav.RouteManager
import xyz.malefic.compose.nav.config.MalefiConfigLoader
import xyz.malefic.compose.theming.MaleficTheme
import xyz.malefic.ext.stream.grass
import java.awt.Toolkit

fun main() =
    application {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val windowWidth = (screenSize.width * 0.3).dp
        val windowHeight = (screenSize.height * 0.7).dp

        NavWindow(
            onCloseRequest = ::exitApplication,
            state =
                rememberWindowState(
                    size = DpSize(windowWidth, windowHeight),
                    position = Aligned(BottomEnd),
                ),
            title = "Leviathan",
        ) {
            RouteManager.initialize(
                composableMap,
                grass("/routes.mcf")!!,
                MalefiConfigLoader(),
            )

            // Initialize the ThemeManager with the system theme if it hasn't been initialized yet
            LaunchedEffect(Unit) {
                val systemThemePath = "/theme/grassy.json"
                ThemeManager.updateTheme(systemThemePath)
            }

            // Use remember to create a mutableState for tracking theme changes
            var themeChangeCount by remember { mutableStateOf(0) }

            // Use LaunchedEffect to connect to the themeChanges Signal
            LaunchedEffect(Unit) {
                // Connect to the Signal and update themeChangeCount when a new value is emitted
                ThemeManager.themeChanges.connect { _ ->
                    themeChangeCount++
                }
            }

            // Use remember to observe the ThemeManager's themeInputStream
            // The key parameter ensures that the state is updated when themeChangeCount changes
            val themeStream by remember(themeChangeCount) { mutableStateOf(ThemeManager.themeInputStream) }

            // Use the observed themeStream
            themeStream?.let { stream ->
                println("Recomposing with theme stream: $stream")
                MaleficTheme(stream) {
                    NavigationMenu()
                }
            } ?: throw IllegalArgumentException("Theme file not found")
        }
    }
