package shampoo.luxury.leviathan.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.fontawesomeicons.SolidGroup
import compose.icons.fontawesomeicons.solid.ArrowRight
import compose.icons.fontawesomeicons.solid.Pause
import compose.icons.fontawesomeicons.solid.UndoAlt
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import shampoo.luxury.leviathan.components.Buicon
import shampoo.luxury.leviathan.components.PageScope
import shampoo.luxury.leviathan.wrap.data.tasks.Task
import shampoo.luxury.leviathan.wrap.data.tasks.fetchTasks
import xyz.malefic.compose.comps.text.typography.Body1
import xyz.malefic.compose.comps.text.typography.Heading3
import xyz.malefic.compose.comps.text.typography.Heading4
import xyz.malefic.compose.comps.text.typography.Heading6

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Pomodoro() =
    PageScope {
        val scope = rememberCoroutineScope()
        var tasks by remember { mutableStateOf(emptyList<Task>()) }
        var selectedTask by remember { mutableStateOf<Task?>(null) }
        var timeLeft by remember { mutableStateOf(25 * 60) }
        var isRunning by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            scope.launch {
                tasks = fetchTasks()
            }
        }

        LaunchedEffect(isRunning) {
            if (isRunning) {
                while (timeLeft > 0) {
                    delay(1000L)
                    timeLeft -= 1
                }
                isRunning = false
            }
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            Arrangement.spacedBy(16.dp),
            CenterHorizontally,
        ) {
            Heading4("Pomodoro Timer")

            if (selectedTask != null) {
                Heading6("Working on: ${selectedTask!!.title}")
            } else {
                Heading6("Select a task to work on")
            }

            Heading3("${timeLeft / 60}:${timeLeft % 60}")

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Buicon(
                    { SolidGroup.ArrowRight },
                    "Start",
                    onClick = { isRunning = true },
                )
                Buicon(
                    { SolidGroup.Pause },
                    "Pause",
                    onClick = { isRunning = false },
                )
                Buicon(
                    { SolidGroup.UndoAlt },
                    "Reset",
                    onClick = {
                        isRunning = false
                        timeLeft = 25 * 60
                    },
                )
            }

            Divider()

            LazyColumn {
                items(tasks) { task ->
                    ListItem(
                        Modifier.clickable { selectedTask = task },
                        text = { Body1(task.title) },
                        secondaryText = { Body1(task.description ?: "No description") },
                    )
                }
            }
        }
    }
