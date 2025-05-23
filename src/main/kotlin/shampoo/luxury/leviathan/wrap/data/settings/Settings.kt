package shampoo.luxury.leviathan.wrap.data.settings

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import shampoo.luxury.leviathan.wrap.data.users.Users

/**
 * Represents the `Settings` table in the database.
 * This table is used to store user-specific application settings in a key-boolean format.
 */
object Settings : IntIdTable() {
    /**
     * The ID of the user this setting belongs to.
     */
    val userId = reference("user_id", Users, onDelete = ReferenceOption.CASCADE)

    /**
     * The unique key or name of the setting.
     */
    val key = varchar("key", 255).uniqueIndex()

    /**
     * The value of the setting, stored as a boolean.
     */
    val value = bool("value").default(true)
}
