package shampoo.luxury.leviathan.components.tasks

import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceBetween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import xyz.malefic.compose.comps.text.typography.Body1
import xyz.malefic.compose.comps.text.typography.Body2

/**
 * A composable function that represents a single tasks item in a tasks list.
 *
 * @param task A map containing tasks details. Expected keys:
 *  - "id" (Int): The unique identifier of the tasks.
 *  - "title" (String): The title of the tasks.
 *  - "description" (String?): An optional description of the tasks.
 *  - "isCompleted" (Boolean): Whether the tasks is completed.
 * @param onTaskCompleted A callback invoked when the tasks's completion status changes.
 *  - Parameters: The tasks ID (Int) and the new completion status (Boolean).
 */
@Composable
fun TaskItem(
    task: Map<String, Any?>,
    onTaskCompleted: (Int, Boolean) -> Unit,
) {
    val id = task["id"] as Int
    val title = task["title"] as String
    val description = task["description"] as? String
    val isCompleted = task["isCompleted"] as Boolean

    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
        SpaceBetween,
        CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Body1(title)
            description?.let {
                Body2(it, Modifier.padding(top = 4.dp))
            }
        }
        Row(verticalAlignment = CenterVertically) {
            Checkbox(
                isCompleted,
                { onTaskCompleted(id, it) },
            )
        }
    }
}
