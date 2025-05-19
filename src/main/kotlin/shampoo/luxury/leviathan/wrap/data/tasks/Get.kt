package shampoo.luxury.leviathan.wrap.data.tasks

import io.ktor.client.call.body
import io.ktor.client.request.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import shampoo.luxury.leviathan.wrap.data.TiApiClient
import shampoo.luxury.leviathan.wrap.data.TiApiClient.baseUrl

@Serializable
data class Task(
    val id: String,
    val title: String,
    val description: String?,
    @SerialName("is_completed") val isCompleted: String,
)

@Serializable
data class ApiResponse(
    val type: String,
    val data: Data,
)

@Serializable
data class Data(
    val columns: List<Column>,
    val rows: List<Task>,
    val result: Result,
)

@Serializable
data class Column(
    val col: String,
    @SerialName("data_type") val dataType: String,
    val nullable: Boolean,
)

@Serializable
data class Result(
    val code: Int,
    val message: String,
    @SerialName("start_ms") val startMS: Long,
    @SerialName("end_ms") val endMS: Long,
    val latency: String,
    @SerialName("row_count") val rowCount: Int,
    @SerialName("row_affect") val rowAffect: Int,
    val limit: Int,
)

suspend fun fetchTasks(): List<Task> {
    val response: ApiResponse = TiApiClient.client.get("$baseUrl/tasks/get").body()
    return response.data.rows
}
