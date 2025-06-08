package shampoo.luxury.leviathan.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import compose.icons.fontawesomeicons.SolidGroup
import compose.icons.fontawesomeicons.solid.ArrowLeft
import compose.icons.fontawesomeicons.solid.Edit
import compose.icons.fontawesomeicons.solid.Pause
import compose.icons.fontawesomeicons.solid.Play
import compose.icons.fontawesomeicons.solid.UndoAlt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import shampoo.luxury.leviathan.components.Buicon
import shampoo.luxury.leviathan.components.layouts.PageScope
import shampoo.luxury.leviathan.global.GlobalLoadingState.navigate
import shampoo.luxury.leviathan.global.GlobalLoadingState.removeLoading
import shampoo.luxury.leviathan.wrap.data.tasks.Task
import shampoo.luxury.leviathan.wrap.data.tasks.fetchTasks
import xyz.malefic.compose.comps.text.typography.Body1
import xyz.malefic.compose.comps.text.typography.Body2
import xyz.malefic.compose.comps.text.typography.Heading3
import xyz.malefic.compose.comps.text.typography.Heading4
import xyz.malefic.compose.comps.text.typography.Heading6
import androidx.compose.foundation.lazy.items as iii

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Pomodoro() =
    PageScope {
        val scope = rememberCoroutineScope { Dispatchers.IO }
        var tasks by remember { mutableStateOf(emptyList<Task>()) }
        var selectedTask by remember { mutableStateOf<Task?>(null) }
        var timeLeft by remember { mutableStateOf(25 * 60) }
        var isRunning by remember { mutableStateOf(false) }
        var showDialog by remember { mutableStateOf(false) }
        var workDuration by remember { mutableStateOf(25) }
        var shortBreakDuration by remember { mutableStateOf(5) }
        var longBreakDuration by remember { mutableStateOf(15) }
        val periods by remember {
            derivedStateOf {
                listOf(
                    workDuration * 60,
                    shortBreakDuration * 60,
                    workDuration * 60,
                    shortBreakDuration * 60,
                    workDuration * 60,
                    shortBreakDuration * 60,
                    workDuration * 60,
                    longBreakDuration * 60,
                )
            }
        }
        var currentPeriodIndex by remember { mutableStateOf(0) }

        LaunchedEffect(Unit) {
            scope.launch {
                tasks = fetchTasks().filter { !it.isCompleted }
                removeLoading("navigation to pomodoro")
            }
        }

        LaunchedEffect(isRunning, currentPeriodIndex) {
            while (isRunning && timeLeft > 0) {
                delay(1000L)
                timeLeft -= 1
            }
            if (timeLeft == 0) {
                isRunning = false
                currentPeriodIndex = (currentPeriodIndex + 1) % periods.size
                timeLeft = periods[currentPeriodIndex]
            }
        }

        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Surface(
                    Modifier.padding(16.dp),
                    MaterialTheme.shapes.medium,
                    elevation = 8.dp,
                ) {
                    Column(
                        Modifier.padding(16.dp),
                        Arrangement.spacedBy(8.dp),
                    ) {
                        Heading6("Customize Durations")
                        OutlinedTextField(
                            workDuration.toString(),
                            { workDuration = it.toIntOrNull() ?: workDuration },
                            label = { Body1("Work Duration (minutes)") },
                        )
                        OutlinedTextField(
                            shortBreakDuration.toString(),
                            { shortBreakDuration = it.toIntOrNull() ?: shortBreakDuration },
                            label = { Body1("Short Break Duration (minutes)") },
                        )
                        OutlinedTextField(
                            longBreakDuration.toString(),
                            { longBreakDuration = it.toIntOrNull() ?: longBreakDuration },
                            label = { Body1("Long Break Duration (minutes)") },
                        )
                        Row(
                            Modifier.fillMaxWidth(),
                            Arrangement.End,
                        ) {
                            TextButton(onClick = { showDialog = false }) {
                                Body1("Cancel")
                            }
                            TextButton(onClick = { showDialog = false }) {
                                Body1("Save")
                            }
                        }
                    }
                }
            }
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            Arrangement.spacedBy(16.dp),
            CenterHorizontally,
        ) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Buicon(
                    { SolidGroup.ArrowLeft },
                    "Tasks",
                    24.dp,
                    32.dp,
                ) {
                    navigate("tasks")
                }
                Heading4("Pomodoro")
            }

            Divider()

            Heading6(
                when (periods[currentPeriodIndex] / 60) {
                    workDuration -> "Working on: ${selectedTask?.title}".takeUnless { selectedTask == null } ?: "Select a task to work on"
                    shortBreakDuration -> "Short Break!"
                    longBreakDuration -> "Long Break!"
                    else -> ""
                },
            )

            Heading3("%02d:%02d".format(timeLeft / 60, timeLeft % 60))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Buicon(
                    { SolidGroup.Play },
                    "Start",
                ) { isRunning = true }
                Buicon(
                    { SolidGroup.Pause },
                    "Pause",
                ) { isRunning = false }
                Buicon(
                    { SolidGroup.UndoAlt },
                    "Reset",
                ) {
                    isRunning = false
                    currentPeriodIndex = 0
                    timeLeft = periods[currentPeriodIndex]
                }
                Buicon(
                    { SolidGroup.Edit },
                    "Customize",
                ) { showDialog = true }
            }

            Divider()

            LazyColumn {
                iii(tasks) { task ->
                    ListItem(
                        Modifier.clickable { selectedTask = task },
                        secondaryText = { Body2(task.description ?: "") },
                    ) { Body1(task.title) }
                }
            }
        }
    }
