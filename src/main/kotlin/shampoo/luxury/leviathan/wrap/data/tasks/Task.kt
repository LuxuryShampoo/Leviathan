package shampoo.luxury.leviathan.wrap.data.tasks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: Int,
    val title: String,
    val description: String?,
    @SerialName("is_completed") val isCompleted: Boolean,
)
