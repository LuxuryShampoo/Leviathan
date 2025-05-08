package shampoo.luxury.wrap

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

/**
 * Data class representing a message in the HackClub AI API request
 */
@Serializable
data class Message(
    val role: String,
    val content: String,
)

/**
 * Data class representing the request body for the HackClub AI API
 */
@Serializable
data class HackClubRequest(
    val messages: List<Message>,
)

/**
 * Data class representing usage breakdown in the HackClub AI API response
 */
@Serializable
data class UsageBreakdown(
    val models: JsonElement? = null,
)

/**
 * Data class representing x_groq information in the HackClub AI API response
 */
@Serializable
data class XGroq(
    val id: String,
)

/**
 * Data class representing the response from the HackClub AI API
 */
@Serializable
data class HackClubResponse(
    val id: String,
    @SerialName("object") val objectType: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>,
    @SerialName("system_fingerprint") val systemFingerprint: String? = null,
    @SerialName("usage_breakdown") val usageBreakdown: UsageBreakdown? = null,
    @SerialName("x_groq") val xGroq: XGroq? = null,
)

/**
 * Data class representing a choice in the HackClub AI API response
 */
@Serializable
data class Choice(
    val index: Int,
    val message: Message,
    @SerialName("finish_reason") val finishReason: String,
    val logprobs: JsonElement? = null,
)

/**
 * HTTP client for making requests to the HackClub AI API
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
    }

/**
 * Function to parse a string and get a response from the HackClub AI API
 *
 * @param input The string to send to the HackClub AI API
 * @return The response from the HackClub AI API
 */
suspend fun parse(input: String): String {
    val request =
        HackClubRequest(
            messages =
                listOf(
                    Message(role = "user", content = input),
                ),
        )

    val response: HackClubResponse =
        client
            .post("https://ai.hackclub.com/chat/completions") {
                contentType(Json)
                setBody(request)
            }.body()

    return response.choices
        .firstOrNull()
        ?.message
        ?.content ?: "No response from AI"
}

suspend fun main() {
    val input = "Hello, HackClub AI!"
    val response = parse(input)
    Logger.i(response)
}
