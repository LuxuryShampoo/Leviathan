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
import shampoo.luxury.leviathan.global.Values.allPets
import shampoo.luxury.leviathan.global.Values.unownedPets
import xyz.malefic.compose.comps.text.typography.Heading3
import java.io.File
import kotlin.Int.Companion.MAX_VALUE

@Composable
fun Shop() =
    PageScope {
        var focusedPetName by remember { mutableStateOf("") }

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
    val startIndex = MAX_VALUE / 2
    val listState = rememberLazyListState(startIndex)
    val coroutineScope = rememberCoroutineScope()
    var focusedPetCost by remember { mutableStateOf(0.0) }

    LaunchedEffect(Unit) {
        unownedPets.forEach {
            File(it.localPath).apply {
                if (exists()) {
                    imageFiles.add(this)
                }
            }
        }
    }

    LaunchedEffect(listState.firstVisibleItemIndex) {
        if (imageFiles.isNotEmpty()) {
            val focusedIndex = listState.firstVisibleItemIndex % imageFiles.size
            val focusedPet = allPets.find { it.localPath == imageFiles[focusedIndex].absolutePath }
            focusedPet?.let {
                onFocusChange(it.name)
                focusedPetCost = it.cost
            }
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
                listState.animateScrollToItem(listState.firstVisibleItemIndex - 1)
            }

            CarouselButton(">", coroutineScope, { align(CenterEnd) }) {
                listState.animateScrollToItem(listState.firstVisibleItemIndex + 1)
            }

            CarouselCost(focusedPetCost)
        }
    }
}
