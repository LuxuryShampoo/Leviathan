package shampoo.luxury.pet

/**
 * Filters the list of pets by their name.
 *
 * @param name The name to filter pets by.
 * @return A list of pets that match the given name.
 */
fun List<Pet>.filterByName(name: String): List<Pet> = this.filter { it.name == name }

/**
 * Filters the list of pets by their URL.
 *
 * @param url The URL to filter pets by.
 * @return A list of pets that match the given URL.
 */
fun List<Pet>.filterByUrl(url: String): List<Pet> = this.filter { it.url == url }

/**
 * Adds a new pet to the mutable list.
 *
 * @param name The name of the pet to add.
 * @param url The URL associated with the pet.
 * @param local The local path for the pet.
 */
fun MutableList<Pet>.addPet(
    name: String,
    url: String,
    local: String,
) {
    this.add(Pet(name, url, local))
}

/**
 * Removes all pets with the specified name from the mutable list.
 *
 * @param name The name of the pets to remove.
 */
fun MutableList<Pet>.removeByName(name: String) {
    this.removeAll { it.name == name }
}
