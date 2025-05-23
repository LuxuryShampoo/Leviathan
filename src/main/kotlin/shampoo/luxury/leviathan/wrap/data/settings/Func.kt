package shampoo.luxury.leviathan.wrap.data.settings

import co.touchlab.kermit.Logger
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

/**
 * Retrieves a string setting from the database.
 * If the setting does not exist, it sets the default value and returns it.
 *
 * @param key The unique key identifying the setting.
 * @param defaultValue The default value to use if the setting is not found.
 * @return The value of the setting, or the default value if not found.
 */
fun getSetting(
    key: String,
    defaultValue: String,
): String {
    val logger = Logger.withTag("Settings")
    return transaction {
        val queryResult =
            Settings
                .select(Settings.key eq key)
                .also { logger.d { "Query result for key: $key -> $it" } }
                .singleOrNull()
                .toString()

        val value =
            queryResult
                .substringAfter("$key=")
                .substringBefore("=")
                .takeIf { it.isNotEmpty() }

        if (value == null) {
            logger.d { "Setting not found for key: $key. Using default value: $defaultValue" }
            setSetting(key, defaultValue)
            defaultValue
        } else {
            logger.d { "Retrieved setting for key: $key. Value: $value" }
            value
        }
    }
}

/**
 * Retrieves a boolean setting from the database.
 * If the setting does not exist, it sets the default value and returns it.
 *
 * @param key The unique key identifying the setting.
 * @param defaultValue The default value to use if the setting is not found.
 * @return The value of the setting, or the default value if not found.
 */
fun getBooleanSetting(
    key: String,
    defaultValue: Boolean,
): Boolean {
    val logger = Logger.withTag("Settings")
    return transaction {
        val queryResult =
            Settings
                .select(Settings.key eq key)
                .also { logger.d { "Query result for key: $key -> $it" } }
                .singleOrNull()
                .toString()

        val value =
            queryResult
                .substringAfter("$key=")
                .substringBefore("=")
                .toIntOrNull()
                ?.let { it == 1 }

        if (value == null) {
            logger.d { "Setting not found for key: $key. Using default value: $defaultValue" }
            setBooleanSetting(key, defaultValue)
            defaultValue
        } else {
            logger.d { "Retrieved setting for key: $key. Value: $value" }
            value
        }
    }
}

/**
 * Sets a string setting in the database.
 * If the setting does not exist, it inserts a new record.
 *
 * @param key The unique key identifying the setting.
 * @param value The value to set for the setting.
 */
fun setSetting(
    key: String,
    value: String,
) = transaction {
    if (Settings.select(Settings.key eq key).empty()) {
        Settings.insert {
            it[Settings.key] = key
            it[Settings.value] = value
            it[Settings.type] = "string"
        }
    } else {
        Settings.update({ Settings.key eq key }) {
            it[Settings.value] = value
        }
    }
}

/**
 * Sets a boolean setting in the database.
 * If the setting does not exist, it inserts a new record.
 *
 * @param key The unique key identifying the setting.
 * @param value The value to set for the setting (true or false).
 */
fun setBooleanSetting(
    key: String,
    value: Boolean,
) = transaction {
    if (Settings.select(Settings.key eq key).empty()) {
        Settings.insert {
            it[Settings.key] = key
            it[Settings.value] = value.toString()
            it[Settings.type] = "boolean"
        }
    } else {
        Settings.update({ Settings.key eq key }) {
            it[Settings.value] = value.toString()
        }
    }
}
