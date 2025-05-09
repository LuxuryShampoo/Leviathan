package shampoo.luxury.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import moe.tlaster.precompose.navigation.Navigator
import xyz.malefic.compose.prefs.delegate.BooleanPreference

@Composable
fun Achievements(navi: Navigator) {
    var speakPreference by BooleanPreference("speak_enabled", true)
    val speak = remember { mutableStateOf(speakPreference) }

    Column {
        Box(Modifier.fillMaxWidth().fillMaxHeight(0.2f)) {}
    }
}
