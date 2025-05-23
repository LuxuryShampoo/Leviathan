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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import shampoo.luxury.leviathan.components.BooleanSetting
import shampoo.luxury.leviathan.components.PageScope
import shampoo.luxury.leviathan.global.Values.Prefs.listenPreference
import shampoo.luxury.leviathan.global.Values.Prefs.speakPreference
import xyz.malefic.compose.comps.text.typography.Heading1

@Composable
fun Settings() =
    PageScope {
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

                BooleanSetting("Speak", speakPreference) { speakPreference = it }

                Divider(Modifier.padding(vertical = 16.dp))

                BooleanSetting("Listen", listenPreference) { listenPreference = it }

                Spacer(Modifier.height(8.dp))
            }
        }
    }
