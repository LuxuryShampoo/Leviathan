package shampoo.luxury.leviathan.global

import shampoo.luxury.leviathan.pet.Pet
import shampoo.luxury.leviathan.pet.pets
import shampoo.luxury.leviathan.wrap.data.settings.SettingsDelegate
import xyz.malefic.compose.prefs.collection.PersistentHashSet
import xyz.malefic.compose.prefs.delegate.IntPreference
import xyz.malefic.compose.prefs.delegate.SerializablePreference
import java.util.prefs.Preferences
import java.util.prefs.Preferences.userRoot

object Values {
    object Prefs {
        val prefs: Preferences = userRoot().node("leviathan")

        var speakSetting by SettingsDelegate("speak_enabled", true)
        var listenSetting by SettingsDelegate("listen_enabled", true)
    }

    var user by IntPreference("current_user", -1)

    val allPets =
        pets {
            pet {
                name = "Bob"
                resourcePath = "image/BobAlarm.png"
                cost = 50.0
            }
            pet {
                name = "Rishi"
                resourcePath = "image/Rishi.png"
                cost = -1.0
            }
            pet {
                name = "MaineCoon"
                resourcePath = "image/MaineCoon.png"
                cost = 101.0
            }
            pet {
                name = "Supreme"
                resourcePath = "image/Supreme.png"
                cost = 250.0
            }
            pet {
                name = "Beluga"
                resourcePath = "image/Beluga.png"
                cost = 75.0
            }
            pet {
                name = "Phat"
                resourcePath = "image/Phat.png"
                cost = 27.0
            }
        }

    val ownedPets =
        PersistentHashSet<Pet>("ownedPets", Prefs.prefs).also { set ->
            if (set.isEmpty()) {
                set.addAll(
                    allPets.filter { pet ->
                        pet.name == "Bob"
                    },
                )
            }
        }

    val unownedPets: Set<Pet>
        get() = allPets.toHashSet().apply { removeAll(ownedPets) }

    val selectedPet by SerializablePreference("selectedPet", ownedPets.first())
}
