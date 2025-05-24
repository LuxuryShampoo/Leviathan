package shampoo.luxury.leviathan.wrap.data.pets

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import shampoo.luxury.leviathan.wrap.data.users.Users

/**
 * Represents the Pets table in the database.
 * Each pet is associated with a user and contains details such as name, resource path, cost, and ownership status.
 */
object Pets : IntIdTable() {
    /**
     * References the ID of the user who owns the pet.
     * Deletes associated pets if the user is deleted.
     */
    val userId = reference("user_id", Users, onDelete = ReferenceOption.CASCADE)

    /**
     * The name of the pet.
     * Maximum length: 50 characters.
     */
    val name = varchar("name", 50)

    /**
     * The file path to the pet's resource (e.g., image).
     * Maximum length: 255 characters.
     */
    val resourcePath = varchar("resource_path", 255)

    /**
     * The cost of the pet.
     * Represented as a double value.
     */
    val cost = double("cost")

    /**
     * Indicates whether the pet is owned by the user.
     * Defaults to false.
     */
    val owned = bool("owned").default(false)
}
