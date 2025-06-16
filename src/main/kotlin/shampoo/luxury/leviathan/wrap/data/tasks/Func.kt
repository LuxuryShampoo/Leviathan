package shampoo.luxury.leviathan.wrap.data.tasks

import co.touchlab.kermit.Logger
import kotlinx.coroutines.Dispatchers.IO
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import shampoo.luxury.leviathan.global.Values.user

/**
 * Adds a new task to the database.
 *
 * @param title The title of the task. Cannot be null.
 * @param description An optional description of the task.
 */
suspend fun addTask(
    title: String,
    description: String?,
) = newSuspendedTransaction(IO) {
    Tasks.insert {
        it[userId] = user
        it[Tasks.title] = title
        it[Tasks.description] = description
    }
}

/**
 * Fetches all tasks from the database.
 *
 * @return A list of tasks, where each task is represented as a `Task` object.
 */
suspend fun fetchTasks() =
    buildList {
        newSuspendedTransaction(IO) {
            Tasks.selectAll().where { Tasks.userId eq user }.map { row ->
                add(
                    Task(
                        row[Tasks.id].value,
                        row[Tasks.title],
                        row[Tasks.description] ?: "",
                        row[Tasks.isCompleted],
                    ),
                )
            }
            Logger.d("Tasks") { "Updated" }
        }
    }

/**
 * Updates the completion status of a task in the database.
 *
 * @param id The unique identifier of the task to update.
 * @param isCompleted The new completion status of the task.
 */
suspend fun updateTask(
    id: Int,
    isCompleted: Boolean,
) = newSuspendedTransaction(IO) {
    Tasks.update({ Tasks.id eq id and (Tasks.userId eq user) }) {
        it[Tasks.isCompleted] = isCompleted
    }
}

/**
 * Deletes a task from the database.
 *
 * @param id The unique identifier of the task to delete.
 */
suspend fun deleteTask(id: Int) =
    newSuspendedTransaction(IO) {
        Tasks.deleteWhere { Tasks.id eq id and (userId eq user) }
    }
