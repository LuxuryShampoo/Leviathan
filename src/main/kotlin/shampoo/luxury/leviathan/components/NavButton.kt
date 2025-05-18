package shampoo.luxury.leviathan.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import xyz.malefic.compose.nav.RouteManager.navi
import xyz.malefic.ext.precompose.gate

/**
 * A composable function that creates a navigation button.
 *
 * @param target The target route to navigate to when the button is clicked.
 * @param content A composable lambda that defines the content of the button.
 */
@Composable
fun NavButton(
    target: String,
    content: @Composable (RowScope.() -> Unit),
) = Button({ navi gate target }, content = content)
