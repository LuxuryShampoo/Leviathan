package shampoo.luxury.global

import shampoo.luxury.global.Resource.getLocalResourcePath
import shampoo.luxury.pet.Pet
import shampoo.luxury.pet.pets
import xyz.malefic.compose.prefs.collection.PersistentArrayList
import xyz.malefic.compose.prefs.delegate.SerializablePreference

object Values {
    const val BOB_ALARM = "https://gallery.malefic.xyz/photos/Leviathan/BobAlarm.png"
    val allPets =
        pets {
            pet {
                name = "BobAlarm"
                url = BOB_ALARM
                local = getLocalResourcePath("BobAlarm.png")
            }
            pet {
                name = "BobAlarm1"
                url = BOB_ALARM
                local = getLocalResourcePath("BobAlarm1.png")
            }
            pet {
                name = "BobAlarm2"
                url = BOB_ALARM
                local = getLocalResourcePath("BobAlarm2.png")
            }
            pet {
                name = "BobAlarm3"
                url = BOB_ALARM
                local = getLocalResourcePath("BobAlarm3.png")
            }
            pet {
                name = "BobAlarm4"
                url = BOB_ALARM
                local = getLocalResourcePath("BobAlarm4.png")
            }
        }
    val ownedPets =
        PersistentArrayList<Pet>("ownedPets").also {
            if (it.isEmpty()) {
                it.addAll(
                    allPets.filter {
                        it.name == "BobAlarm"
                    },
                )
            }
        }
    val selectedPet by SerializablePreference("selectedPet", allPets[0])
}
