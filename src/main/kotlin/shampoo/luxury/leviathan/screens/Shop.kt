package shampoo.luxury.leviathan.screens

import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import shampoo.luxury.leviathan.components.Carousel
import shampoo.luxury.leviathan.components.CarouselButton
import shampoo.luxury.leviathan.components.CarouselCost
import shampoo.luxury.leviathan.components.PageScope
import shampoo.luxury.leviathan.wrap.data.pets.unownedPets
import xyz.malefic.compose.comps.text.typography.Heading3
import java.io.File

@Composable
fun Shop() =
    PageScope {
        var focusedPetName by remember { mutableStateOf("Woah!") }

        TopRow(focusedPetName)
        Divider()
        MarketBox { focusedPetName = it }
    }

@Composable
private fun TopRow(focusedPetName: String) {
    Row(
        Modifier
            .fillMaxHeight(0.2f)
            .fillMaxWidth(),
        SpaceEvenly,
        CenterVertically,
    ) {
        Heading3(focusedPetName)
    }
}

@Composable
private fun MarketBox(onFocusChange: (String) -> Unit) {
    val imageFiles = remember { mutableStateListOf<File>() }
    val sortedUnownedPets = remember { unownedPets.sortedBy { it.cost } }
    var focusedPet by remember { mutableStateOf(sortedUnownedPets.firstOrNull()) }
    val coroutineScope = rememberCoroutineScope()

    if (sortedUnownedPets.isEmpty()) {
        Box(
            Modifier
                .fillMaxHeight(0.6f)
                .fillMaxWidth(),
            Center,
        ) {
            Heading3("All Gone!")
        }
        return
    }

    LaunchedEffect(Unit) {
        imageFiles.addAll(
            sortedUnownedPets
                .filter { File(it.localPath).exists() }
                .map { File(it.localPath) },
        )
    }

    val listState = rememberLazyListState()

    LaunchedEffect(listState.firstVisibleItemIndex) {
        focusedPet = sortedUnownedPets.getOrNull(listState.firstVisibleItemIndex)
        focusedPet?.let {
            onFocusChange(it.name)
        }
    }

    Box(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        Center,
    ) {
        if (imageFiles.isNotEmpty()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f),
                Center,
            ) {
                Carousel(listState, imageFiles)
            }

            CarouselButton("<", coroutineScope, { align(CenterStart) }) {
                val prevIndex = (listState.firstVisibleItemIndex - 1 + imageFiles.size) % imageFiles.size
                listState.animateScrollToItem(prevIndex)
            }

            CarouselButton(">", coroutineScope, { align(CenterEnd) }) {
                val nextIndex = (listState.firstVisibleItemIndex + 1) % unownedPets.size
                listState.animateScrollToItem(nextIndex)
            }

            CarouselCost(focusedPet)
        }
    }
}
