package shampoo.luxury.leviathan.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import shampoo.luxury.leviathan.components.PageScope
import xyz.malefic.compose.comps.text.typography.Heading4

@Composable
fun Tasks() =
    PageScope {
        Box(
            Modifier
                .fillMaxSize(),
            Alignment.Center,
        ) {
            Heading4("Tasks Placeholder Page")
        }
    }
