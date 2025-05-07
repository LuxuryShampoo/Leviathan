package shampoo.luxury.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.Navigator
import shampoo.luxury.components.Butext
import shampoo.luxury.components.FileImage
import shampoo.luxury.components.NavBar
import shampoo.luxury.io.Resource.BOB_ALARM
import shampoo.luxury.io.Resource.downloadFile
import shampoo.luxury.io.Resource.getLocalResourcePath
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
    ) {
    }
}

@Composable
private fun MarketBox() {
    val imageFiles = remember { mutableStateListOf<File>() }
    val startIndex = MAX_VALUE / 2
    val listState = rememberLazyListState(startIndex)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val fileList =
            listOf(
                BOB_ALARM to getLocalResourcePath("BobAlarm1.png"),
                BOB_ALARM to getLocalResourcePath("BobAlarm2.png"),
                BOB_ALARM to getLocalResourcePath("BobAlarm3.png"),
                BOB_ALARM to getLocalResourcePath("BobAlarm4.png"),
            )
        fileList.forEach {
            downloadFile(it.first, it.second).apply {
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

            Butext(
                "<",
                {
                    align(CenterStart)
                        .padding(start = 16.dp)
                        .size(50.dp)
                        .alpha(0.5f)
                },
            ) {
                coroutineScope.launch {
                    listState.animateScrollToItem(listState.firstVisibleItemIndex - 1)
                }
            }

            Butext(
                ">",
                {
                    align(CenterEnd)
                        .padding(end = 16.dp)
                        .size(50.dp)
                        .alpha(0.5f)
                },
            ) {
                coroutineScope.launch {
                    listState.animateScrollToItem(listState.firstVisibleItemIndex + 1)
                }
            }
        }
    }
}
