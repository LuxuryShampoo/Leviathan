package shampoo.luxury.leviathan.components

import androidx.compose.runtime.*
import shampoo.luxury.leviathan.wrap.data.initializeDatabase

@Composable
fun AppRoot(content: @Composable () -> Unit) {
    var dbReady by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        initializeDatabase()
        dbReady = true
    }

    if (dbReady) {
        content()
    } else {
        MaxLoading()
    }
}
