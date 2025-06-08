package shampoo.luxury.leviathan.components.settings

import androidx.compose.material.Switch
import androidx.compose.runtime.Composable

/**
 * A composable function that represents a boolean setting option in the UI.
 * It displays a switch component that allows the user to toggle the setting on or off.
 *
 * @param setting The name or label of the setting to be displayed.
 * @param boolean The current value of the setting (true for enabled, false for disabled).
 * @param setSetting A lambda function to update the setting value when the switch is toggled.
 */
@Composable
fun BooleanSetting(
    setting: String,
    boolean: Boolean,
    setSetting: (Boolean) -> Unit,
) {
    SettingsOption(setting) {
        Switch(
            boolean,
            setSetting,
        )
    }
}
