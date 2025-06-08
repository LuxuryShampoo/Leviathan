package shampoo.luxury.leviathan.components.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import shampoo.luxury.leviathan.components.nav.NavBar

/**
 * A composable function that defines the layout for a page in the application.
 *
 * @param onDispose A lambda to execute cleanup logic when the page is disposed.
 * @param content A composable lambda that represents the main content of the page.
 *                This content is displayed above the divider and navigation bar.
 */
@Composable
fun PageScope(
    onDispose: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    onDispose?.let { disposeAction ->
        DisposableEffect(Unit) {
            onDispose {
                disposeAction()
            }
        }
    }

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
        NavBar()
    }
}
