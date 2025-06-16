package shampoo.luxury.leviathan.wrap.data.currency

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import shampoo.luxury.leviathan.wrap.data.users.Users
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Represents the Currency table in the database.
 * This table stores the currency balance for each user.
 *
 * - `user`: A foreign key referencing the `Users` table.
 *   Ensures a one-to-one relationship between a user and their currency record.
 *   If a user is deleted, their associated currency record is also deleted (CASCADE).
 * - `amount`: The currency balance for the user, stored as a decimal value
 *   with a precision of 18 and scale of 2. Defaults to 0.00.
 * - `primaryKey`: The primary key for the table, set to the `user` column.
 */
object Currency : Table() {
    val user = reference("user_id", Users, onDelete = ReferenceOption.CASCADE)
    val amount = decimal("amount", 18, 2).default(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))
    override val primaryKey = PrimaryKey(user)
}
