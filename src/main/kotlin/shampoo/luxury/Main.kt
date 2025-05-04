package shampoo.luxury

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition.Aligned
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
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

            val themeInputStream =
                grass(
                    if (isSystemInDarkTheme()) "/theme/dark.json" else "/theme/grassy.json",
                ) ?: throw IllegalArgumentException("Theme file not found")

            MaleficTheme(themeInputStream) {
                NavigationMenu()
            }
        }
    }
