package shampoo.luxury.theme

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.malefic.Signal
import xyz.malefic.ext.stream.grass
import java.io.InputStream

/**
 * Singleton object to manage the application theme.
 * Provides a global themeInputStream that can be accessed and updated from anywhere in the app.
 * Uses Signal for reactive theme change notifications.
 */
object ThemeManager {
    // Default theme file path
    private const val DEFAULT_THEME = "/theme/dark.json"

    // MutableState to hold the current theme file path
    private val currentThemePath = mutableStateOf(DEFAULT_THEME)

    // MutableState to hold the current theme input stream
    private val _themeInputStream = mutableStateOf<InputStream?>(grass(DEFAULT_THEME))

    // Signal to emit updates when the theme changes
    val themeChanges = Signal<Int>()

    // Public property to access the theme input stream
    val themeInputStream: InputStream?
        get() = _themeInputStream.value

    // Initialize the theme manager with the default theme
    init {
        CoroutineScope(Dispatchers.IO).launch {
            updateTheme(DEFAULT_THEME)
        }
    }

    /**
     * Updates the current theme.
     *
     * @param themePath The path to the theme file.
     * @return True if the theme was updated successfully, false otherwise.
     */
    suspend fun updateTheme(themePath: String): Boolean {
        val stream = grass(themePath)
        if (stream != null) {
            currentThemePath.value = themePath
            _themeInputStream.value = stream
            themeChanges.emit(themeChanges.hashCode())
            return true
        }
        return false
    }

    /**
     * Gets the current theme file path.
     *
     * @return The current theme file path.
     */
    fun getCurrentThemePath(): String = currentThemePath.value
}
