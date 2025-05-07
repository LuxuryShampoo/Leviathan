package shampoo.luxury.screens

import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.navigation.Navigator
import shampoo.luxury.components.Carousel
import shampoo.luxury.components.NavBar
import shampoo.luxury.components.createCarouselButton
import shampoo.luxury.io.Resource.BOB_ALARM
import shampoo.luxury.io.Resource.downloadFile
import shampoo.luxury.io.Resource.getLocalResourcePath
import shampoo.luxury.pet.Pet
import java.io.File
import kotlin.Int.Companion.MAX_VALUE

@Composable
fun Shop(navi: Navigator) {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = CenterHorizontally,
    ) {
        TopRow()
        Divider()
        MarketBox()
        Divider()
        NavBar(navi)
    }
}

@Composable
private fun TopRow() {
    Row(
        Modifier
            .fillMaxHeight(0.2f)
            .fillMaxWidth(),
        SpaceEvenly,
        CenterVertically,
    ) {}
}

@Composable
private fun MarketBox() {
    val imageFiles = remember { mutableStateListOf<File>() }
    val startIndex = MAX_VALUE / 2
    val listState = rememberLazyListState(startIndex)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        listOf(
            Pet("BobAlarm", BOB_ALARM, getLocalResourcePath("BobAlarm.png")),
            Pet("BobAlarm1", BOB_ALARM, getLocalResourcePath("BobAlarm1.png")),
            Pet("BobAlarm2", BOB_ALARM, getLocalResourcePath("BobAlarm2.png")),
            Pet("BobAlarm3", BOB_ALARM, getLocalResourcePath("BobAlarm3.png")),
            Pet("BobAlarm4", BOB_ALARM, getLocalResourcePath("BobAlarm4.png")),
        ).forEach {
            downloadFile(it.url, it.local).apply {
                if (exists()) {
                    imageFiles.add(this)
                }
            }
        }
    }

    Box(
        Modifier
            .fillMaxHeight(0.75f)
            .fillMaxWidth(),
        Center,
    ) {
        if (imageFiles.isNotEmpty()) {
            Carousel(listState, imageFiles)

            createCarouselButton("<", coroutineScope, { align(CenterStart) }) {
                listState.animateScrollToItem(listState.firstVisibleItemIndex - 1)
            }

            createCarouselButton(">", coroutineScope, { align(CenterEnd) }) {
                listState.animateScrollToItem(listState.firstVisibleItemIndex + 1)
            }
        }
    }
}
