package xyz.malefic.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import xyz.malefic.compose.comps.precompose.NavWindow
import xyz.malefic.compose.nav.RouteManager
import xyz.malefic.compose.nav.config.MalefiConfigLoader
import xyz.malefic.compose.theming.MaleficTheme
import xyz.malefic.ext.stream.grass

/**
 * Entry point of the application that sets up the main navigation window.
 * It determines the theme based on the system's current theme (dark or light),
 * applies the selected theme, and initializes the route manager with the
 * composable map and configuration loader. The navigation menu is then displayed.
 */
fun main() =
    application {
        NavWindow(onCloseRequest = ::exitApplication, state = rememberWindowState(size = DpSize(500.dp, 600.dp)), title = "Leviathan") {
            // Initialize the route manager
            RouteManager.initialize(
                composableMap,
                grass("/routes.mcf")!!,
                MalefiConfigLoader(),
            )

            // Determine the theme file path based on the system's theme (dark or light)
            val themeInputStream =
                grass(
                    if (isSystemInDarkTheme()) "/theme/dark.json" else "/theme/light.json",
                ) ?: throw IllegalArgumentException("Theme file not found")

            // Apply the selected theme and invoke the Navigation Menu
            MaleficTheme(themeInputStream) {
                NavigationMenu()
            }
        }
    }
