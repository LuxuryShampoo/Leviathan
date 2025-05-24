package shampoo.luxury.leviathan.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import shampoo.luxury.leviathan.wrap.data.achievements.AchievementCategory
import shampoo.luxury.leviathan.wrap.data.achievements.AchievementCategory.PET_CARE
import shampoo.luxury.leviathan.wrap.data.achievements.AchievementCategory.POMODORO
import shampoo.luxury.leviathan.wrap.data.achievements.AchievementCategory.PRODUCTIVITY
import shampoo.luxury.leviathan.wrap.data.achievements.AchievementCategory.SPECIAL
import shampoo.luxury.leviathan.wrap.data.achievements.AchievementCategory.TASKS
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
            PET_CARE -> "Pet Care"
            PRODUCTIVITY -> "Productivity"
            POMODORO -> "Pomodoro"
            TASKS -> "Tasks"
            SPECIAL -> "Special"
        }

    Column(Modifier.padding(vertical = 8.dp)) {
        Heading2(categoryName)
        Divider(Modifier.padding(top = 4.dp))
    }
}
