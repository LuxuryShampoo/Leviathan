package shampoo.luxury.components

import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.Composable
import xyz.malefic.compose.comps.text.typography.Body1

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
        Body1(string)
    }
}
