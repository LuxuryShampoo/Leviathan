package shampoo.luxury.leviathan.wrap.data.tasks

import org.jetbrains.exposed.dao.id.IntIdTable
import java.awt.SystemColor.text

/**
 * Represents the `Tasks` table in the database.
 * This table is used to store information about tasks, including their ID, title, description,
 * completion status, and primary key.
 */
object Tasks : IntIdTable() {
    /**
     * The title of the task.
     * This is a required field with a maximum length of 255 characters.
     */
    val title = varchar("title", 255)

    /**
     * A detailed description of the task.
     * This field is optional and can be null.
     */
    val description = text("description").nullable()

    /**
     * Indicates whether the task is completed.
     * Defaults to `false` if not specified.
     */
    val isCompleted = bool("is_completed").default(false)
}
