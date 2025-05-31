package shampoo.luxury.leviathan.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import compose.icons.fontawesomeicons.SolidGroup
import compose.icons.fontawesomeicons.solid.Trophy
import shampoo.luxury.leviathan.components.Buicon
import shampoo.luxury.leviathan.components.Burger
import shampoo.luxury.leviathan.components.FileImage
import shampoo.luxury.leviathan.components.NavButton
import shampoo.luxury.leviathan.components.PageScope
import shampoo.luxury.leviathan.global.GlobalLoadingState.addLoading
import shampoo.luxury.leviathan.global.GlobalLoadingState.navigate
import shampoo.luxury.leviathan.global.GlobalLoadingState.removeLoading
import shampoo.luxury.leviathan.global.Values.selectedPet
import xyz.malefic.compose.comps.text.typography.Body1
import xyz.malefic.compose.comps.text.typography.ColorType.OnPrimary
import java.io.File

@Composable
fun Home() =
    PageScope {
        TopRow()
        Divider()
        PetContainer()
    }

@Composable
private fun TopRow() {
    Row(
        Modifier
            .fillMaxHeight(0.2f)
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        SpaceBetween,
        CenterVertically,
    ) {
        Burger()
        NavButton("tasks") {
            Body1("Tasks", colorType = OnPrimary)
        }
        Buicon(
            { SolidGroup.Trophy },
            "Achievements",
            32.dp,
            48.dp,
        ) {
            navigate("achievements")
        }
    }
}

@Composable
private fun PetContainer() {
    Box(
        Modifier.fillMaxSize(),
        Center,
    ) {
        val pet by selectedPet.collectAsState()
        var file by remember { mutableStateOf(File("")) }

        LaunchedEffect(Unit) {
            removeLoading("navigation to home")
        }

        LaunchedEffect(pet) {
            file = pet?.localPath?.let { File(it) } ?: File("")
        }

        LaunchedEffect(file) {
            if (!file.exists()) {
                addLoading("home pet image")
            } else {
                removeLoading("home pet image")
            }
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally,
        ) {
            key(file.absolutePath) {
                FileImage(
                    file,
                    pet?.name ?: "Pet",
                ) { size(400.dp).clip(RoundedCornerShape(8.dp)) }
            }
        }
    }
}
