package shampoo.luxury.leviathan.pet

/**
 * Filters the list of pets by their name.
 *
 * @param name The name to filter pets by.
 * @return A list of pets that match the given name.
 */
fun List<Pet>.filterByName(name: String): List<Pet> = this.filter { it.name == name }

/**
 * Adds a new pet to the mutable list.
 *
 * @param name The name of the pet to add.
 * @param resourcePath The relative path to the pet's resource in the JAR.
 * @param cost The cost of the pet. Default is 0.0.
 */
fun MutableList<Pet>.addPet(
    name: String,
    resourcePath: String,
    cost: Double = 0.0,
) {
    this.add(Pet(name, resourcePath, cost))
}
