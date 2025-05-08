package shampoo.luxury.screens

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
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import moe.tlaster.precompose.navigation.Navigator
import shampoo.luxury.components.DropdownText
import shampoo.luxury.components.FileImage
import shampoo.luxury.components.NavBar
import shampoo.luxury.global.Values.selectedPet
import xyz.malefic.compose.comps.text.typography.Body1
import xyz.malefic.compose.comps.text.typography.ColorType.OnPrimary
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
        PetContainer()
        Divider()
        NavBar(navi)
    }
}

@Composable
private fun TopRow() {
    Row(
        Modifier
            .fillMaxHeight(0.2f)
            .fillMaxWidth(),
        SpaceEvenly,
        CenterVertically,
    ) {
        Body1("Hello, World!")
        Button({
            Logger.d("Button clicked!")
        }) {
            Body1("Button", colorType = OnPrimary)
        }
        var expanded by remember { mutableStateOf(false) }
        Box {
            Button({ expanded = true }) {
                Body1("Menu", colorType = OnPrimary)
            }
            DropdownMenu(
                expanded,
                { expanded = false },
            ) {
                DropdownText("Option 1") {
                    Logger.i("Option 1")
                    expanded = false
                }
                DropdownText("Option 2") {
                    Logger.i("Option 2")
                    expanded = false
                }
            }
        }
    }
}

@Composable
private fun PetContainer() {
    Box(
        Modifier
            .fillMaxHeight(0.75f)
            .fillMaxWidth(),
        Center,
    ) {
        var file by remember { mutableStateOf(File("")) }

        LaunchedEffect(Unit) {
            file = selectedPet.downloadImage()
            Logger.d { "File downloaded: ${file.absolutePath}" }
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally,
        ) {
            key(file.absolutePath) {
                Logger.d { "File exists: ${file.exists()}" }
                if (file.exists()) {
                    Logger.d("Recomposed the image")
                    FileImage(
                        file,
                        "Character Image",
                    ) { size(400.dp).clip(RoundedCornerShape(8.dp)) }
                }
            }
        }
    }
}
