package shampoo.luxury.leviathan.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import shampoo.luxury.leviathan.wrap.data.tasks.Task
import shampoo.luxury.leviathan.wrap.data.tasks.TaskItem

@Composable
fun TaskList(
    tasks: List<Task>,
    onTaskCompleted: (Int, Boolean) -> Unit,
    onDeleteTask: (Int) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(tasks) { task ->
            TaskItem(
                mapOf(
                    "id" to task.id.toInt(),
                    "title" to task.title,
                    "description" to task.description,
                    "isCompleted" to (task.isCompleted == "1"),
                ),
                { id, isCompleted -> onTaskCompleted(id, isCompleted) },
                { id -> onDeleteTask(id) },
            )
        }
    }
}
