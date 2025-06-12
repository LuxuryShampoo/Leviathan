package shampoo.luxury.leviathan.global

import java.io.File
import java.io.FileOutputStream
import java.net.URI

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
        val safePath = path.replace("/", File.separator)
        return "${
            when {
                osName.contains("win") -> System.getenv("APPDATA") ?: "$userHome${File.separator}AppData${File.separator}Roaming"
                osName.contains("mac") -> "$userHome${File.separator}Library${File.separator}Application Support"
                else -> "$userHome${File.separator}.config"
            }
        }${File.separator}Leviathan${File.separator}$safePath"
    }

    /**
     * Extracts a resource from the JAR file and saves it to a local destination.
     *
     * @param resourcePath The path to the resource within the JAR file.
     * @param overwrite If true, the resource will be extracted even if the file already exists. Defaults to false.
     * @return The `File` object representing the extracted resource.
     */
    fun extractResourceToLocal(
        resourcePath: String,
        overwrite: Boolean = false,
    ): File {
        val destinationPath = getLocalResourcePath(resourcePath)
        val outputFile = File(destinationPath)
        if (outputFile.exists() && !overwrite) {
            return outputFile
        }
        outputFile.parentFile.mkdirs()
        val resourceStream =
            object {}.javaClass.getResourceAsStream("/$resourcePath")
                ?: throw IllegalArgumentException("Resource not found: /$resourcePath")
        FileOutputStream(outputFile).use { out ->
            resourceStream.use { it.copyTo(out) }
        }
        return outputFile
    }

    /**
     * Downloads a file from the given URL and saves it to the local resource path.
     *
     * @param url The URL to download the file from.
     * @param destinationPath The relative path to save the file under the local resource directory.
     * @param overwrite If true, the file will be downloaded even if it already exists. Defaults to false.
     * @return The `File` object representing the downloaded file.
     */
    fun downloadFileToLocal(
        url: String,
        destinationPath: String,
        overwrite: Boolean = false,
    ): File {
        val outputFile = File(getLocalResourcePath(destinationPath))
        if (outputFile.exists() && !overwrite) {
            return outputFile
        }
        outputFile.parentFile.mkdirs()
        URI(url).toURL().openStream().use { input ->
            FileOutputStream(outputFile).use { output ->
                input.copyTo(output)
            }
        }
        return outputFile
    }
}
