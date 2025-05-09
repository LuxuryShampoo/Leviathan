package shampoo.luxury.leviathan.components

import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.Composable
import xyz.malefic.compose.comps.text.typography.Body1
import xyz.malefic.compose.comps.text.typography.ColorType.OnSecondary

/**
 * A composable function that represents a dropdown menu item with text.
 *
 * @param string The text to display in the dropdown menu item.
 * @param onClick A lambda function to be executed when the dropdown menu item is clicked.
 */
@Composable
fun DropdownText(
    string: String,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        onClick = {
            onClick()
        },
    ) {
        Body1(string, colorType = OnSecondary)
    }
}
