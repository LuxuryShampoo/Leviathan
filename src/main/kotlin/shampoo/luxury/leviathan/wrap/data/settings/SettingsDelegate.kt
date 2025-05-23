package shampoo.luxury.leviathan.wrap.data.settings

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A delegate class for managing application settings with support for `Boolean` and `String` types.
 * This delegate allows properties to automatically read from and write to a settings storage
 * using the provided key and default value.
 *
 * @param T The type of the setting value. Supported types are `Boolean` and `String`.
 * @property key The unique key used to identify the setting in the storage.
 * @property defaultValue The default value to return if the setting is not found in the storage.
 *
 * @throws IllegalArgumentException If the type of the default value or the value being set is unsupported.
 */
class SettingsDelegate<T>(
    private val key: String,
    private val defaultValue: T,
) : ReadWriteProperty<Any?, T> {
    /**
     * Retrieves the value of the setting from the storage.
     *
     * @param thisRef The object containing the property (not used in this implementation).
     * @param property The metadata for the property being accessed.
     * @return The value of the setting, or the default value if the setting is not found.
     * @throws IllegalArgumentException If the type of the default value is unsupported.
     */
    @Suppress("UNCHECKED_CAST")
    override fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): T =
        when (defaultValue) {
            is Boolean -> getBooleanSetting(key, defaultValue) as T
            is String -> getSetting(key, defaultValue) as T
            else -> throw IllegalArgumentException("Unsupported type")
        }

    /**
     * Updates the value of the setting in the storage.
     *
     * @param thisRef The object containing the property (not used in this implementation).
     * @param property The metadata for the property being updated.
     * @param value The new value to set for the setting.
     * @throws IllegalArgumentException If the type of the value being set is unsupported.
     */
    override fun setValue(
        thisRef: Any?,
        property: KProperty<*>,
        value: T,
    ) {
        when (value) {
            is Boolean -> setBooleanSetting(key, value)
            is String -> setSetting(key, value)
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }
}
