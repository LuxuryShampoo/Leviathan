package shampoo.luxury.leviathan.wrap.data.tasks

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import shampoo.luxury.leviathan.wrap.data.users.Users

/**
 * Represents the `Tasks` table in the database.
 * This table is used to store user-specific tasks.
 */
object Tasks : IntIdTable() {
    /**
     * The ID of the user this task belongs to.
     */
    val userId = reference("user_id", Users, onDelete = ReferenceOption.CASCADE)

    /**
     * The title of the task.
     */
    val title = varchar("title", 255)

    /**
     * A detailed description of the task.
     */
    val description = text("description").nullable()

    /**
     * Indicates whether the task is completed.
     */
    val isCompleted = bool("is_completed").default(false)
}
