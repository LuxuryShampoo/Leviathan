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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import shampoo.luxury.leviathan.components.PageScope
import shampoo.luxury.leviathan.components.SettingsOption
import shampoo.luxury.leviathan.wrap.data.settings.getSetting
import shampoo.luxury.leviathan.wrap.data.settings.setSetting
import xyz.malefic.compose.comps.switch.BooleanSwitch
import xyz.malefic.compose.comps.text.typography.Heading1

@Composable
fun Settings() =
    PageScope {
        val speak = remember { mutableStateOf(getSetting("speak_enabled")?.toBoolean() != false) }
        val listen = remember { mutableStateOf(getSetting("listen_enabled")?.toBoolean() != false) }

        LaunchedEffect(speak.value) {
            setSetting("speak_enabled", speak.value.toString())
        }

        LaunchedEffect(listen.value) {
            setSetting("listen_enabled", listen.value.toString())
        }

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
    }
