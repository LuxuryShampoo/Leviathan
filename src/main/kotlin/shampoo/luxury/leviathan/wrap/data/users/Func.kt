package shampoo.luxury.leviathan.wrap.data.users

import at.favre.lib.crypto.bcrypt.BCrypt
import kotlinx.coroutines.Dispatchers.IO
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import shampoo.luxury.leviathan.wrap.data.currency.Currency

/**
 * Inserts a new user into the database if the user does not already exist.
 *
 * @param username The username for the user.
 * @param password The plain-text password for the user (should be hashed before storing).
 *
 * @throws IllegalStateException if a user with the given userId already exists.
 */
suspend fun insertUser(
    username: String,
    password: String,
): Unit =
    newSuspendedTransaction(IO) {
        val existingUser = Users.selectAll().where { Users.username eq username }.singleOrNull()

        check(existingUser == null) { "User with name $username already exists." }

        Users.insert {
            it[Users.username] = username
            it[Users.password] = BCrypt.withDefaults().hashToString(12, password.toCharArray())
        }

        Currency.insert {
            it[Currency.user] = getUserIdByUsername(username)
            it[Currency.amount] = 0.00.toBigDecimal()
        }
    }

suspend fun userIdExists(userId: Int): Boolean =
    newSuspendedTransaction(IO) {
        Users.selectAll().where { Users.id eq userId }.any()
    }

/**
 * Verifies if the provided plain-text password matches the stored hashed password for the given username.
 *
 * @param username The username whose password is to be checked.
 * @param password The plain-text password to verify.
 * @return `true` if the password matches the stored hash, `false` otherwise.
 */
fun checkPassword(
    username: String,
    password: String,
) = transaction {
    val hashed =
        Users
            .selectAll()
            .where { Users.username eq username }
            .singleOrNull()
            ?.get(Users.password) ?: return@transaction false
    return@transaction BCrypt.verifyer().verify(password.toCharArray(), hashed).verified
}

/**
 * Retrieves the user ID for a given username.
 *
 * @param username The username to look up.
 * @return The user ID associated with the username.
 * @throws IllegalStateException if the user is not found.
 */
fun getUserIdByUsername(username: String): Int =
    transaction {
        Users
            .select(Users.id)
            .where { Users.username eq username }
            .singleOrNull()
            ?.get(Users.id)
            ?.value ?: throw IllegalStateException("User not found.")
    }

/**
 * Checks if the provided username is valid.
 *
 * A valid username must be between 3 and 20 characters long and may only contain
 * letters, digits, or underscores.
 *
 * @param username The username to validate.
 * @return `true` if the username is valid, `false` otherwise.
 */
fun isValidUsername(username: String): Boolean = username.length in 3..20 && username.all { it.isLetterOrDigit() || it == '_' }

/**
 * Checks if the provided password is valid.
 *
 * A valid password must be at least 8 characters long, contain at least one letter,
 * one digit, and at least one allowed symbol. The password must not contain any whitespace
 * and may only include letters, digits, or the specified allowed symbols.
 *
 * Allowed symbols: !@#\$%^&*()-_=+[]{}|;:',.<>?/`~
 *
 * @param password The password to validate.
 * @return `true` if the password is valid, `false` otherwise.
 */
fun isValidPassword(password: String): Boolean {
    val allowedSymbols = "!@#\\$%^&*()-_=+[]{}|;:',.<>?/`~"
    return password.length >= 8 &&
        password.any { it.isLetter() } &&
        password.any { it.isDigit() } &&
        password.any { it in allowedSymbols } &&
        password.all { it.isLetterOrDigit() || it in allowedSymbols } &&
        !password.any { it.isWhitespace() }
}
