package shampoo.luxury.leviathan.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import compose.icons.fontawesomeicons.SolidGroup
import compose.icons.fontawesomeicons.solid.Trophy
import kotlinx.coroutines.launch
import shampoo.luxury.leviathan.components.Buicon
import shampoo.luxury.leviathan.components.Burger
import shampoo.luxury.leviathan.components.FileImage
import shampoo.luxury.leviathan.components.layouts.PageScope
import shampoo.luxury.leviathan.components.nav.NavButton
import shampoo.luxury.leviathan.components.shop.CarouselButton
import shampoo.luxury.leviathan.global.GlobalLoadingState.addLoading
import shampoo.luxury.leviathan.global.GlobalLoadingState.navigate
import shampoo.luxury.leviathan.global.GlobalLoadingState.removeLoading
import shampoo.luxury.leviathan.global.Values
import shampoo.luxury.leviathan.global.Values.selectedPet
import shampoo.luxury.leviathan.wrap.data.pets.Pet
import shampoo.luxury.leviathan.wrap.data.pets.getOwnedPets
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
        var file by remember { mutableStateOf(File("")) }
        var ownedPets by remember { mutableStateOf<List<Pet>>(emptyList()) }
        var showRow by remember { mutableStateOf(false) }
        var currentIndex by remember { mutableStateOf(0) }
        val logger = Logger.withTag("PetContainer")
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            removeLoading("navigation to home")
            scope.launch {
                ownedPets = getOwnedPets()
                if (ownedPets.isNotEmpty()) currentIndex = 0
            }
        }

        LaunchedEffect(selectedPet) {
            file = File(selectedPet.localPath)
        }

        LaunchedEffect(file) {
            logger.d { "Checking file: ${file.absolutePath}, exists: ${file.exists()}, pet: ${selectedPet.name}" }
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
                if (file.exists()) {
                    val imageSize = if (showRow) 120.dp else 400.dp
                    val elevation = if (showRow) 8.dp else 0.dp
                    Surface(
                        Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { showRow = !showRow }
                            .padding(8.dp),
                        elevation = elevation,
                    ) {
                        FileImage(file, selectedPet.name) { size(imageSize) }
                    }
                }
            }
            if (showRow && ownedPets.isNotEmpty()) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    contentAlignment = Center,
                ) {
                    CarouselButton("<", scope, { align(Alignment.CenterStart) }) {
                        currentIndex = (currentIndex - 1 + ownedPets.size) % ownedPets.size
                    }
                    val shownPet = ownedPets[currentIndex]
                    val shownFile = File(shownPet.localPath)
                    if (shownFile.exists()) {
                        Surface(
                            Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    scope.launch {
                                        logger.d { "Loading pet: ${shownPet.name}" }
                                        Values.selectedPet = shownPet
                                        logger.d { "Selected pet updated: ${Values.selectedPet}" }
                                        showRow = false
                                    }
                                }.padding(8.dp),
                            elevation = 8.dp,
                        ) {
                            FileImage(shownFile, shownPet.name) { size(120.dp) }
                        }
                    }
                    CarouselButton(">", scope, { align(Alignment.CenterEnd) }) {
                        currentIndex = (currentIndex + 1) % ownedPets.size
                    }
                }
            }
        }
    }
}
