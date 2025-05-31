package shampoo.luxury.leviathan.components

import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import compose.icons.fontawesomeicons.solid.Cog
import compose.icons.fontawesomeicons.solid.Home
import compose.icons.fontawesomeicons.solid.Store
import shampoo.luxury.leviathan.global.GlobalLoadingState.navigate
import xyz.malefic.compose.nav.RouteManager.navi

/**
 * A composable function that represents a navigation bar with three buttons: Home, Shop, and Settings.
 *
 * @param navi The `Navigator` instance used to handle navigation between different screens.
 */
@Composable
fun NavBar() {
    val currentEntry by navi.currentEntry.collectAsState(null)
    val currentRoute = currentEntry?.route?.route

    Row(
        Modifier.fillMaxHeight().fillMaxWidth(),
        SpaceEvenly,
        CenterVertically,
    ) {
        Buicon({ Home }, "Home") { if (currentRoute != "home") navigate("home") }
        Buicon({ Store }, "Shop") { if (currentRoute != "shop") navigate("shop") }
        Buicon({ Cog }, "Settings") { if (currentRoute != "settings") navigate("settings") }
    }
}
