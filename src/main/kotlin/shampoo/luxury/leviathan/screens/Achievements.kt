package shampoo.luxury.leviathan.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.fontawesomeicons.solid.ArrowLeft
import shampoo.luxury.leviathan.components.Buicon
import shampoo.luxury.leviathan.components.achievements.AchievementItem
import shampoo.luxury.leviathan.components.achievements.CategoryHeader
import shampoo.luxury.leviathan.global.GlobalLoadingState.navigate
import shampoo.luxury.leviathan.global.GlobalLoadingState.removeLoading
import xyz.malefic.compose.comps.text.typography.Body1
import xyz.malefic.compose.comps.text.typography.Heading2
import androidx.compose.foundation.lazy.items as iii

@Composable
fun Achievements() =
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp, 16.dp, 16.dp),
    ) {
        LaunchedEffect(Unit) {
            removeLoading("navigation to achievements")
        }

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = CenterVertically,
        ) {
            Buicon(
                { ArrowLeft },
                "Back",
                24.dp,
                32.dp,
            ) { navigate("home") }
            Spacer(Modifier.width(8.dp))
            Heading2("Achievements")
        }
        Body1("Track your progress and earn rewards")
        Divider(Modifier.padding(top = 16.dp))

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            val groupedAchievements =
                shampoo.luxury.leviathan.global.Values.allAchievements
                    .groupBy { it.category }

            groupedAchievements.forEach { (category, achievements) ->
                item {
                    CategoryHeader(category)
                }

                iii(achievements) { achievement ->
                    AchievementItem(achievement)
                }

                item {
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
