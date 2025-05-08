package shampoo.luxury.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.navigation.Navigator
import shampoo.luxury.components.NavBar
import shampoo.luxury.components.SettingsOption
import shampoo.luxury.theme.ThemeSelector
import xyz.malefic.compose.comps.switch.BooleanSwitch
import xyz.malefic.compose.comps.text.typography.Heading1
import xyz.malefic.compose.prefs.delegate.BooleanPreference

@Composable
fun Settings(navi: Navigator) {
    val listState = rememberLazyListState()
    var speakPreference by BooleanPreference("speak_enabled", true)
    var listenPreference by BooleanPreference("listen_enabled", true)
    val speak = remember { mutableStateOf(speakPreference) }
    val listen = remember { mutableStateOf(listenPreference) }

    LaunchedEffect(speak.value) {
        speakPreference = speak.value
        listenPreference = listen.value
    }

    val gradientAlpha by animateFloatAsState(
        if (listState.canScrollForward) 1f else 0f,
        tween(durationMillis = 1000),
    )

    Column {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        ) {
            Heading1("Settings")
            Divider(Modifier.padding(top = 16.dp))
        }

        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
        ) {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp),
                listState,
            ) {
                item {
                    Spacer(Modifier.height(8.dp))

                    SettingsOption("Theme") {
                        ThemeSelector()
                    }

                    Divider(Modifier.padding(vertical = 16.dp))

                    SettingsOption("Speak") {
                        BooleanSwitch(speak)
                    }

                    Divider(Modifier.padding(vertical = 16.dp))

                    SettingsOption("Listen") {
                        BooleanSwitch(listen)
                    }

                    Spacer(Modifier.height(8.dp))
                }
            }

            if (listState.canScrollForward) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .align(Alignment.BottomCenter),
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawRect(
                            brush =
                                Brush.verticalGradient(
                                    colors =
                                        listOf(
                                            Transparent,
                                            Transparent,
                                            Black.copy(alpha = 0.2f * gradientAlpha),
                                        ),
                                ),
                        )
                    }
                }
            }
        }

        Divider()
        NavBar(navi)
    }
}
