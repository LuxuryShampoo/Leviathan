package shampoo.luxury.leviathan.global

import shampoo.luxury.leviathan.pet.Pet
import shampoo.luxury.leviathan.pet.pets
import xyz.malefic.compose.prefs.collection.PersistentHashSet
import xyz.malefic.compose.prefs.delegate.BooleanPreference
import xyz.malefic.compose.prefs.delegate.SerializablePreference
import java.util.prefs.Preferences
import java.util.prefs.Preferences.userRoot

object Values {
    object Prefs {
        val prefs: Preferences = userRoot().node("leviathan")
        var speakPreference by BooleanPreference("speak_enabled", true, prefs)
        var listenPreference by BooleanPreference("listen_enabled", true, prefs)
    }

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
