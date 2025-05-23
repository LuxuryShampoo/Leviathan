package shampoo.luxury.leviathan.wrap.data.settings

import org.jetbrains.exposed.sql.Table

/**
 * Represents the `Settings` table in the database.
 * This table is used to store application settings in a key-value format,
 * with an additional column to specify the data type of the value.
 */
object Settings : Table() {
    /**
     * Auto-incrementing primary key for the settings table.
     */
    val id = integer("id").autoIncrement()

    /**
     * The unique key or name of the setting.
     * This column is indexed to ensure uniqueness.
     */
    val key = varchar("key", 255).uniqueIndex()

    /**
     * The value of the setting, stored as a string.
     * The actual type of the value can be determined using the `type` column.
     */
    val value = text("value")

    /**
     * The data type of the setting value (e.g., "boolean", "string").
     * This helps in interpreting the value correctly in the application logic.
     */
    val type = varchar("type", 50)

    /**
     * Defines the primary key for the table, which is the `id` column.
     */
    override val primaryKey = PrimaryKey(id)
}
