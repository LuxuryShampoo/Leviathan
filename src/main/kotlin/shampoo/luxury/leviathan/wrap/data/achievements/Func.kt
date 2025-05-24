package shampoo.luxury.leviathan.wrap.data.achievements

import shampoo.luxury.leviathan.global.Values.completedAchievements

/**
 * Marks an achievement as completed by adding its ID to the persistent storage.
 *
 * @param achievementId The ID of the achievement to mark as completed.
 */
fun completeAchievement(achievementId: String) {
    completedAchievements.add(achievementId)
}
