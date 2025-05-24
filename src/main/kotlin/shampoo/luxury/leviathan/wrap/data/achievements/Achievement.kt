package shampoo.luxury.leviathan.wrap.data.achievements

import shampoo.luxury.leviathan.screens.AchievementCategory
import java.io.Serializable

/**
 * Data class representing an achievement.
 *
 * @property id Unique identifier for the achievement.
 * @property title Title of the achievement.
 * @property description Description of the achievement.
 * @property category Category to which the achievement belongs.
 * @property reward Reward for unlocking the achievement.
 * @property isUnlocked Indicates whether the achievement is unlocked.
 */
data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val category: AchievementCategory,
    val reward: String,
    val isUnlocked: Boolean = false,
) : Serializable
