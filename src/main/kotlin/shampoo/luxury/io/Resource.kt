package shampoo.luxury.io

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.get
import io.ktor.client.statement.readRawBytes
import java.io.File
import java.io.File.separator

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
                osName.contains("win") -> System.getenv("APPDATA") ?: "$userHome${separator}AppData${separator}Roaming"
                osName.contains("mac") -> "$userHome${separator}Library${separator}Application Support"
                else -> "$userHome$separator.config"
            }
        }${separator}Leviathan${separator}$path"
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
            Logger.d("File already exists.")
            return file
        }

        Logger.d("Downloading file ${file.name}.")
        HttpClient(CIO) {
            install(HttpTimeout) {
                requestTimeoutMillis = 120000
            }
        }.use { client ->
            val fileBytes: ByteArray = client.get(fileURL).readRawBytes()
            file.writeBytes(fileBytes)
            Logger.d("File downloaded successfully.")
        }
        return file
    }

    const val BOB_ALARM = "https://gallery.malefic.xyz/photos/Leviathan/BobAlarm.png"
}
