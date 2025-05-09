package shampoo.luxury.leviathan.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import xyz.malefic.compose.comps.text.typography.Body1
import xyz.malefic.compose.comps.text.typography.ColorType.OnSecondary

/**
 * A composable function that creates a button with text content.
 *
 * @param content The text to be displayed on the button.
 * @param modifier A lambda to apply additional modifiers to the button.
 * @param onClick A lambda function to be executed when the button is clicked.
 */
@Composable
fun Butext(
    content: String,
    modifier: Modifier.() -> Modifier = { Modifier },
    onClick: () -> Unit,
) = Button(
    onClick,
    Modifier.modifier(),
    contentPadding = PaddingValues(horizontal = 8.dp),
) { Body1(content, colorType = OnSecondary) }
