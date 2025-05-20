package shampoo.luxury.leviathan.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import xyz.malefic.compose.comps.text.typography.Body1

@Composable
fun TaskInputForm(
    title: String,
    description: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onAddTask: () -> Unit,
) {
    Column(
        Modifier.fillMaxWidth(),
        Arrangement.spacedBy(8.dp),
    ) {
        TextField(
            title,
            onTitleChange,
            Modifier.fillMaxWidth(),
            label = { Body1("Task Title") },
            placeholder = { Body1("Enter tasks title") },
        )
        TextField(
            description,
            onDescriptionChange,
            Modifier.fillMaxWidth(),
            label = { Body1("Task Description") },
            placeholder = { Body1("Enter tasks description") },
        )
        Button(
            onAddTask,
            Modifier.align(Alignment.End),
        ) {
            Body1("Add Task")
        }
    }
}
