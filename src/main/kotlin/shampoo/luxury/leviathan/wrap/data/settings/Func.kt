package shampoo.luxury.leviathan.wrap.data.settings

import co.touchlab.kermit.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import shampoo.luxury.leviathan.global.Values.Prefs.setListenSetting
import shampoo.luxury.leviathan.global.Values.Prefs.setSpeakSetting
import shampoo.luxury.leviathan.global.Values.user

/**
 * Retrieves a boolean setting from the database.
 * If the setting does not exist, it sets the default value and returns it.
 *
 * @param key The unique key identifying the setting.
 * @param defaultValue The default boolean value to use if the setting is not found.
 * @return The value of the setting, or the default value if not found.
 */
suspend fun getBooleanSetting(
    key: String,
    defaultValue: Boolean,
): Boolean =
    withContext(IO) {
        val logger = Logger.withTag("Settings")
        val queryResult =
            transaction {
                Settings.selectAll().where { Settings.userId eq user and (Settings.key eq key) }.singleOrNull()
            }
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
suspend fun setBooleanSetting(
    key: String,
    value: Boolean,
) = withContext(IO) {
    val logger = Logger.withTag("Settings")
    transaction {
        if (Settings.selectAll().where { Settings.userId eq user and (Settings.key eq key) }.empty()) {
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
}

/**
 * Saves the current speak and listen settings if they have changed.
 *
 * This function compares the new local speak and listen values with the old values.
 * If a value has changed, it updates the corresponding setting using the provided setter functions.
 * All operations are performed on the IO dispatcher.
 *
 * @param log Logger instance for logging actions.
 * @param localSpeak The new value for the speak setting.
 * @param localListen The new value for the listen setting.
 * @param oldSpeak The previous value for the speak setting.
 * @param oldListen The previous value for the listen setting.
 */
suspend fun saveSettings(
    log: Logger,
    localSpeak: Boolean,
    localListen: Boolean,
    oldSpeak: Boolean,
    oldListen: Boolean,
) = withContext(Dispatchers.IO) {
    log.d { "Saving settings: speak=$localSpeak, listen=$localListen" }
    if (localSpeak != oldSpeak) {
        setSpeakSetting(localSpeak)
    }
    if (localListen != oldListen) {
        setListenSetting(localListen)
    }
    log.d { "Settings saved: speak=$localSpeak, listen=$localListen" }
}
