package shampoo.luxury.leviathan.wrap.data.pets

import java.io.File
import java.io.Serializable

// Minimal stub for Resource to extract a resource to local file.
object Resource {
    fun extractResourceToLocal(resourcePath: String): File = File(resourcePath)
}

data class Pet(
    val name: String,
    internal val resourcePath: String,
    val cost: Double = 0.0,
    var level: Int = 1, // pet level (starts at 1)
    var foodFed: Int = 0, // count of food fed towards next level
) : Serializable {
    // Match the original serialVersionUID to maintain compatibility with existing serialized data
    companion object {
        private const val serialVersionUID = -469598414369076742L
    }

    val localPath: String by lazy { Resource.extractResourceToLocal(resourcePath).absolutePath }
}
