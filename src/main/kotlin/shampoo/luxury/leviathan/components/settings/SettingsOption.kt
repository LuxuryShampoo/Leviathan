package shampoo.luxury.leviathan.components.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import xyz.malefic.compose.comps.text.typography.Heading3

/**
 * A composable function that represents a settings option row.
 *
 * This function displays a name (label) on the left and a custom composable content
 * (e.g., a switch or other UI element) on the right, arranged horizontally.
 *
 * @param name The label for the settings option.
 * @param content A composable lambda that defines the content to be displayed
 *                on the right side of the row.
 */
@Composable
fun SettingsOption(
    name: String,
    content: @Composable () -> Unit,
) = Row(
    Modifier.fillMaxWidth(),
    Arrangement.SpaceBetween,
    Alignment.CenterVertically,
) {
    Heading3(name)
    content()
}
