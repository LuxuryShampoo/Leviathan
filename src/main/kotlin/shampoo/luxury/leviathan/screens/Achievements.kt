package shampoo.luxury.leviathan.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import shampoo.luxury.leviathan.components.AchievementItem
import shampoo.luxury.leviathan.components.CategoryHeader
import shampoo.luxury.leviathan.components.PageScope
import shampoo.luxury.leviathan.global.Values.allAchievements
import xyz.malefic.compose.comps.text.typography.Body1
import xyz.malefic.compose.comps.text.typography.Heading2

/**
 * Main composable function for the Achievements screen.
 * Displays a list of achievements grouped by category.
 */
@Composable
fun Achievements() =
    PageScope {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        ) {
            Heading2("Achievements")
            Body1("Track your progress and earn rewards")
            Divider(Modifier.padding(top = 16.dp))
        }

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            val groupedAchievements = allAchievements.groupBy { it.category }

            groupedAchievements.forEach { (category, achievements) ->
                item {
                    CategoryHeader(category)
                }

                items(achievements) { achievement ->
                    AchievementItem(achievement)
                }

                item {
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
