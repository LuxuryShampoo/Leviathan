package shampoo.luxury.leviathan.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import compose.icons.fontawesomeicons.SolidGroup
import compose.icons.fontawesomeicons.solid.Hamburger
import compose.icons.fontawesomeicons.solid.Paw
import kotlinx.coroutines.launch
import shampoo.luxury.leviathan.components.Buicon
import shampoo.luxury.leviathan.components.layouts.PageScope
import shampoo.luxury.leviathan.components.shop.Carousel
import shampoo.luxury.leviathan.components.shop.CarouselButton
import shampoo.luxury.leviathan.components.shop.CarouselCost
import shampoo.luxury.leviathan.global.BalanceState
import shampoo.luxury.leviathan.global.GlobalLoadingState.addLoading
import shampoo.luxury.leviathan.global.GlobalLoadingState.removeLoading
import shampoo.luxury.leviathan.global.Values.getSelectedPet
import shampoo.luxury.leviathan.wrap.data.currency.addToBalance
import shampoo.luxury.leviathan.wrap.data.currency.getMoney
import shampoo.luxury.leviathan.wrap.data.pets.Pet
import shampoo.luxury.leviathan.wrap.data.pets.buyPet
import shampoo.luxury.leviathan.wrap.data.pets.getUnownedPets
import shampoo.luxury.leviathan.wrap.data.pets.increasePetLevel
import xyz.malefic.compose.comps.text.typography.Body1
import xyz.malefic.compose.comps.text.typography.Body2
import xyz.malefic.compose.comps.text.typography.ColorType.OnPrimary
import xyz.malefic.compose.comps.text.typography.Heading3
import xyz.malefic.compose.comps.text.typography.Heading6
import java.io.File

@Composable
fun Shop() =
    PageScope {
        var focusedPetName by remember { mutableStateOf("Loading...") }
        val balance by BalanceState.balance.collectAsState()
        var showPetFood by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            addToBalance(0)
            val selectedPetName = getSelectedPet().name
            Logger.d("Shop") { "The selected pet is $selectedPetName" }
        }

        TopRow(focusedPetName.takeUnless { showPetFood } ?: "Pet Food")
        Divider()
        Box {
            if (!showPetFood) {
                PetMarket { focusedPetName = it }
            } else {
                FoodMarket()
            }
            Box(
                Modifier
                    .align(TopEnd)
                    .padding(16.dp),
            ) {
                Body1("$${balance.toPlainString()}")
            }
            Box(
                Modifier
                    .align(TopStart)
                    .padding(16.dp),
            ) {
                if (showPetFood) {
                    Buicon(
                        { SolidGroup.Paw },
                        contentDescription = "Toggle Pet Market",
                        modifier = Modifier.padding(start = 8.dp),
                        iconSize = 24.dp,
                        hitBox = 32.dp,
                    ) { showPetFood = false }
                } else {
                    Buicon(
                        { SolidGroup.Hamburger },
                        contentDescription = "Toggle Pet Food",
                        modifier = Modifier.padding(start = 8.dp),
                        iconSize = 24.dp,
                        hitBox = 32.dp,
                    ) { showPetFood = true }
                }
            }
        }
    }

@Composable
private fun TopRow(focusedPetName: String) {
    Row(
        Modifier
            .fillMaxHeight(0.2f)
            .fillMaxWidth(),
        SpaceEvenly,
        CenterVertically,
    ) {
        Heading3(focusedPetName)
    }
}

@Composable
private fun PetMarket(onFocusChange: (String) -> Unit) {
    val imageFiles = remember { mutableStateListOf<File>() }
    var sortedUnownedPets by remember { mutableStateOf<List<Pet>>(emptyList()) }
    var focusedPet by remember { mutableStateOf<Pet?>(null) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    suspend fun loadPets() {
        addLoading("shop market")
        removeLoading("navigation to shop")
        val unownedPets = getUnownedPets()
        sortedUnownedPets = unownedPets.sortedBy { it.cost }
        imageFiles.clear()
        imageFiles.addAll(
            sortedUnownedPets
                .filter { File(it.localPath).exists() }
                .map { File(it.localPath) },
        )
        focusedPet = sortedUnownedPets.firstOrNull()
        onFocusChange(focusedPet?.name ?: "Loading...")
        removeLoading("shop market")
    }

    LaunchedEffect(Unit) {
        loadPets()
    }

    LaunchedEffect(listState.firstVisibleItemIndex) {
        focusedPet = sortedUnownedPets.getOrNull(listState.firstVisibleItemIndex)
        focusedPet?.let {
            onFocusChange(it.name)
        }
    }

    if (sortedUnownedPets.isEmpty()) {
        Box(
            Modifier
                .fillMaxHeight(0.6f)
                .fillMaxWidth(),
            Center,
        ) {
            Heading3("All Gone!")
        }
        onFocusChange("Woah!")
        return
    }

    Box(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        Center,
    ) {
        if (imageFiles.isNotEmpty()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f),
                Center,
            ) {
                Carousel(listState, imageFiles)
            }

            CarouselButton("<", scope, { align(CenterStart) }) {
                val prevIndex = (listState.firstVisibleItemIndex - 1 + imageFiles.size) % imageFiles.size
                listState.animateScrollToItem(prevIndex)
            }

            CarouselButton(">", scope, { align(CenterEnd) }) {
                val nextIndex = (listState.firstVisibleItemIndex + 1) % imageFiles.size
                listState.animateScrollToItem(nextIndex)
            }

            CarouselCost(focusedPet)

            focusedPet?.let {
                Button(
                    {
                        scope.launch {
                            buyPet(focusedPet!!)
                            loadPets()
                            listState.animateScrollToItem(0)
                        }
                    },
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 24.dp, bottom = 8.dp),
                ) {
                    Text("Buy")
                }
            }
        }
    }
}

@Composable
private fun FoodMarket() {
    val scope = rememberCoroutineScope()
    val petFoods =
        listOf(
            PetFood("Basic Food", 10.0, 0.12),
            PetFood("Premium Food", 25.0, 0.32),
            PetFood("Deluxe Food", 50.0, 0.7),
            PetFood("Royal Food", 100.0, 1.5),
        )

    LazyColumn(
        Modifier
            .fillMaxWidth()
            .padding(start = 48.dp, end = 48.dp, top = 32.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        itemsIndexed(petFoods) { idx, food ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = CenterVertically,
            ) {
                Column(Modifier.weight(1f)) {
                    Heading6(food.name)
                    Body2("+${food.levelIncrease} xp")
                }
                Body1("$${food.cost}", Modifier.padding(end = 24.dp))
                Button(
                    {
                        scope.launch {
                            addToBalance(-food.cost)
                            BalanceState.updateBalance(getMoney())
                            increasePetLevel(food.levelIncrease, getSelectedPet())
                            Logger.d("Shop") { "Bought ${food.name} for ${food.cost}, increased pet level by ${food.levelIncrease}" }
                        }
                    },
                ) {
                    Body1("Order", colorType = OnPrimary)
                }
            }
            if (idx < petFoods.lastIndex) {
                Divider()
            }
        }
    }
}

/**
 * Data class representing a type of pet food available in the shop.
 *
 * @property name The display name of the pet food.
 * @property cost The cost of the pet food in currency.
 * @property levelIncrease The amount of experience or level increase provided by the food.
 */
private data class PetFood(
    val name: String,
    val cost: Double,
    val levelIncrease: Double,
)
