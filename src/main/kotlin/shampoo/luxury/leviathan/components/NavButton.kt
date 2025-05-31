package shampoo.luxury.leviathan.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import shampoo.luxury.leviathan.global.GlobalLoadingState.navigate

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
) = Button({ navigate(target) }, content = content)
