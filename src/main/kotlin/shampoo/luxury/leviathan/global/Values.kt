package shampoo.luxury.leviathan.global

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import shampoo.luxury.leviathan.wrap.data.achievements.Achievement
import shampoo.luxury.leviathan.wrap.data.achievements.AchievementCategory
import shampoo.luxury.leviathan.wrap.data.pets.Pet
import shampoo.luxury.leviathan.wrap.data.pets.getOwnedPets
import shampoo.luxury.leviathan.wrap.data.settings.getBooleanSetting
import shampoo.luxury.leviathan.wrap.data.settings.setBooleanSetting
import xyz.malefic.compose.prefs.collection.PersistentHashSet
import xyz.malefic.compose.prefs.delegate.IntPreference
import java.util.prefs.Preferences
import java.util.prefs.Preferences.userRoot

object Values {
    object Prefs {
        val prefs: Preferences = userRoot().node("leviathan")

        suspend fun getSpeakSetting(): Boolean = getBooleanSetting("speak_enabled", true)

        suspend fun setSpeakSetting(value: Boolean) = setBooleanSetting("speak_enabled", value)

        suspend fun getListenSetting(): Boolean = getBooleanSetting("listen_enabled", true)

        suspend fun setListenSetting(value: Boolean) = setBooleanSetting("listen_enabled", value)
    }

    var user by IntPreference("current_user", -1)

    private val _selectedPet = MutableStateFlow<Pet?>(null)
    val selectedPet: StateFlow<Pet?> = _selectedPet

    suspend fun updateSelectedPet() {
        val pets = getOwnedPets()
        _selectedPet.value = pets.firstOrNull()
    }

    val completedAchievements =
        PersistentHashSet<String>(
            "completedAchievements",
            Prefs.prefs,
        )

    val allAchievements =
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
}
