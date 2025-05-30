package shampoo.luxury.leviathan.components

import androidx.compose.runtime.*
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
        MaxLoading().takeIf { loading }
        content()
    }
}
