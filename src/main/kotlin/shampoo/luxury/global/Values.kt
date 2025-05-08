package shampoo.luxury.global

import shampoo.luxury.global.Resource.getLocalResourcePath
import shampoo.luxury.pet.Pet
import shampoo.luxury.pet.pets
import xyz.malefic.compose.prefs.collection.PersistentArrayList
import xyz.malefic.compose.prefs.delegate.BooleanPreference
import xyz.malefic.compose.prefs.delegate.SerializablePreference

object Values {
    var speakPreference by BooleanPreference("speak_enabled", true)
    var listenPreference by BooleanPreference("listen_enabled", true)
    const val BOB = "https://gallery.malefic.xyz/photos/Leviathan/BobAlarm.png"
    const val CAT = "https://gallery.malefic.xyz/photos/Leviathan/MaineCoon.png"
    val allPets =
        pets {
            pet {
                name = "Bob"
                url = BOB
                local = getLocalResourcePath("BobAlarm.png")
            }
            pet {
                name = "Cat"
                url = CAT
                local = getLocalResourcePath("MaineCoon.png")
            }
            pet {
                name = "BobAlarm2"
                url = BOB
                local = getLocalResourcePath("BobAlarm2.png")
            }
            pet {
                name = "BobAlarm3"
                url = BOB
                local = getLocalResourcePath("BobAlarm3.png")
            }
            pet {
                name = "BobAlarm4"
                url = BOB
                local = getLocalResourcePath("BobAlarm4.png")
            }
        }
    val ownedPets =
        PersistentArrayList<Pet>("ownedPets").also { list ->
            if (list.isEmpty()) {
                list.addAll(
                    allPets.filter { pet ->
                        pet.name == "Bob"
                    },
                )
            }
        }
    val selectedPet by SerializablePreference("selectedPet", allPets[0])
}
