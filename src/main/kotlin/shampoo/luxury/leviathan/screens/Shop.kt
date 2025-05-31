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
import shampoo.luxury.leviathan.global.GlobalLoadingState.addLoading
import shampoo.luxury.leviathan.global.GlobalLoadingState.removeLoading
import shampoo.luxury.leviathan.wrap.data.pets.Pet
import shampoo.luxury.leviathan.wrap.data.pets.getUnownedPets
import xyz.malefic.compose.comps.text.typography.Heading3
import java.io.File

@Composable
fun Shop() =
    PageScope {
        var focusedPetName by remember { mutableStateOf("Loading...") }

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
    var sortedUnownedPets by remember { mutableStateOf<List<Pet>>(emptyList()) }
    var focusedPet by remember { mutableStateOf<Pet?>(null) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        addLoading("shop market load")
        removeLoading("navigation to shop")
        val unownedPets = getUnownedPets()
        sortedUnownedPets = unownedPets.sortedBy { it.cost }
        imageFiles.clear()
        imageFiles.addAll(
            sortedUnownedPets
                .filter { File(it.localPath).exists() }
                .map { File(it.localPath) },
        )
        focusedPet = sortedUnownedPets.firstOrNull()
        onFocusChange(focusedPet?.name ?: "Loading...")
        removeLoading("shop market load")
    }

    LaunchedEffect(listState.firstVisibleItemIndex) {
        focusedPet = sortedUnownedPets.getOrNull(listState.firstVisibleItemIndex)
        focusedPet?.let {
            onFocusChange(it.name)
        }
    }

    if (sortedUnownedPets.isEmpty()) {
        Box(
            Modifier
                .fillMaxHeight(0.6f)
                .fillMaxWidth(),
            Center,
        ) {
            Heading3("All Gone!")
        }
        onFocusChange("Woah!")
        return
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

            CarouselButton("<", scope, { align(CenterStart) }) {
                val prevIndex = (listState.firstVisibleItemIndex - 1 + imageFiles.size) % imageFiles.size
                listState.animateScrollToItem(prevIndex)
            }

            CarouselButton(">", scope, { align(CenterEnd) }) {
                val nextIndex = (listState.firstVisibleItemIndex + 1) % imageFiles.size
                listState.animateScrollToItem(nextIndex)
            }

            CarouselCost(focusedPet)
        }
    }
}
