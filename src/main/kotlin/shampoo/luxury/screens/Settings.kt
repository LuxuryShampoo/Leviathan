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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Home
import moe.tlaster.precompose.navigation.Navigator
import shampoo.luxury.components.Buicon
import shampoo.luxury.theme.ThemeSelector
import xyz.malefic.compose.comps.text.typography.Heading1
import xyz.malefic.compose.comps.text.typography.Heading2

@Composable
fun Settings(navi: Navigator) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Buicon(
                FontAwesomeIcons.Solid.Home,
                "homeButton",
                size = 24.dp,
                hitBox = 32.dp,
                unbounded = true,
                Modifier.align(Alignment.TopEnd),
            ) { navi.navigate("home") }
            Heading1("Settings", Modifier.align(Alignment.TopStart))
        }
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Heading2("Theme")

            ThemeSelector()
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        Spacer(modifier = Modifier.weight(1f))
    }
}
