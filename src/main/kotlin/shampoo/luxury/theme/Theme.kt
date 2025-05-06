package shampoo.luxury.theme

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
    ;

    companion object {
        fun fromPath(path: String) = entries.find { it.filePath == path } ?: DARK
    }
}
