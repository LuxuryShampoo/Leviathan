package shampoo.luxury.leviathan.wrap.data.tasks

import org.jetbrains.exposed.sql.Table

/**
 * Represents the `Tasks` table in the database.
 * This table is used to store information about tasks, including their ID, title, description,
 * completion status, and primary key.
 */
object Tasks : Table() {
    /**
     * The unique identifier for each task.
     * This column is auto-incremented.
     */
    val id = integer("id").autoIncrement()

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

    /**
     * Defines the primary key for the `Tasks` table.
     * The primary key is the `id` column.
     */
    override val primaryKey = PrimaryKey(id)
}
