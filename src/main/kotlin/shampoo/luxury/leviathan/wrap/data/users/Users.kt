package shampoo.luxury.leviathan.wrap.data.users

import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Represents the `Users` table in the database.
 * This table stores user information and serves as a reference for user-specific data.
 */
object Users : IntIdTable() {
    /**
     * The username of the user.
     */
    val username = varchar("username", 255).uniqueIndex()

    /**
     * The hashed password of the user.
     */
    val password = varchar("password", 255)
}
