package shampoo.luxury.leviathan.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Divider
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
import compose.icons.fontawesomeicons.solid.Hamburger
import compose.icons.fontawesomeicons.solid.Paw
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
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
import shampoo.luxury.leviathan.wrap.data.currency.getMoney
import shampoo.luxury.leviathan.wrap.data.currency.newAddBalance
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
import java.math.BigDecimal

@Composable
fun Shop() =
    PageScope {
        var focusedPetName by remember { mutableStateOf("Loading...") }
        val balance by BalanceState.balance.collectAsState()
        var showPetFood by remember { mutableStateOf(false) }
        var sortedUnownedPets by remember { mutableStateOf<List<Pet>>(emptyList()) }
        var focusedPet by remember { mutableStateOf<Pet?>(null) }
        var init by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            BalanceState.updateBalance(getMoney())
            addLoading("shop market")
            removeLoading("navigation to shop")
            sortedUnownedPets = getUnownedPets().sortedBy { it.cost }
            init = true
        }

        LaunchedEffect(sortedUnownedPets) {
            if (init) {
                focusedPet = sortedUnownedPets.firstOrNull()
                focusedPetName = focusedPet?.name ?: "Loading..."
                removeLoading("shop market")
            }
        }

        TopRow(focusedPetName.takeUnless { showPetFood } ?: "Pet Food")
        Divider()
        Box {
            Box(
                Modifier.fillMaxSize(),
            ) {
                AnimatedVisibility(visible = !showPetFood) {
                    PetMarket(
                        sortedUnownedPets = sortedUnownedPets,
                        focusedPet = focusedPet,
                        onFocusChange = { focusedPetName = it },
                        onBuyPet = { pet ->
                            sortedUnownedPets = sortedUnownedPets - pet
                        },
                        setFocusedPet = { pet ->
                            focusedPet = pet
                        },
                    )
                }
                AnimatedVisibility(visible = showPetFood) {
                    FoodMarket(balance)
                }
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
                        { Paw },
                        contentDescription = "Toggle Pet Market",
                        modifier = Modifier.padding(start = 8.dp),
                        iconSize = 24.dp,
                        hitBox = 32.dp,
                    ) { showPetFood = false }
                } else {
                    Buicon(
                        { Hamburger },
                        contentDescription = "Toggle Pet Food",
                        modifier = Modifier.padding(start = 8.dp),
                        iconSize = 24.dp,
                        hitBox = 32.dp,
                    ) { showPetFood = true }
                }
            }
        }
    }

@OptIn(DelicateCoroutinesApi::class)
@Composable
private fun PetMarket(
    sortedUnownedPets: List<Pet>,
    focusedPet: Pet?,
    onFocusChange: (String) -> Unit,
    onBuyPet: (Pet) -> Unit,
    setFocusedPet: (Pet?) -> Unit,
) {
    var debounceJob by remember { mutableStateOf<Job?>(null) }
    val imageFiles = remember { mutableStateListOf<File>() }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LaunchedEffect(sortedUnownedPets) {
        imageFiles.clear()
        imageFiles.addAll(
            sortedUnownedPets
                .filter { File(it.localPath).exists() }
                .map { File(it.localPath) },
        )
    }

    LaunchedEffect(listState.firstVisibleItemIndex) {
        debounceJob?.cancel()
        debounceJob =
            scope.launch {
                delay(300)
                val pet = sortedUnownedPets.getOrNull(listState.firstVisibleItemIndex)
                setFocusedPet(pet)
                pet?.let { onFocusChange(it.name) }
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

            focusedPet?.let { pet ->
                Button(
                    {
                        onBuyPet(pet)
                        GlobalScope
                            .launch {
                                buyPet(pet)
                            }.invokeOnCompletion {
                                scope.launch {
                                    listState.animateScrollToItem(0)
                                }
                            }
                    },
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 24.dp, bottom = 8.dp),
                ) {
                    Body1("Buy", colorType = OnPrimary)
                }
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

@OptIn(DelicateCoroutinesApi::class)
@Composable
private fun FoodMarket(balance: BigDecimal) {
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
                        GlobalScope.launch {
                            listOf(
                                launch {
                                    newAddBalance(-food.cost, balance)
                                    BalanceState.updateBalance(getMoney())
                                },
                                launch { increasePetLevel(food.levelIncrease, getSelectedPet()) },
                            ).joinAll()

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
