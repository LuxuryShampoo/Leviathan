package shampoo.luxury.leviathan.wrap.data.settings

import co.touchlab.kermit.Logger
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import shampoo.luxury.leviathan.global.Values.user

/**
 * Retrieves a boolean setting from the database.
 * If the setting does not exist, it sets the default value and returns it.
 *
 * @param key The unique key identifying the setting.
 * @param defaultValue The default boolean value to use if the setting is not found.
 * @return The value of the setting, or the default value if not found.
 */
fun getBooleanSetting(
    key: String,
    defaultValue: Boolean,
) = transaction {
    val logger = Logger.withTag("Settings")

    val queryResult = Settings.select(Settings.key eq key, Settings.userId eq user, Settings.value).singleOrNull()

    if (queryResult == null) {
        logger.d { "Setting not found for key: $key. Using default value: $defaultValue" }
        setBooleanSetting(key, defaultValue)
        defaultValue
    } else {
        queryResult[Settings.value]
    }
}

/**
 * Sets a boolean setting in the database.
 * If the setting does not exist, it inserts a new record with the given key and value.
 * If the setting already exists, it updates the value.
 *
 * @param key The unique key identifying the setting.
 * @param value The boolean value to set for the setting.
 */
fun setBooleanSetting(
    key: String,
    value: Boolean,
) = transaction {
    val logger = Logger.withTag("Settings")

    if (Settings.select(Settings.key eq key, Settings.userId eq user).empty()) {
        logger.d { "Inserting new boolean setting: key=$key, value=$value" }
        Settings.insert {
            it[userId] = user
            it[Settings.key] = key
            it[Settings.value] = value
        }
    } else {
        logger.d { "Updating existing boolean setting: key=$key, value=$value" }
        Settings.update({ Settings.key eq key and (Settings.userId eq user) }) {
            it[Settings.value] = value
        }
    }
}
