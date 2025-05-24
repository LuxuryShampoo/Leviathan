package shampoo.luxury.leviathan.global

import shampoo.luxury.leviathan.wrap.data.pets.ownedPets
import shampoo.luxury.leviathan.wrap.data.settings.SettingsDelegate
import xyz.malefic.compose.prefs.delegate.IntPreference
import java.util.prefs.Preferences
import java.util.prefs.Preferences.userRoot

object Values {
    object Prefs {
        val prefs: Preferences = userRoot().node("leviathan")

        var speakSetting by SettingsDelegate("speak_enabled", true)
        var listenSetting by SettingsDelegate("listen_enabled", true)
    }

    var user by IntPreference("current_user", -1)

    val selectedPet by lazy { ownedPets.first() }
}
