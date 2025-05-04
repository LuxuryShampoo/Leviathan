package xyz.malefic.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.application
import xyz.malefic.compose.comps.precompose.NavWindow
import xyz.malefic.compose.comps.text.typography.Heading1
import xyz.malefic.compose.engine.factory.RowFactory
import xyz.malefic.compose.engine.factory.div
import xyz.malefic.compose.engine.factory.timesAssign
import xyz.malefic.compose.engine.fuel.background
import xyz.malefic.compose.engine.fuel.center
import xyz.malefic.compose.engine.fuel.divide
import xyz.malefic.compose.engine.fuel.fuel
import xyz.malefic.compose.nav.RouteManager
import xyz.malefic.compose.nav.RouteManager.RoutedNavHost
import xyz.malefic.compose.nav.RouteManager.RoutedSidebar
import xyz.malefic.compose.nav.RouteManager.navi
import xyz.malefic.compose.nav.config.MalefiConfigLoader
import xyz.malefic.compose.screens.App1
import xyz.malefic.compose.screens.Home
import xyz.malefic.compose.theming.MaleficTheme
import xyz.malefic.ext.list.get
import xyz.malefic.ext.stream.grass

/**
 * Entry point of the application that sets up the main navigation window.
 * It determines the theme based on the system's current theme (dark or light),
 * applies the selected theme, and initializes the route manager with the
 * composable map and configuration loader. The navigation menu is then displayed.
 */
fun main() =
    application {
        NavWindow(onCloseRequest = ::exitApplication, title = "Compose Desktop Template") {
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

/**
 * Composable function that defines the navigation menu layout. It includes a sidebar and a
 * content area separated by a divider.
 */
@Composable
fun NavigationMenu() {
    RowFactory {
        fuel { RoutedSidebar() }.divide()()
        RoutedNavHost()
    } / {
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    } *= {
        center()
        background()
    }
}

/**
 * A map of composable functions used for routing. Each entry maps a route name to a composable
 * function that takes a list of parameters.
 */
val composableMap: Map<String, @Composable (List<String?>) -> Unit> =
    mapOf(
        "App1" to { params -> App1(id = params[0]!!, name = params[1, null]) },
        "Home" to { _ -> Home(navi) },
        "Text" to { params -> Heading1(text = params[0, "Nope."]) },
    )
