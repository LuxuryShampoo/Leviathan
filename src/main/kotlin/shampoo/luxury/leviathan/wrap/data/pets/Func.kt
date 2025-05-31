package shampoo.luxury.leviathan.wrap.data.pets

import kotlinx.coroutines.Dispatchers.IO
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import shampoo.luxury.leviathan.global.Values.updateSelectedPet
import shampoo.luxury.leviathan.global.Values.user
import shampoo.luxury.leviathan.wrap.data.currency.addToBalance

/**
 * Initializes the pets in the database.
 * If the pets table is empty, it populates it with a predefined list of pets.
 * The pet "Bob" is marked as owned by default.
 */
suspend fun initializePets() {
    newSuspendedTransaction(IO) {
        if (Pets.selectAll().empty()) {
            listOf(
                Pet("Rishi", "image/Rishi.png", -1.0),
                Pet("Phat", "image/Phat.png", 27.0),
                Pet("Bob", "image/BobAlarm.png", 50.0),
                Pet("Beluga", "image/Beluga.png", 75.0),
                Pet("MaineCoon", "image/MaineCoon.png", 101.0),
                Pet("Supreme", "image/Supreme.png", 250.0),
            ).forEach { pet ->
                Pets.insert {
                    it[userId] = user
                    it[name] = pet.name
                    it[resourcePath] = pet.resourcePath
                    it[cost] = pet.cost
                    it[owned] = pet.name == "Bob"
                }
            }
        }
    }
    updateSelectedPet()
}

/**
 * Retrieves all pets associated with the current user.
 * @return A list of all pets belonging to the user.
 */
suspend fun getAllPets() =
    buildList {
        newSuspendedTransaction(IO) {
            Pets.selectAll().where { Pets.userId eq user }.forEach {
                add(Pet(it[Pets.name], it[Pets.resourcePath], it[Pets.cost]))
            }
        }
    }

/**
 * Retrieves all pets owned by the current user.
 * @return A list of owned pets.
 */
suspend fun getOwnedPets() =
    buildList {
        newSuspendedTransaction(IO) {
            Pets.selectAll().where { (Pets.userId eq user) and (Pets.owned eq true) }.forEach {
                add(Pet(it[Pets.name], it[Pets.resourcePath], it[Pets.cost]))
            }
        }
    }

/**
 * Retrieves all pets not owned by the current user.
 * @return A list of unowned pets.
 */
suspend fun getUnownedPets() =
    buildList {
        newSuspendedTransaction(IO) {
            Pets.selectAll().where { (Pets.userId eq user) and (Pets.owned eq false) }.forEach {
                add(Pet(it[Pets.name], it[Pets.resourcePath], it[Pets.cost]))
            }
        }
    }

/**
 * Purchases a pet for the current user.
 * Deducts the pet's cost from the user's balance and marks the pet as owned in the database.
 *
 * @param pet The pet to be purchased.
 */
suspend fun buyPet(pet: Pet) =
    newSuspendedTransaction(IO) {
        addToBalance(-pet.cost)
        Pets.update({ (Pets.name eq pet.name) and (Pets.userId eq user) }) {
            it[owned] = true
        }
    }
