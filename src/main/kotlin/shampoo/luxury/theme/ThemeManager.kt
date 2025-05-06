package shampoo.luxury.theme

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.malefic.Signal
import xyz.malefic.compose.prefs.delegate.StringPreference
import xyz.malefic.ext.stream.grass
import java.io.InputStream

/**
 * Singleton object to manage the application theme.
 * Provides a global themeInputStream that can be accessed and updated from anywhere in the app.
 * Uses Signal for reactive theme change notifications.
 */
object ThemeManager {
    /**
     * The default theme io used when no other theme is selected.
     */
    private const val DEFAULT_THEME = "/theme/dark.json"

    /**
     * A preference-backed property that stores the current theme io.
     * The value is persisted and retrieved using `StringPreference`.
     */
    var currentThemePath by StringPreference("theme_path", DEFAULT_THEME)

    /**
     * A mutable state holding the current theme's input stream.
     * This is used to provide the theme data to the application.
     */
    private val _themeInputStream = mutableStateOf(grass(currentThemePath))

    /**
     * A reactive signal that emits notifications when the theme changes.
     * Listeners can connect to this signal to respond to theme updates.
     */
    val themeChanges = Signal<Int>()

    /**
     * A public getter for the current theme's input stream.
     * Provides access to the theme data for other parts of the application.
     */
    val themeInputStream: InputStream?
        get() = _themeInputStream.value

    init {
        CoroutineScope(Dispatchers.IO).launch {
            updateTheme(currentThemePath)
        }
    }

    /**
     * Updates the current theme.
     *
     * @param themePath The io to the theme file.
     * @return True if the theme was updated successfully, false otherwise.
     */
    suspend fun updateTheme(themePath: String): Boolean {
        val stream = grass(themePath)
        if (stream != null) {
            currentThemePath = themePath
            _themeInputStream.value = stream
            themeChanges.emit(themeChanges.hashCode())
            return true
        }
        return false
    }
}
