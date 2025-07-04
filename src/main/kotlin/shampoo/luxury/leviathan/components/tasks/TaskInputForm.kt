package shampoo.luxury.leviathan.components.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import xyz.malefic.compose.comps.text.typography.Body1
import xyz.malefic.compose.comps.text.typography.Body2

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
            label = { Body2("Task Title") },
            placeholder = { Body1("Enter tasks title") },
            colors = TextFieldDefaults.textFieldColors(textColor = MaterialTheme.colors.onBackground),
        )
        TextField(
            description,
            onDescriptionChange,
            Modifier.fillMaxWidth(),
            label = { Body2("Task Description") },
            placeholder = { Body1("Enter tasks description") },
            colors = TextFieldDefaults.textFieldColors(textColor = MaterialTheme.colors.onBackground),
        )
        Button(
            onAddTask,
            Modifier.align(Alignment.End),
        ) {
            Body1("Add Task")
        }
    }
}
