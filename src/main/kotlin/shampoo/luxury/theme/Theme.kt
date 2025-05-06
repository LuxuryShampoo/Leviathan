package shampoo.luxury.theme

enum class Theme(
    val displayName: String,
    val filePath: String,
) {
    DARK("Dark", "/theme/dark.json"),
    LIGHT("Light", "/theme/light.json"),
    GRASSY("Grassy", "/theme/grassy.json"),
    FOREST("Forest", "/theme/forest.json"),
    OCEAN("Ocean", "/theme/ocean.json"),
    SUNSET("Sunset", "/theme/sunset.json"),
    NEON("Neon", "/theme/neon.json"),
    MONOCHROME("Monochrome", "/theme/monochrome.json"),
    BANANA("Banana", "/theme/banana.json"),
    ;

    companion object {
        fun fromPath(path: String): Theme = entries.find { it.filePath == path } ?: DARK
    }
}
