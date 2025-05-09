package shampoo.luxury.leviathan.wrap

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.readRawBytes
import io.ktor.http.HttpHeaders.Accept
import io.ktor.http.URLBuilder
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import shampoo.luxury.leviathan.global.Values.Prefs.speakPreference
import java.io.ByteArrayInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.SourceDataLine

/**
 * Client for the OpenTTS API
 */
private val client =
    HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                },
            )
        }
        expectSuccess = true

        engine {
            requestTimeout = 60000
        }
    }

/**
 * Base URL for the OpenTTS API
 */
private const val BASE_URL = "https://tts.malefic.xyz"

/**
 * Function to play WAV audio from a byte array
 *
 * @param audioData The WAV audio data as a byte array
 */
private fun playWavAudio(audioData: ByteArray) {
    try {
        val audioInputStream = AudioSystem.getAudioInputStream(ByteArrayInputStream(audioData))
        val format = audioInputStream.format
        val info = DataLine.Info(SourceDataLine::class.java, format)

        val line = AudioSystem.getLine(info) as SourceDataLine
        line.open(format)
        line.start()

        val buffer = ByteArray(4096)
        var bytesRead = 0

        while (audioInputStream.read(buffer).also { bytesRead = it } != -1) {
            line.write(buffer, 0, bytesRead)
        }

        line.drain()
        line.stop()
        line.close()
        audioInputStream.close()
    } catch (e: Exception) {
        Logger.e("Error playing audio: ${e.message}")
        e.printStackTrace()
    }
}

/**
 * Data class representing a voice in the OpenTTS API
 */
@Serializable
data class Voice(
    val id: String,
    val name: String,
    val language: String? = null,
    val locale: String? = null,
    val gender: String? = null,
    @SerialName("tts_name") val ttsName: String? = null,
)

/**
 * Function to speak text using the OpenTTS API
 *
 * @param text The text to speak
 * @param voice The voice to use (default: "espeak:en")
 * @param vocoder The vocoder quality (optional)
 * @param denoiserStrength The strength of the vocoder denoiser (optional)
 * @param cache Whether to use the WAV cache (optional)
 */
suspend fun speak(
    text: String,
    voice: String = "espeak:en",
    vocoder: String? = null,
    denoiserStrength: Float? = null,
    cache: Boolean? = null,
) {
    if (!speakPreference) {
        Logger.i("Speak preference is disabled. Skipping TTS.")
        return
    }

    try {
        val url =
            URLBuilder(BASE_URL)
                .apply {
                    path("api", "tts")
                    parameters.apply {
                        append("text", text)
                        append("voice", voice)
                        vocoder?.let { append("vocoder", it) }
                        denoiserStrength?.let { append("denoiserStrength", it.toString()) }
                        cache?.let { append("cache", it.toString()) }
                    }
                }.build()

        val response =
            client.get(url) {
                headers {
                    append(Accept, "audio/wav")
                }
            }

        val audioBytes = response.readRawBytes()

        playWavAudio(audioBytes)
    } catch (e: Exception) {
        Logger.e("Error speaking text: ${e.message}")
        e.printStackTrace()
    }
}

/**
 * Function to get available voices from the OpenTTS API
 *
 * @param language Filter based on language(s) (optional)
 * @param locale Filter based on locale(s) (optional)
 * @param gender Filter based on gender(s) (optional)
 * @param ttsName Filter based on TTS system name(s) (optional)
 * @return A map of voice IDs to Voice objects
 */
suspend fun getVoices(
    language: String? = null,
    locale: String? = null,
    gender: String? = null,
    ttsName: String? = null,
): Map<String, Voice> {
    try {
        val url =
            URLBuilder(BASE_URL)
                .apply {
                    path("api", "voices")
                    parameters.apply {
                        language?.let { append("language", it) }
                        locale?.let { append("locale", it) }
                        gender?.let { append("gender", it) }
                        ttsName?.let { append("tts_name", it) }
                    }
                }.build()

        return client.get(url).body()
    } catch (e: Exception) {
        Logger.e("Error getting voices: ${e.message}")
        e.printStackTrace()
        return emptyMap()
    }
}

/**
 * Function to get available languages from the OpenTTS API
 *
 * @param ttsName Filter based on TTS system name(s) (optional)
 * @return A list of available languages
 */
suspend fun getLanguages(ttsName: String? = null): List<String> {
    try {
        val url =
            URLBuilder(BASE_URL)
                .apply {
                    path("api", "languages")
                    parameters.apply {
                        ttsName?.let { append("tts_name", it) }
                    }
                }.build()

        return client.get(url).body()
    } catch (e: Exception) {
        Logger.e("Error getting languages: ${e.message}")
        e.printStackTrace()
        return emptyList()
    }
}

/**
 * Main function for testing the TTS functionality
 */
suspend fun main() {
    speak("Hello, this is a test of the OpenTTS API.")

    val voices = getVoices()
    Logger.i("Available voices: $voices")

    val languages = getLanguages()
    Logger.i("Available languages: $languages")
}
