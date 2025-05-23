package shampoo.luxury.leviathan.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import shampoo.luxury.leviathan.components.PageScope
import shampoo.luxury.leviathan.global.Values
import xyz.malefic.compose.comps.text.typography.Body1
import xyz.malefic.compose.comps.text.typography.Heading2
import xyz.malefic.compose.prefs.collection.PersistentHashSet
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

/**
 * Enum class representing the categories of achievements.
 */
enum class AchievementCategory {
    PET_CARE,
    PRODUCTIVITY,
    POMODORO,
    TASKS,
    SPECIAL,
}

// Persistent storage for completed achievements
private val completedAchievements =
    PersistentHashSet<String>(
        "completedAchievements",
        Values.Prefs.prefs,
    )

// List of all achievements available in the app
private val allAchievements =
    listOf(
        Achievement(
            "pet_first_interaction",
            "First Friend",
            "Interact with your pet for the first time",
            AchievementCategory.PET_CARE,
            "Unlock new pet interaction options",
            completedAchievements.contains("pet_first_interaction"),
        ),
        Achievement(
            "pet_daily_streak_7",
            "Loyal Companion",
            "Interact with your pet for 7 consecutive days",
            AchievementCategory.PET_CARE,
            "Bonus pet happiness",
            completedAchievements.contains("pet_daily_streak_7"),
        ),
        Achievement(
            "pet_collection_complete",
            "Pet Collector",
            "Collect all available pets",
            AchievementCategory.PET_CARE,
            "Special pet animation",
            completedAchievements.contains("pet_collection_complete"),
        ),
        Achievement(
            "productivity_first_alarm",
            "Early Bird",
            "Set your first alarm",
            AchievementCategory.PRODUCTIVITY,
            "New alarm sound options",
            completedAchievements.contains("productivity_first_alarm"),
        ),
        Achievement(
            "productivity_morning_routine",
            "Morning Master",
            "Complete a morning routine for 5 consecutive days",
            AchievementCategory.PRODUCTIVITY,
            "Morning productivity boost",
            completedAchievements.contains("productivity_morning_routine"),
        ),
        Achievement(
            "productivity_voice_assistant",
            "Voice Commander",
            "Use voice commands 10 times",
            AchievementCategory.PRODUCTIVITY,
            "Unlock advanced voice commands",
            completedAchievements.contains("productivity_voice_assistant"),
        ),
        Achievement(
            "pomodoro_first_session",
            "Focus Initiate",
            "Complete your first Pomodoro session",
            AchievementCategory.POMODORO,
            "Basic Pomodoro statistics",
            completedAchievements.contains("pomodoro_first_session"),
        ),
        Achievement(
            "pomodoro_marathon",
            "Focus Marathon",
            "Complete 4 Pomodoro sessions in a row",
            AchievementCategory.POMODORO,
            "Extended focus timer option",
            completedAchievements.contains("pomodoro_marathon"),
        ),
        Achievement(
            "pomodoro_master",
            "Pomodoro Master",
            "Complete 100 Pomodoro sessions total",
            AchievementCategory.POMODORO,
            "Custom Pomodoro settings",
            completedAchievements.contains("pomodoro_master"),
        ),
        // Task Achievements
        Achievement(
            "tasks_first_complete",
            "Task Tackler",
            "Complete your first task",
            AchievementCategory.TASKS,
            "Task prioritization feature",
            completedAchievements.contains("tasks_first_complete"),
        ),
        Achievement(
            "tasks_daily_complete",
            "Daily Dominator",
            "Complete all daily tasks for 3 consecutive days",
            AchievementCategory.TASKS,
            "Task streak counter",
            completedAchievements.contains("tasks_daily_complete"),
        ),
        Achievement(
            "tasks_efficiency",
            "Efficiency Expert",
            "Complete 10 tasks in a single day",
            AchievementCategory.TASKS,
            "Task efficiency statistics",
            completedAchievements.contains("tasks_efficiency"),
        ),
        // Special Achievements
        Achievement(
            "special_night_owl",
            "Night Owl",
            "Use the app after midnight for 5 days",
            AchievementCategory.SPECIAL,
            "Dark theme option",
            completedAchievements.contains("special_night_owl"),
        ),
        Achievement(
            "special_early_adopter",
            "Early Adopter",
            "Use the app within the first month of release",
            AchievementCategory.SPECIAL,
            "Exclusive pet accessory",
            completedAchievements.contains("special_early_adopter"),
        ),
        Achievement(
            "special_explorer",
            "Feature Explorer",
            "Try every feature in the app at least once",
            AchievementCategory.SPECIAL,
            "App customization options",
            completedAchievements.contains("special_explorer"),
        ),
    )

/**
 * Marks an achievement as completed by adding its ID to the persistent storage.
 *
 * @param achievementId The ID of the achievement to mark as completed.
 */
fun completeAchievement(achievementId: String) {
    completedAchievements.add(achievementId)
}

/**
 * Main composable function for the Achievements screen.
 * Displays a list of achievements grouped by category.
 */
@Composable
fun Achievements() =
    PageScope {
        // Header section
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        ) {
            Heading2("Achievements")
            Body1("Track your progress and earn rewards")
            Divider(Modifier.padding(top = 16.dp))
        }

        // Achievements list
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Group achievements by category
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

/**
 * Composable function for displaying a category header.
 *
 * @param category The category of achievements to display.
 */
@Composable
private fun CategoryHeader(category: AchievementCategory) {
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

/**
 * Composable function for displaying an individual achievement.
 *
 * @param achievement The achievement to display.
 */
@Composable
private fun AchievementItem(achievement: Achievement) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(8.dp),
        border =
            if (achievement.isUnlocked) {
                BorderStroke(1.dp, MaterialTheme.colors.primary)
            } else {
                null
            },
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Status icon
            Box(
                modifier =
                    Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (achievement.isUnlocked) {
                                MaterialTheme.colors.primary
                            } else {
                                Color.Gray.copy(alpha = 0.3f)
                            },
                        ),
                contentAlignment = Alignment.Center,
            ) {
                if (achievement.isUnlocked) {
                    // Draw a checkmark using Canvas
                    Canvas(modifier = Modifier.size(24.dp)) {
                        val path =
                            Path().apply {
                                moveTo(size.width * 0.2f, size.height * 0.5f)
                                lineTo(size.width * 0.45f, size.height * 0.75f)
                                lineTo(size.width * 0.8f, size.height * 0.25f)
                            }
                        drawPath(
                            path = path,
                            color = Color.White,
                            style = Stroke(width = size.width * 0.1f, cap = StrokeCap.Round),
                        )
                    }
                } else {
                    // Draw a simple lock shape using Canvas
                    Canvas(modifier = Modifier.size(24.dp)) {
                        // Draw the lock body
                        drawRect(
                            color = Color.Gray,
                            topLeft = Offset(size.width * 0.25f, size.height * 0.4f),
                            size = Size(size.width * 0.5f, size.height * 0.5f),
                        )
                        // Draw the lock shackle
                        drawArc(
                            color = Color.Gray,
                            startAngle = 0f,
                            sweepAngle = 180f,
                            useCenter = false,
                            topLeft = Offset(size.width * 0.25f, size.height * 0.15f),
                            size = Size(size.width * 0.5f, size.height * 0.5f),
                            style = Stroke(width = size.width * 0.1f),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Achievement details
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = achievement.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )

                Text(
                    text = achievement.description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Reward: ${achievement.reward}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.primary,
                )
            }
        }
    }
}
