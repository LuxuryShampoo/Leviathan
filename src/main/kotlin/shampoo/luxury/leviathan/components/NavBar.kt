package shampoo.luxury.leviathan.components

import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import compose.icons.fontawesomeicons.solid.Cog
import compose.icons.fontawesomeicons.solid.Home
import compose.icons.fontawesomeicons.solid.Store
import moe.tlaster.precompose.navigation.Navigator
import xyz.malefic.ext.precompose.gate

/**
 * A composable function that represents a navigation bar with three buttons: Home, Shop, and Settings.
 *
 * @param navi The `Navigator` instance used to handle navigation between different screens.
 */
@Composable
fun NavBar(navi: Navigator) {
    Row(
        Modifier.fillMaxHeight().fillMaxWidth(),
        SpaceEvenly,
        CenterVertically,
    ) {
        Buicon({ Home }, "Home") { navi gate "home" }
        Buicon({ Store }, "Shop") { navi gate "shop" }
        Buicon({ Cog }, "Settings") { navi gate "settings" }
    }
}
