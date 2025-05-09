package shampoo.luxury.leviathan.global

import shampoo.luxury.leviathan.global.Resource.getLocalResourcePath
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

    const val BOB = "https://omydagreat.github.io/FileHosting/FileHosting/BobAlarm.png"
    const val RISHI = "https://rishthewizard.github.io/Icarus/assets/madTesting.png"

    val allPets =
        pets {
            pet {
                name = "Bob"
                url = BOB
                local = getLocalResourcePath("BobAlarm.png")
                cost = 50.0
            }
            pet {
                name = "Rishi"
                url = RISHI
                local = getLocalResourcePath("Rishi.png")
                cost = -1.0
            }
            pet {
                name = "BobAlarm2"
                url = BOB
                local = getLocalResourcePath("BobAlarm2.png")
                cost = 51.0
            }
            pet {
                name = "BobAlarm3"
                url = BOB
                local = getLocalResourcePath("BobAlarm3.png")
                cost = 52.0
            }
            pet {
                name = "BobAlarm4"
                url = BOB
                local = getLocalResourcePath("BobAlarm4.png")
                cost = 53.0
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
