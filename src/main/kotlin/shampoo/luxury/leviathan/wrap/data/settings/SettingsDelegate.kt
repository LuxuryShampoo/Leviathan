package shampoo.luxury.leviathan.wrap.data.settings

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A delegate class for managing application settings with support for `Boolean` types only.
 * This delegate allows properties to automatically read from and write to a settings storage
 * using the provided key and default value.
 *
 * @property key The unique key used to identify the setting in the storage.
 * @property defaultValue The default value to return if the setting is not found in the storage.
 */
class SettingsDelegate(
    private val key: String,
    private val defaultValue: Boolean,
) : ReadWriteProperty<Any?, Boolean> {
    /**
     * Retrieves the value of the setting from the storage.
     *
     * @param thisRef The object containing the property (not used in this implementation).
     * @param property The metadata for the property being accessed.
     * @return The value of the setting, or the default value if the setting is not found.
     */
    override fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): Boolean = getBooleanSetting(key, defaultValue)

    /**
     * Updates the value of the setting in the storage.
     *
     * @param thisRef The object containing the property (not used in this implementation).
     * @param property The metadata for the property being updated.
     * @param value The new value to set for the setting.
     */
    override fun setValue(
        thisRef: Any?,
        property: KProperty<*>,
        value: Boolean,
    ) {
        setBooleanSetting(key, value)
    }
}
