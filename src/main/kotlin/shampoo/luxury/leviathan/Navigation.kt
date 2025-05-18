package shampoo.luxury.leviathan

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import shampoo.luxury.leviathan.screens.Achievements
import shampoo.luxury.leviathan.screens.Home
import shampoo.luxury.leviathan.screens.Settings
import shampoo.luxury.leviathan.screens.Shop
import shampoo.luxury.leviathan.screens.Tasks
import xyz.malefic.compose.engine.factory.ColumnFactory
import xyz.malefic.compose.engine.factory.div
import xyz.malefic.compose.engine.factory.timesAssign
import xyz.malefic.compose.engine.fuel.background
import xyz.malefic.compose.engine.fuel.center
import xyz.malefic.compose.nav.RouteManager.RoutedNavHost

/**
 * Composable function that defines the navigation menu layout. It includes a sidebar and a
 * content area separated by a divider.
 */
@Composable
fun NavigationMenu() {
    ColumnFactory {
        RoutedNavHost()
    } / {
        modifier = Modifier.fillMaxSize()
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
        "Home" to { _ -> Home() },
        "Settings" to { _ -> Settings() },
        "Shop" to { _ -> Shop() },
        "Tasks" to { _ -> Tasks() },
        "Achievements" to { _ -> Achievements() },
    )
