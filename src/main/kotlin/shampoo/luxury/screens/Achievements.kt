package shampoo.luxury.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.navigation.Navigator
import shampoo.luxury.components.NavBar
import shampoo.luxury.theme.ThemeSelector
import xyz.malefic.compose.comps.switch.BooleanSwitch
import xyz.malefic.compose.comps.text.typography.Heading1
import xyz.malefic.compose.comps.text.typography.Heading2
import xyz.malefic.compose.prefs.delegate.BooleanPreference

@Composable
fun Achievements(navi: Navigator) {
    var speakPreference by BooleanPreference("speak_enabled", true)
    val speak = remember { mutableStateOf(speakPreference) }

    Column {
        Box(Modifier.fillMaxWidth().fillMaxHeight(0.2f)) {}
    }
}
