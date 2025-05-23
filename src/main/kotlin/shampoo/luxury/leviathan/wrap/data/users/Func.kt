package shampoo.luxury.leviathan.wrap.data.users

import at.favre.lib.crypto.bcrypt.BCrypt
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Inserts a new user into the database if the user does not already exist.
 *
 * @param username The username for the user.
 * @param password The plain-text password for the user (should be hashed before storing).
 *
 * @throws IllegalStateException if a user with the given userId already exists.
 */
fun insertUser(
    username: String,
    password: String,
): Unit =
    transaction {
        val existingUser = Users.select(Users.username eq username).singleOrNull()

        check(existingUser == null) { "User with name $username already exists." }

        Users.insert {
            it[Users.username] = username
            it[Users.password] = BCrypt.withDefaults().hashToString(12, password.toCharArray())
        }
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
): Boolean =
    transaction {
        val hashed =
            Users
                .select(Users.username eq username, Users.password)
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
            .select(Users.username eq username, Users.id)
            .singleOrNull()
            ?.get(Users.id)
            ?.value ?: throw IllegalStateException("User not found.")
    }
