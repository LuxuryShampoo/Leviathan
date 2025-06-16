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
import androidx.compose.material.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import compose.icons.fontawesomeicons.solid.Tasks
import compose.icons.fontawesomeicons.solid.Trophy
import kotlinx.coroutines.launch
import shampoo.luxury.leviathan.components.Buicon
import shampoo.luxury.leviathan.components.FileImage
import shampoo.luxury.leviathan.components.layouts.PageScope
import shampoo.luxury.leviathan.components.shop.CarouselButton
import shampoo.luxury.leviathan.global.GlobalLoadingState.addLoading
import shampoo.luxury.leviathan.global.GlobalLoadingState.navigate
import shampoo.luxury.leviathan.global.GlobalLoadingState.removeLoading
import shampoo.luxury.leviathan.global.Values.getSelectedPet
import shampoo.luxury.leviathan.global.Values.selectedPetName
import shampoo.luxury.leviathan.global.Values.setSelectedPet
import shampoo.luxury.leviathan.global.clearPreferences
import shampoo.luxury.leviathan.wrap.data.pets.Pet
import shampoo.luxury.leviathan.wrap.data.pets.getOwnedPets
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
        Buicon(
            { Tasks },
            "Tasks",
            32.dp,
            48.dp,
        ) {
            navigate("tasks")
        }
        Buicon(
            { Trophy },
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
        var initSelectedPet by remember { mutableStateOf(false) }
        var selectedPet by remember { mutableStateOf(Pet("Bob", "images/BobAlarm.png", 50.0)) }

        LaunchedEffect(Unit) {
            removeLoading("navigation to home")
            ownedPets = getOwnedPets()
            if (ownedPets.isNotEmpty()) currentIndex = 0
        }

        LaunchedEffect(Unit) {
            selectedPet = getSelectedPet()
            logger.d { "Initial selected pet: $selectedPet" }
            initSelectedPet =
                true.also {
                    logger.d { "Selected pet initialized: $selectedPet" }
                }
            if (selectedPetName != selectedPet.name) {
                logger.d { "Selected pet name changed: ${selectedPet.name} != $selectedPetName" }
                clearPreferences()
                selectedPetName = selectedPet.name
                logger.d { "Updated pet after error: ${selectedPet.name} and $selectedPetName should be the same" }
            }
        }

        LaunchedEffect(selectedPet, initSelectedPet) {
            file = File(selectedPet.localPath)
            if (initSelectedPet) {
                file = File(selectedPet.localPath)
                setSelectedPet(selectedPet)
                logger.d { "Selected pet updated: ${selectedPet.name}, selectedPetName: $selectedPetName, file: ${file.absolutePath}" }
            } else {
                logger.d { "The selected pet is not initialized yet, waiting for it to be set." }
            }
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
                        Box {
                            FileImage(file, selectedPet.name) { size(imageSize) }
                            Surface(
                                Modifier
                                    .align(Alignment.TopStart)
                                    .padding(4.dp),
                                RoundedCornerShape(bottomEnd = 8.dp),
                                Color.Black.copy(alpha = 0.7f),
                            ) {
                                Text(
                                    "Lv. ${selectedPet.level.toInt()}",
                                    Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    Color.White,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }
                }
            }
            if (showRow && ownedPets.isNotEmpty()) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    Center,
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
                                        selectedPet = shownPet
                                        logger.d { "Selected pet updated: $selectedPet" }
                                        showRow = false
                                    }
                                }.padding(8.dp),
                            elevation = 8.dp,
                        ) {
                            Box {
                                FileImage(shownFile, shownPet.name) { size(120.dp) }
                                Surface(
                                    Modifier
                                        .align(Alignment.TopStart)
                                        .padding(4.dp),
                                    RoundedCornerShape(bottomEnd = 8.dp),
                                    Color.Black.copy(alpha = 0.7f),
                                ) {
                                    Text(
                                        "Lv. ${shownPet.level.toInt()}",
                                        Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        Color.White,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                            }
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
