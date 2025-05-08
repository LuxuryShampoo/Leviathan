package shampoo.luxury.global

import shampoo.luxury.global.Resource.getLocalResourcePath
import shampoo.luxury.pet.Pet
import shampoo.luxury.pet.pets
import xyz.malefic.compose.prefs.collection.PersistentArrayList
import xyz.malefic.compose.prefs.delegate.SerializablePreference

object Values {
    const val BOB = "https://gallery.malefic.xyz/photos/Leviathan/BobAlarm.png"
    val allPets =
        pets {
            pet {
                name = "Bob"
                url = BOB
                local = getLocalResourcePath("BobAlarm.png")
            }
            pet {
                name = "BobAlarm1"
                url = BOB
                local = getLocalResourcePath("BobAlarm1.png")
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
        PersistentArrayList<Pet>("ownedPets").also {
            if (it.isEmpty()) {
                it.addAll(
                    allPets.filter {
                        it.name == "Bob"
                    },
                )
            }
        }
    val selectedPet by SerializablePreference("selectedPet", allPets[0])
}
