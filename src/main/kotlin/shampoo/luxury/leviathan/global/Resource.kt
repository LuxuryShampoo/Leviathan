package shampoo.luxury.leviathan.global

import io.ktor.client.request.get
import java.io.File
import kotlin.io.copyTo

/** Utility object for handling resource paths. */
object Resource {
    /**
     * Returns the local resource path for the given relative path.
     *
     * @param path The relative path to the resource.
     * @return The absolute path to the resource in the appropriate config directory.
     */
    fun getLocalResourcePath(path: String): String {
        val osName = System.getProperty("os.name").lowercase()
        val userHome = System.getProperty("user.home")

        return "${
            when {
                osName.contains("win") -> System.getenv("APPDATA") ?: "$userHome${File.separator}AppData${File.separator}Roaming"
                osName.contains("mac") -> "$userHome${File.separator}Library${File.separator}Application Support"
                else -> "$userHome${File.separator}.config"
            }
        }${File.separator}Leviathan${File.separator}$path"
    }

    /**
     * Extracts a resource from the JAR file and saves it to a local destination.
     *
     * @param resourcePath The path to the resource within the JAR file.
     * @param destinationPath The relative path where the resource will be saved locally.
     * @param overwrite If true, the resource will be extracted even if the file already exists. Defaults to false.
     * @return The `File` object representing the extracted resource.
     */
    fun extractResourceToLocal(
        resourcePath: String,
        destinationPath: String = resourcePath,
        overwrite: Boolean = false,
    ): File {
        val destinationFile = File(getLocalResourcePath(destinationPath))

        if (destinationFile.exists() && !overwrite) {
            return destinationFile
        }

        val inputStream = ClassLoader.getSystemResourceAsStream(resourcePath)
        requireNotNull(inputStream) { "Resource $resourcePath not found in JAR." }

        destinationFile.parentFile?.mkdirs()
        inputStream.buffered().use { input ->
            destinationFile.outputStream().buffered().use { output ->
                input.copyTo(output)
            }
        }
        return destinationFile
    }
}
