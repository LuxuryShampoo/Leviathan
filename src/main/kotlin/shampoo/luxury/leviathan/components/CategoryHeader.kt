package shampoo.luxury.leviathan.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import shampoo.luxury.leviathan.screens.AchievementCategory
import xyz.malefic.compose.comps.text.typography.Heading2

/**
 * Composable function for displaying a category header.
 *
 * @param category The category of achievements to display.
 */
@Composable
fun CategoryHeader(category: AchievementCategory) {
    val categoryName =
        when (category) {
            AchievementCategory.PET_CARE -> "Pet Care"
            AchievementCategory.PRODUCTIVITY -> "Productivity"
            AchievementCategory.POMODORO -> "Pomodoro"
            AchievementCategory.TASKS -> "Tasks"
            AchievementCategory.SPECIAL -> "Special"
        }

    Column(Modifier.padding(vertical = 8.dp)) {
        Heading2(categoryName)
        Divider(Modifier.padding(top = 4.dp))
    }
}
