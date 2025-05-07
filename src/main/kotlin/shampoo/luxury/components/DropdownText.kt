package shampoo.luxury.components

import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.Composable

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
        Body1OS(string)
    }
}
