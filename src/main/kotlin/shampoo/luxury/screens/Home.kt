package shampoo.luxury.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Cog
import compose.icons.fontawesomeicons.solid.QuestionCircle
import moe.tlaster.precompose.navigation.Navigator
import shampoo.luxury.components.Buicon
import shampoo.luxury.components.DropdownText
import shampoo.luxury.io.Resource.downloadFile
import shampoo.luxury.io.Resource.getLocalResourcePath
import xyz.malefic.compose.comps.text.typography.Body1
import xyz.malefic.compose.comps.text.typography.Body1B
import xyz.malefic.ext.precompose.gate
import java.io.File

@Composable
fun Home(navi: Navigator) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
        horizontalAlignment = CenterHorizontally,
    ) {
        TopRow()
        Divider()
        MiddleBox()
        Divider()
        BottomRow(navi)
    }
}

@Composable
private fun TopRow() {
    Row(
        modifier =
            Modifier
                .fillMaxHeight(0.2f)
                .fillMaxWidth(),
        horizontalArrangement = SpaceEvenly,
        verticalAlignment = CenterVertically,
    ) {
        Body1("Hello, World!")
        Button(onClick = {
            println("Button clicked!")
        }) {
            Body1B("Button")
        }
        val expanded = remember { mutableStateOf(false) }
        Box {
            Button(onClick = { expanded.value = true }) {
                Body1B("Menu")
            }
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
            ) {
                DropdownText("Option 1") {
                    println("Option 1")
                    expanded.value = false
                }
                DropdownText("Option 2") {
                    println("Option 2")
                    expanded.value = false
                }
            }
        }
    }
}

@Composable
private fun MiddleBox() {
    Box(
        modifier =
            Modifier
                .fillMaxHeight(0.75f)
                .fillMaxWidth(),
        contentAlignment = Center,
    ) {
        var file by remember { mutableStateOf(File("")) }

        LaunchedEffect(Unit) {
            file = downloadFile("https://gallery.malefic.xyz/photos/Leviathan/BobAlarm.png", getLocalResourcePath("BobAlarm.png"))
            println("File downloaded: ${file.absolutePath}")
        }

        Column(
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            key(file.absolutePath) {
                Logger.d { "File exists: ${file.exists()}" }
                if (file.exists()) {
                    Logger.d("Recomposed the image")
                    val image: ImageBitmap = file.inputStream().readAllBytes().decodeToImageBitmap()
                    Image(
                        bitmap = image,
                        contentDescription = "Character Image",
                        modifier =
                            Modifier
                                .size(400.dp)
                                .clip(RoundedCornerShape(8.dp)),
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomRow(navi: Navigator) {
    Row(
        modifier =
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
        horizontalArrangement = SpaceEvenly,
        verticalAlignment = CenterVertically,
    ) {
        Buicon(FontAwesomeIcons.Solid.Cog, "Settings") { navi gate "settings" }
        Buicon(FontAwesomeIcons.Solid.QuestionCircle, "Help") { println("Help clicked!") }
    }
}
