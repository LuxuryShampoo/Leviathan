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
import shampoo.luxury.leviathan.global.Values.Prefs.listenPreference
import shampoo.luxury.leviathan.global.Values.Prefs.speakPreference
import xyz.malefic.compose.comps.text.typography.Heading1

@Composable
fun Settings() {
    var localSpeakPreference by remember { mutableStateOf(speakPreference) }
    var localListenPreference by remember { mutableStateOf(listenPreference) }

    val saveSettings = {
        speakPreference = localSpeakPreference
        listenPreference = localListenPreference
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

                BooleanSetting("Speak", localSpeakPreference) { localSpeakPreference = it }

                Divider(Modifier.padding(vertical = 16.dp))

                BooleanSetting("Listen", localListenPreference) { localListenPreference = it }

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
