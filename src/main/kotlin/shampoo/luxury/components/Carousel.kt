package shampoo.luxury.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import kotlin.Int.Companion.MAX_VALUE

@Composable
fun Carousel(
    listState: LazyListState,
    imageFiles: SnapshotStateList<File>,
) {
    LazyRow(
        Modifier.fillMaxWidth(),
        listState,
        horizontalArrangement = Arrangement.spacedBy(30.dp),
    ) {
        items(MAX_VALUE) { index ->
            val wrappedIndex = index % imageFiles.size
            val file = imageFiles[wrappedIndex]
            if (file.exists()) {
                FileImage(
                    file,
                    "Pet Carousel",
                ) { fillMaxSize(0.9f) }
            }
        }
    }
}

@Composable
fun createCarouselButton(
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
                .size(50.dp)
                .alpha(0.5f)
        },
    ) {
        coroutineScope.launch { onClick() }
    }
}
