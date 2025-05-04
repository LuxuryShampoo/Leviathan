package xyz.malefic.compose.screens

import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.navigation.Navigator
import xyz.malefic.compose.comps.text.typography.Body1
import xyz.malefic.compose.comps.text.typography.Body2
import xyz.malefic.compose.comps.text.typography.Heading1
import xyz.malefic.compose.engine.fuel.divide
import xyz.malefic.compose.engine.fuel.fuel

@Composable
fun HomeScreen(navi: Navigator) {
    Column(
        modifier =
            Modifier
                .width(500.dp)
                .height(600.dp),
        horizontalAlignment = CenterHorizontally,
    ) {
        fuel {
            Row(
                modifier =
                    Modifier
                        .height(100.dp)
                        .fillMaxWidth(),
                horizontalArrangement = SpaceEvenly,
                verticalAlignment = CenterVertically,
            ) {
                Body1("Hello, World!")
                Body1("Hello, Body!")
            }
        }.divide(vertical = false)()
        fuel {
            Box(
                modifier =
                    Modifier
                        .height(400.dp)
                        .fillMaxWidth(),
                contentAlignment = Center,
            ) {
                Heading1("Character HERE")
            }
        }.divide(vertical = false)()
        Row(
            modifier =
                Modifier
                    .height(100.dp)
                    .fillMaxWidth(),
            horizontalArrangement = SpaceEvenly,
            verticalAlignment = CenterVertically,
        ) {
            Body2("settings n stuff")
        }
    }
}
