package shampoo.luxury.leviathan.wrap.data.settings

import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Represents the `Settings` table in the database.
 * This table is used to store application settings in a key-boolean format.
 */
object Settings : IntIdTable() {
    /**
     * The unique key or name of the setting.
     */
    val key = varchar("key", 255).uniqueIndex()

    /**
     * The value of the setting, stored as a boolean.
     */
    val value = bool("value").default(true)
}
