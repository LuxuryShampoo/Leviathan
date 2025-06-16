package shampoo.luxury.leviathan.components

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import xyz.malefic.compose.comps.box.BackgroundBox

@Composable
fun MaxLoading(modifier: Modifier = Modifier) {
    BackgroundBox(
        modifier = modifier,
        contentAlignment = Center,
    ) {
        CircularProgressIndicator(strokeCap = Round, color = MaterialTheme.colors.onBackground)
    }
}
