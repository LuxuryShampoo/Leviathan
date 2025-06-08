package shampoo.luxury.leviathan.components.layouts

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import shampoo.luxury.leviathan.components.MaxLoading
import shampoo.luxury.leviathan.global.GlobalLoadingState
import shampoo.luxury.leviathan.global.GlobalLoadingState.addLoading
import shampoo.luxury.leviathan.global.GlobalLoadingState.databaseLoaded
import shampoo.luxury.leviathan.global.GlobalLoadingState.removeLoading
import shampoo.luxury.leviathan.wrap.data.initializeDatabase

@Composable
fun LoadingBox(content: @Composable () -> Unit) {
    val loading by GlobalLoadingState.isLoading

    LaunchedEffect(Unit) {
        addLoading("database initialization")
        initializeDatabase()
        removeLoading("database initialization")
        databaseLoaded = true
    }

    Box {
        content()
        if (loading) {
            MaxLoading()
        }
    }
}
