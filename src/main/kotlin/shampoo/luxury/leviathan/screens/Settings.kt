package shampoo.luxury.leviathan.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import shampoo.luxury.leviathan.components.BooleanSetting
import shampoo.luxury.leviathan.components.PageScope
import shampoo.luxury.leviathan.global.Values.Prefs.listenSetting
import shampoo.luxury.leviathan.global.Values.Prefs.speakSetting
import xyz.malefic.compose.comps.text.typography.Heading1

@Composable
fun Settings() {
    var localSpeakSetting by remember { mutableStateOf(speakSetting) }
    var localListenSetting by remember { mutableStateOf(listenSetting) }

    val saveSettings = {
        speakSetting = localSpeakSetting
        listenSetting = localListenSetting
    }

    PageScope(saveSettings) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        ) {
            Heading1("Settings")
            Divider(Modifier.padding(top = 16.dp))
        }

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp),
        ) {
            item {
                Spacer(Modifier.height(8.dp))

                BooleanSetting("Speak", localSpeakSetting) { localSpeakSetting = it }

                Divider(Modifier.padding(vertical = 16.dp))

                BooleanSetting("Listen", localListenSetting) { localListenSetting = it }

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
