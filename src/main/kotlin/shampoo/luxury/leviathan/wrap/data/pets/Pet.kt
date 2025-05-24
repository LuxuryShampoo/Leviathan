package shampoo.luxury.leviathan.wrap.data.pets

import shampoo.luxury.leviathan.global.Resource
import java.io.Serializable

/**
 * A data class representing a pet with its associated information.
 *
 * @property name The name of the pet.
 * @property resourcePath The relative path to the pet's resource in the JAR.
 * @property cost The cost of the pet.
 */
data class Pet(
    val name: String,
    internal val resourcePath: String,
    val cost: Double = 0.0,
) : Serializable {
    /**
     * Resolves the local file path for the pet's resource using `extractResourceToLocal`.
     * The value is computed once and cached.
     */
    val localPath: String by lazy { Resource.extractResourceToLocal(resourcePath).absolutePath }
}
