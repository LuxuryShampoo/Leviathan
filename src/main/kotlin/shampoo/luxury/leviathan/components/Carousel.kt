package shampoo.luxury.leviathan.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.DollarSign
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import xyz.malefic.compose.comps.text.typography.Body1
import java.io.File
import kotlin.Int.Companion.MAX_VALUE

/**
 * A composable function that displays a horizontally scrollable carousel of images.
 *
 * @param listState The state of the lazy list, used to control and observe the scroll position.
 * @param imageFiles A list of image files to be displayed in the carousel.
 */
@Composable
fun Carousel(
    listState: LazyListState,
    imageFiles: SnapshotStateList<File>,
) {
    LazyRow(
        Modifier.fillMaxWidth(),
        listState,
        horizontalArrangement = Arrangement.spacedBy(30.dp),
        contentPadding = PaddingValues(horizontal = 100.dp),
    ) {
        items(MAX_VALUE) { index ->
            val wrappedIndex = index % imageFiles.size
            val file = imageFiles[wrappedIndex]
            if (file.exists()) {
                val isFocused = index == listState.firstVisibleItemIndex
                val scale by animateFloatAsState(targetValue = if (isFocused) 1.25f else 1f)

                FileImage(
                    file,
                    "Pet Carousel",
                ) { fillMaxSize(0.9f).scale(scale) }
            }
        }
    }
}

/**
 * A composable function that creates a button for the carousel with custom alignment and click behavior.
 *
 * @param text The text to display on the button.
 * @param coroutineScope The coroutine scope used to launch the click action.
 * @param alignment A lambda to define the alignment of the button.
 * @param onClick A suspend function to execute when the button is clicked.
 */
@Composable
fun CarouselButton(
    text: String,
    coroutineScope: CoroutineScope,
    alignment: Modifier.() -> Modifier,
    onClick: suspend () -> Unit,
) {
    Butext(
        text,
        {
            alignment()
                .padding(horizontal = 16.dp)
                .size(25.dp, 50.dp)
                .alpha(0.5f)
        },
    ) {
        coroutineScope.launch { onClick() }
    }
}

/**
 * A composable function that displays the cost of the focused pet in the carousel.
 *
 * @param focusedPetCost The cost of the currently focused pet, displayed as a `Double`.
 */
@Composable
fun BoxScope.CarouselCost(focusedPetCost: Double) {
    Box(
        Modifier
            .align(BottomCenter)
            .fillMaxWidth(0.3f)
            .padding(4.dp)
            .background(Black.copy(alpha = 0.6f))
            .padding(4.dp),
        Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                FontAwesomeIcons.Solid.DollarSign,
                "Currency Icon",
                Modifier.size(16.dp),
                Color.White,
            )
            Body1(focusedPetCost.toString(), Modifier.padding(start = 4.dp))
        }
    }
}
