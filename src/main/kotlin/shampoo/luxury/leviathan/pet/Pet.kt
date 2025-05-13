package shampoo.luxury.leviathan.pet

import shampoo.luxury.leviathan.global.Resource.extractResourceToLocal
import java.io.Serializable as JavaSerializable

/**
 * A data class representing a pet with its associated information.
 *
 * @property name The name of the pet.
 * @property resourcePath The relative path to the pet's resource in the JAR.
 * @property cost The cost of the pet.
 */
data class Pet(
    val name: String,
    private val resourcePath: String,
    val cost: Double = 0.0,
) : JavaSerializable {
    /**
     * Resolves the local file path for the pet's resource using `extractResourceToLocal`.
     * The value is computed once and cached.
     */
    val localPath: String by lazy { extractResourceToLocal(resourcePath).absolutePath }
}

/**
 * Builder class for creating a single `Pet` object.
 *
 * @property name The name of the pet.
 * @property resourcePath The relative path to the pet's resource in the JAR.
 * @property cost The cost of the pet.
 */
@PetDsl
class PetBuilder {
    /** The name of the pet. */
    var name = ""

    /** The relative path to the pet's resource in the JAR. */
    var resourcePath = ""

    /** The cost of the pet. */
    var cost = 0.0

    /**
     * Builds and returns a `Pet` object using the current properties.
     *
     * @return A new `Pet` instance.
     */
    fun build() = Pet(name, resourcePath, cost)
}

/**
 * Annotation to mark DSL-specific classes and functions.
 * Ensures that the DSL scope is properly restricted.
 */
@DslMarker
annotation class PetDsl

/**
 * Builder class for creating a list of `Pet` objects.
 */
@PetDsl
class PetsBuilder {
    private val pets = mutableSetOf<Pet>()

    /**
     * Adds a new `Pet` to the list by applying the given DSL block.
     *
     * @param block A DSL block to configure the `PetBuilder`.
     */
    fun pet(block: PetBuilder.() -> Unit) {
        val petBuilder = PetBuilder().apply(block)
        pets.add(petBuilder.build())
    }

    /**
     * Builds and returns the list of `Pet` objects.
     *
     * @return A list of `Pet` instances.
     */
    fun build() = pets
}

/**
 * Top-level DSL function to create a list of `Pet` objects.
 *
 * @param block A DSL block to configure the `PetsBuilder`.
 * @return A list of `Pet` instances.
 */
fun pets(block: PetsBuilder.() -> Unit) = PetsBuilder().apply(block).build()
