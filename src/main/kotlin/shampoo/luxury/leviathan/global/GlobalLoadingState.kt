package shampoo.luxury.leviathan.global

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.malefic.compose.nav.RouteManager.navi
import xyz.malefic.ext.precompose.gate

object GlobalLoadingState {
    private val loadingIds: MutableState<Set<String>> = mutableStateOf(emptySet())
    private val log = Logger.withTag("GlobalLoadingState")
    var databaseLoaded = false

    val isLoading: State<Boolean>
        @Composable get() = derivedStateOf { loadingIds.value.isNotEmpty() }

    fun addLoading(id: String) {
        log.d("Adding loading state for: $id")
        loadingIds.value = loadingIds.value + id
    }

    fun removeLoading(id: String) {
        log.d("Removing loading state for: $id")
        loadingIds.value = loadingIds.value - id
    }

    fun navigate(route: String) {
        addLoading("navigation to $route")
        CoroutineScope(Dispatchers.Main).launch {
            navi gate route
        }
    }
}
