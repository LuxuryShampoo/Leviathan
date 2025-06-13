package shampoo.luxury.leviathan.components.shop

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.DollarSign
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import shampoo.luxury.leviathan.components.Butext
import shampoo.luxury.leviathan.components.FileImage
import shampoo.luxury.leviathan.wrap.data.pets.Pet
import xyz.malefic.compose.comps.text.typography.Body1
import xyz.malefic.compose.comps.text.typography.ColorType.OnPrimary
import java.io.File

/**
 * Displays a horizontal carousel of images using a `LazyRow`.
 *
 * @param listState The state object to control and observe the scroll position of the carousel.
 * @param imageFiles A list of image files to display in the carousel.
 *
 * Each image is scaled up if it is the currently focused (first visible) item.
 * Only existing files are displayed.
 */
@Composable
fun Carousel(
    listState: LazyListState,
    imageFiles: SnapshotStateList<File>,
) {
    LazyRow(
        Modifier.fillMaxWidth(),
        listState,
        PaddingValues(horizontal = 130.dp),
        horizontalArrangement = Arrangement.spacedBy(30.dp),
    ) {
        items(imageFiles.size) { index ->
            val file = imageFiles[index]
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
 * @param focusedPet The currently focused pet, which may be null.
 */
@Composable
fun BoxScope.CarouselCost(focusedPet: Pet?) {
    focusedPet?.let {
        Surface(
            Modifier
                .align(Alignment.BottomStart)
                .padding(start = 24.dp, bottom = 8.dp)
                .fillMaxWidth(0.3f),
            MaterialTheme.shapes.medium,
            MaterialTheme.colors.primary.copy(alpha = 0.85f),
        ) {
            Row(
                Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    FontAwesomeIcons.Solid.DollarSign,
                    "Currency",
                    Modifier.size(16.dp),
                    MaterialTheme.colors.onPrimary,
                )
                Body1(
                    focusedPet.cost.toString(),
                    Modifier.padding(start = 4.dp),
                    OnPrimary,
                )
            }
        }
    }
}
