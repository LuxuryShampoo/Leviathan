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
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.touchlab.kermit.Logger
import compose.icons.fontawesomeicons.SolidGroup
import compose.icons.fontawesomeicons.solid.Trophy
import shampoo.luxury.leviathan.components.Buicon
import shampoo.luxury.leviathan.components.Burger
import shampoo.luxury.leviathan.components.FileImage
import shampoo.luxury.leviathan.components.layouts.PageScope
import shampoo.luxury.leviathan.components.nav.NavButton
import shampoo.luxury.leviathan.global.GlobalLoadingState.addLoading
import shampoo.luxury.leviathan.global.GlobalLoadingState.navigate
import shampoo.luxury.leviathan.global.GlobalLoadingState.removeLoading
import shampoo.luxury.leviathan.global.Values
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
        // Use remember to store the pet object directly
        val pet = remember { Values.selectedPet }
        var file by remember { mutableStateOf(File("")) }
        val logger = Logger.withTag("PetContainer")

        LaunchedEffect(Unit) {
            removeLoading("navigation to home")
        }

        LaunchedEffect(pet) {
            file = File(pet.localPath)
        }

        LaunchedEffect(file) {
            logger.i { "Checking file: ${file.absolutePath}, exists: ${file.exists()}, pet: ${pet.name}" }
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
            // Level indicator above pet
            Text(
                text = "lvl ${pet.level}", // Show actual pet level
                color = Color(0xFF4CAF50), // Green color for the level
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            // Pet Food Count
            Row(
                verticalAlignment = CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 8.dp),
            ) {
                Text(
                    text = "Pet Food: ${Values.petFoodCount}",
                    fontSize = 16.sp,
                )

                // Add Feed button if there's pet food available
                if (Values.petFoodCount > 0) {
                    Button(
                        onClick = {
                            // Use one pet food
                            Values.petFoodCount--
                            // Increment pet's feeding progress
                            Values.selectedPet =
                                pet.copy(
                                    foodFed = pet.foodFed + 1,
                                )

                            // Level up if enough food has been fed
                            if (Values.selectedPet.foodFed >= Values.selectedPet.level) {
                                Values.selectedPet =
                                    Values.selectedPet.copy(
                                        level = Values.selectedPet.level + 1,
                                        foodFed = 0,
                                    )
                            }
                        },
                        modifier = Modifier.padding(start = 8.dp),
                    ) {
                        Text("Feed")
                    }
                }
            }

            // Progress bar to show feeding progress
            LinearProgressIndicator(
                progress = if (pet.level > 0) pet.foodFed.toFloat() / pet.level.toFloat() else 0f,
                modifier =
                    Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth(0.8f),
                color = Color(0xFF4CAF50), // Green color
            )

            key(file.absolutePath) {
                if (file.exists()) {
                    FileImage(
                        file,
                        pet.name,
                    ) { size(400.dp).clip(RoundedCornerShape(8.dp)) }
                }
            }
        }
    }
}
