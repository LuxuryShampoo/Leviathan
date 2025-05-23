package shampoo.luxury.leviathan.wrap.data.settings

import org.jetbrains.exposed.dao.id.IntIdTable
import java.awt.SystemColor.text

/**
 * Represents the `Settings` table in the database.
 * This table is used to store application settings in a key-value format,
 * with an additional column to specify the data type of the value.
 */
object Settings : IntIdTable() {
    /**
     * The unique key or name of the setting.
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
}
