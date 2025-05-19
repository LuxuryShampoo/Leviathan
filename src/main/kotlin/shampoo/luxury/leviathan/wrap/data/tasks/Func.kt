package shampoo.luxury.leviathan.wrap.data.tasks

import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun addTask(
    title: String,
    description: String?,
) {
    transaction {
        Tasks.insert {
            it[Tasks.title] = title
            it[Tasks.description] = description
        }
    }
}

fun getAllTasks(): List<Map<String, Any?>> =
    transaction {
        Tasks.selectAll().map { row ->
            mapOf(
                "id" to row[Tasks.id],
                "title" to row[Tasks.title],
                "description" to row[Tasks.description],
                "isCompleted" to row[Tasks.isCompleted],
            )
        }
    }

fun updateTask(
    id: Int,
    isCompleted: Boolean,
) {
    transaction {
        Tasks.update({ Tasks.id eq id }) {
            it[Tasks.isCompleted] = isCompleted
        }
    }
}

fun deleteTask(id: Int) {
    transaction {
        Tasks.deleteWhere { with(it) { Tasks.id eq id } }
    }
}
