package shampoo.luxury.leviathan.theme

/**
 * Enum class representing different themes for the application.
 *
 * @property displayName The user-friendly name of the theme.
 * @property filePath The file path to the theme's configuration file.
 */
enum class Theme(
    val displayName: String,
    val filePath: String,
) {
    DARK("Dark", "/theme/dark.json"),
    NEON("Neon", "/theme/neon.json"),
    GALAXY("Galaxy", "/theme/galaxy.json"),
    OBSIDIAN("Obsidian", "/theme/obsidian.json"),
    MIDNIGHT("Midnight", "/theme/midnight.json"),
    LIGHT("Light", "/theme/light.json"),
    MONOCHROME("Monochrome", "/theme/monochrome.json"),
    GRASSY("Grassy", "/theme/grassy.json"),
    FOREST("Forest", "/theme/forest.json"),
    BANANA("Banana", "/theme/banana.json"),
    LAVA("Lava", "/theme/lava.json"),
    OCEAN("Ocean", "/theme/ocean.json"),
    SUNSET("Sunset", "/theme/sunset.json"),
    RETRO("Retro", "/theme/retro.json"),
    ;

    companion object {
        /**
         * The total number of themes available.
         */
        val size by lazy { entries.size }

        /**
         * Retrieves a theme based on its file path.
         *
         * @param path The file path of the theme.
         * @return The corresponding theme if found, or the default theme (DARK) if not.
         */
        fun fromPath(path: String) = entries.find { it.filePath == path } ?: DARK
    }
}
