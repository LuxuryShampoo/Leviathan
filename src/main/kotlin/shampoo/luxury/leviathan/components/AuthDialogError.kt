package shampoo.luxury.leviathan.components

import androidx.compose.runtime.Composable
import xyz.malefic.compose.comps.text.typography.Body2
import xyz.malefic.compose.comps.text.typography.ColorType

/**
 * A composable function that displays an error message in the dialog.
 *
 * @param errorMessage The error message to display.
 */
@Composable
fun AuthDialogError(errorMessage: String) {
    Body2(
        errorMessage,
        colorType = ColorType.Error,
    )
}
