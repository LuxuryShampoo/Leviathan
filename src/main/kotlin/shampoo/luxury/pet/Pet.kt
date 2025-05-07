package shampoo.luxury.pet

/**
 * A data class representing a pet with its associated information.
 *
 * @property name The name of the pet.
 * @property url The URL pointing to the pet's online image.
 * @property local The local file path where the pet's image is stored.
 */
data class Pet(
    val name: String,
    val url: String,
    val local: String,
)
