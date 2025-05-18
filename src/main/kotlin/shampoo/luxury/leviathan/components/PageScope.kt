package shampoo.luxury.leviathan.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import xyz.malefic.compose.nav.RouteManager.navi

/**
 * A composable function that defines the layout for a page in the application.
 *
 * @param content A composable lambda that represents the main content of the page.
 *                This content is displayed above the divider and navigation bar.
 */
@Composable
fun PageScope(content: @Composable () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = CenterHorizontally,
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
        ) {
            content()
        }
        Divider()
        NavBar(navi)
    }
}
