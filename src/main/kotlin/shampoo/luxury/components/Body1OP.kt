package shampoo.luxury.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A composable function that displays a body text with Body1 typography style.
 *
 * @param text The text to be displayed.
 * @param modifier The modifier to be applied to the Text composable.
 */
@Composable
fun Body1OP(
    text: String,
    modifier: Modifier = Modifier,
) = Text(
    text = text,
    style = typography.body1,
    color = MaterialTheme.colors.onPrimary,
    modifier = modifier,
)

/**
 * A composable function that displays a body text with Body1 typography style.
 *
 * @param text The text to be displayed.
 * @param modifier The modifier to be applied to the Text composable.
 */
@Composable
fun Body1OS(
    text: String,
    modifier: Modifier = Modifier,
) = Text(
    text = text,
    style = typography.body1,
    color = MaterialTheme.colors.onSecondary,
    modifier = modifier,
)
