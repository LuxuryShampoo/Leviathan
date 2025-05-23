package shampoo.luxury.leviathan.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.fontawesomeicons.SolidGroup
import compose.icons.fontawesomeicons.solid.HourglassStart
import kotlinx.coroutines.launch
import shampoo.luxury.leviathan.components.Buicon
import shampoo.luxury.leviathan.components.PageScope
import shampoo.luxury.leviathan.components.TaskInputForm
import shampoo.luxury.leviathan.components.TaskList
import shampoo.luxury.leviathan.wrap.data.tasks.Task
import shampoo.luxury.leviathan.wrap.data.tasks.addTask
import shampoo.luxury.leviathan.wrap.data.tasks.deleteTask
import shampoo.luxury.leviathan.wrap.data.tasks.fetchTasks
import shampoo.luxury.leviathan.wrap.data.tasks.updateTask
import xyz.malefic.compose.comps.text.typography.Heading4
import xyz.malefic.compose.nav.RouteManager.navi
import xyz.malefic.ext.precompose.gate

@Composable
fun Tasks() =
    PageScope {
        val scope = rememberCoroutineScope()
        var tasks by remember { mutableStateOf(emptyList<Task>()) }
        var newTaskTitle by remember { mutableStateOf("") }
        var newTaskDescription by remember { mutableStateOf("") }

        LaunchedEffect(Unit) {
            scope.launch {
                tasks = fetchTasks()
            }
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            Arrangement.spacedBy(16.dp),
            CenterHorizontally,
        ) {
            Row(
                Modifier.fillMaxWidth(),
                Arrangement.SpaceBetween,
                CenterVertically,
            ) {
                Heading4("Tasks")
                Buicon(
                    { SolidGroup.HourglassStart },
                    "Pomodoro",
                    24.dp,
                    32.dp,
                ) {
                    navi gate "pomodoro"
                }
            }

            TaskInputForm(
                newTaskTitle,
                newTaskDescription,
                { newTaskTitle = it },
                { newTaskDescription = it },
                {
                    scope.launch {
                        addTask(newTaskTitle, newTaskDescription.ifBlank { null })
                        tasks = fetchTasks()
                        newTaskTitle = ""
                        newTaskDescription = ""
                    }
                },
            )

            TaskList(
                tasks,
                { id, isCompleted ->
                    scope.launch {
                        updateTask(id, isCompleted)
                        tasks = fetchTasks()
                    }
                },
                { id ->
                    scope.launch {
                        deleteTask(id)
                        tasks = fetchTasks()
                    }
                },
            )
        }
    }
