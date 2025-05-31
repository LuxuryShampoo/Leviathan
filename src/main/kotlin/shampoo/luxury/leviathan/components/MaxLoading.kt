package shampoo.luxury.leviathan.components

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.zIndex
import xyz.malefic.compose.comps.box.BackgroundBox

@Composable
fun MaxLoading() {
    BackgroundBox(
        Modifier.zIndex(1f),
        contentAlignment = Center,
    ) {
        CircularProgressIndicator(strokeCap = Round)
    }
}
