package shampoo.luxury.leviathan.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap.Companion.Round

@Composable
fun MaxLoading() {
    Box(Modifier.fillMaxSize(), Center) {
        CircularProgressIndicator(strokeCap = Round)
    }
}
