package shampoo.luxury.leviathan.components.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import shampoo.luxury.leviathan.wrap.data.tasks.Task
import xyz.malefic.compose.comps.text.typography.Body1
import androidx.compose.foundation.lazy.items as iii

@Composable
fun TaskList(
    tasks: List<Task>,
    onTaskCompleted: (Int, Boolean) -> Unit,
    onDeleteTask: (Int) -> Unit,
) {
    LazyColumn(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        iii(tasks) { task ->
            TaskItem(
                mapOf(
                    "id" to task.id,
                    "title" to task.title,
                    "description" to task.description,
                    "isCompleted" to task.isCompleted,
                ),
                { id, isCompleted -> onTaskCompleted(id, isCompleted) },
                { id -> onDeleteTask(id) },
            )
        }.takeUnless { tasks.isEmpty() } ?: run {
            item {
                Body1("No tasks available")
            }
        }
    }
}
