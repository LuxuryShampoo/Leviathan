package shampoo.luxury.leviathan.components

import androidx.compose.runtime.*
import co.touchlab.kermit.Logger
import shampoo.luxury.leviathan.global.GlobalLoadingState
import shampoo.luxury.leviathan.global.GlobalLoadingState.addLoading
import shampoo.luxury.leviathan.global.GlobalLoadingState.removeLoading
import shampoo.luxury.leviathan.wrap.data.initializeDatabase
import xyz.malefic.compose.comps.box.BackgroundBox

@Composable
fun AppRoot(content: @Composable () -> Unit) {
    val loading by GlobalLoadingState.isLoading

    LaunchedEffect(Unit) {
        addLoading("database initialization")
        initializeDatabase()
        removeLoading("database initialization")
    }

    BackgroundBox {
        if (loading) {
            MaxLoading()
            Logger.d("Loading in progress, showing loading indicator", tag = "AppRoot")
        } else {
            Logger.d("Loading complete, rendering content", tag = "AppRoot")
        }
        content()
    }
}
