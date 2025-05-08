package shampoo.luxury.global

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.get
import io.ktor.client.statement.readRawBytes
import java.io.File

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
     * Downloads a file from the specified URL and saves it to the given destination path.
     *
     * @param fileURL The URL of the file to download.
     * @param destinationPath The path where the downloaded file will be saved.
     * @return The downloaded file.
     */
    suspend fun downloadFile(
        fileURL: String,
        destinationPath: String,
        update: Boolean = false,
    ): File {
        val file = File(destinationPath)
        file.parentFile?.mkdirs()
        if (file.exists() && !update) {
            Logger.Companion.d("File already exists.")
            return file
        }

        Logger.Companion.d("Downloading file ${file.name}.")
        HttpClient(CIO) {
            install(HttpTimeout) {
                requestTimeoutMillis = 120000
            }
        }.use { client ->
            val fileBytes: ByteArray = client.get(fileURL).readRawBytes()
            file.writeBytes(fileBytes)
            Logger.Companion.d("File downloaded successfully.")
        }
        return file
    }
}
