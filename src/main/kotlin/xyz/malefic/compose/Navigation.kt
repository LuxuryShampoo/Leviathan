package xyz.malefic.compose

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import xyz.malefic.compose.comps.text.typography.Heading1
import xyz.malefic.compose.engine.factory.ColumnFactory
import xyz.malefic.compose.engine.factory.div
import xyz.malefic.compose.engine.factory.timesAssign
import xyz.malefic.compose.engine.fuel.background
import xyz.malefic.compose.engine.fuel.center
import xyz.malefic.compose.nav.RouteManager.RoutedNavHost
import xyz.malefic.compose.nav.RouteManager.navi
import xyz.malefic.compose.screens.App1
import xyz.malefic.compose.screens.Home
import xyz.malefic.compose.screens.HomeScreen
import xyz.malefic.ext.list.get

/**
 * Composable function that defines the navigation menu layout. It includes a sidebar and a
 * content area separated by a divider.
 */
@Composable
fun NavigationMenu() {
    ColumnFactory {
        RoutedNavHost()
    }.div {
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    }.timesAssign {
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
        "HomeScreen" to { _ -> HomeScreen(navi) },
    )
