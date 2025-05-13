@file:Suppress("unused")

package shampoo.luxury.leviathan.global

import java.util.prefs.Preferences

fun listAllPreferences() {
    fun traverseNode(
        node: Preferences,
        path: String = "",
    ) {
        try {
            val keys = node.keys()
            if (keys.isNotEmpty()) {
                println("Preferences in node '$path':")
                keys.forEach { key ->
                    println("$key = ${node[key, "N/A"]}")
                }
            }
            val children = node.childrenNames()
            for (child in children) {
                traverseNode(node.node(child), "$path/$child")
            }
        } catch (e: Exception) {
            println("Error accessing node '$path': ${e.message}")
        }
    }

    println("Listing all preferences:")
    traverseNode(Preferences.userRoot())
}

fun clearPreferences(nodePath: String) {
    val prefs = Preferences.userRoot().node(nodePath)
    prefs.clear()
    println("Preferences cleared for node: $nodePath")
}

fun main() {
    clearPreferences("xyz.malefic.compose.prefs.Common")
    clearPreferences("leviathan")
    listAllPreferences()
}
