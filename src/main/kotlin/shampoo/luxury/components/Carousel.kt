package shampoo.luxury.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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
                FileImage(
                    file,
                    "Pet Carousel",
                ) { fillMaxSize(0.9f).scale(if (index == listState.firstVisibleItemIndex) 1.2f else 1f) }
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
