package shampoo.luxury.leviathan.screens

import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import shampoo.luxury.leviathan.components.BooleanSetting
import shampoo.luxury.leviathan.components.PageScope
import shampoo.luxury.leviathan.global.GlobalLoadingState.addLoading
import shampoo.luxury.leviathan.global.GlobalLoadingState.removeLoading
import shampoo.luxury.leviathan.global.Values.Prefs.getListenSetting
import shampoo.luxury.leviathan.global.Values.Prefs.getSpeakSetting
import shampoo.luxury.leviathan.theme.ThemeSelector
import shampoo.luxury.leviathan.wrap.data.settings.saveSettings
import xyz.malefic.compose.comps.text.typography.Heading1
import xyz.malefic.compose.comps.text.typography.Heading3

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun Settings() {
    var localSpeakSetting by remember { mutableStateOf(false) }
    var localListenSetting by remember { mutableStateOf(false) }
    var oldSpeakSetting = false
    var oldListenSetting = false
    var settingsChanged by remember { mutableStateOf(false) }

    val log = Logger.withTag("Settings")

    LaunchedEffect(Unit) {
        addLoading("getting settings")
        removeLoading("navigation to settings")
        withContext(Dispatchers.IO) {
            log.d { "Loading settings..." }
            oldSpeakSetting = getSpeakSetting()
            localSpeakSetting = oldSpeakSetting
            oldListenSetting = getListenSetting()
            localListenSetting = oldListenSetting
            log.d { "Settings loaded: speak=$localSpeakSetting, listen=$localListenSetting" }
            removeLoading("getting settings")
        }
    }

    PageScope({
        if (settingsChanged) {
            GlobalScope.launch {
                saveSettings(
                    log,
                    localSpeakSetting,
                    localListenSetting,
                    oldSpeakSetting,
                    oldListenSetting,
                )
                settingsChanged = false
            }
        }
    }) {
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

                Row(
                    Modifier.fillMaxWidth(),
                    SpaceBetween,
                    CenterVertically,
                ) {
                    Heading3("Theme")
                    ThemeSelector()
                }

                Divider(Modifier.padding(vertical = 16.dp))

                BooleanSetting("Speak", localSpeakSetting) { localSpeakSetting = it }

                Divider(Modifier.padding(vertical = 16.dp))

                BooleanSetting("Listen", localListenSetting) { localListenSetting = it }

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
