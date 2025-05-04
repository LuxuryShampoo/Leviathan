package xyz.malefic.compose.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.unit.dp
import xyz.malefic.compose.comps.box.BackgroundBox
import xyz.malefic.compose.comps.text.typography.Heading2
import xyz.malefic.compose.engine.factory.ButtonFactory
import xyz.malefic.compose.engine.factory.ColumnFactory
import xyz.malefic.compose.engine.factory.TextFactory
import xyz.malefic.compose.engine.factory.div
import xyz.malefic.compose.engine.factory.divAssign
import xyz.malefic.compose.engine.factory.timesAssign
import xyz.malefic.compose.engine.fuel.space
import xyz.malefic.ext.any.resolveNull
import xyz.malefic.ext.string.either

@Composable
fun App1(
    id: String,
    name: String?,
) {
    var text by remember { mutableStateOf("Hello, World!") }

    BackgroundBox(contentAlignment = Alignment.Center) {
        ColumnFactory {
            ButtonFactory { TextFactory(text)() } / {
                onClick = { text = text.either("Hello, World!", "Hello, Desktop!") }
            } *= {
                space(16.dp)
            }
            Heading2("ID: $id")
            Heading2(name.resolveNull("Name: $name", "Unnamed"))
        } /= {
            horizontalAlignment = CenterHorizontally
            verticalArrangement = Arrangement.Center
        }
    }
}
