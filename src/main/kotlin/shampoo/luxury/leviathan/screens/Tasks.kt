package shampoo.luxury.leviathan.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Notification.Type.Warning
import androidx.compose.ui.window.rememberNotification
import androidx.compose.ui.window.rememberTrayState
import co.touchlab.kermit.Logger
import compose.icons.fontawesomeicons.solid.HourglassStart
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import shampoo.luxury.leviathan.components.Buicon
import shampoo.luxury.leviathan.components.layouts.PageScope
import shampoo.luxury.leviathan.components.tasks.TaskInputForm
import shampoo.luxury.leviathan.components.tasks.TaskList
import shampoo.luxury.leviathan.global.GlobalLoadingState.addLoading
import shampoo.luxury.leviathan.global.GlobalLoadingState.navigate
import shampoo.luxury.leviathan.global.GlobalLoadingState.removeLoading
import shampoo.luxury.leviathan.global.TrayNotif.notifSignal
import shampoo.luxury.leviathan.wrap.data.currency.addToBalance
import shampoo.luxury.leviathan.wrap.data.tasks.Task
import shampoo.luxury.leviathan.wrap.data.tasks.addTask
import shampoo.luxury.leviathan.wrap.data.tasks.deleteTask
import shampoo.luxury.leviathan.wrap.data.tasks.fetchTasks
import shampoo.luxury.leviathan.wrap.data.tasks.updateTask
import xyz.malefic.compose.comps.text.typography.Heading4

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun Tasks() =
    PageScope {
        val scope = rememberCoroutineScope()
        var tasks by remember { mutableStateOf(emptyList<Task>()) }
        var newTaskTitle by remember { mutableStateOf("") }
        var newTaskDescription by remember { mutableStateOf("") }
        val trayState = rememberTrayState()
        val notifState = rememberNotification("Leviathan", "Please wait a few seconds for the database to update", Warning)

        LaunchedEffect(Unit) {
            addLoading("fetching tasks")
            removeLoading("navigation to tasks")
            tasks = fetchTasks()
            removeLoading("fetching tasks")
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
                    { HourglassStart },
                    "Pomodoro",
                    24.dp,
                    32.dp,
                ) {
                    navigate("pomodoro")
                }
            }

            TaskInputForm(
                newTaskTitle,
                newTaskDescription,
                { newTaskTitle = it },
                { newTaskDescription = it },
                {
                    tasks += Task(Int.MAX_VALUE, newTaskTitle, newTaskDescription, false)
                    val tempTitle = newTaskTitle
                    newTaskTitle = ""
                    val tempDescription = newTaskDescription
                    newTaskDescription = ""
                    GlobalScope
                        .launch {
                            addTask(tempTitle, tempDescription.ifBlank { null })
                        }.invokeOnCompletion {
                            scope.launch {
                                tasks = fetchTasks()
                            }
                        }
                },
            )

            TaskList(
                tasks,
            ) { id, isCompleted ->
                if (id == Int.MAX_VALUE) {
                    Logger.w("Tasks") { "Attempted to update task with invalid ID: $id" }
                    trayState.sendNotification(notifState)
                    GlobalScope.launch {
                        notifSignal.emit(notifState)
                    }
                    return@TaskList
                }
                if (isCompleted) {
                    tasks.firstOrNull { it.id == id }?.let {
                        Logger.d("Tasks") { "Removing $it" }
                        tasks -= it
                    }
                }
                GlobalScope
                    .launch {
                        if (isCompleted) {
                            addToBalance(10)
                            deleteTask(id)
                            Logger.d("Tasks") { "Task $id has been removed" }
                        } else {
                            updateTask(id, false)
                        }
                    }.invokeOnCompletion {
                        scope.launch {
                            tasks = fetchTasks()
                        }
                    }
            }
        }
    }
