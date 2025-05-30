package shampoo.luxury.leviathan.global

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf

object GlobalLoadingState {
    private val loadingIds: MutableState<Set<String>> = mutableStateOf(emptySet())

    val isLoading: State<Boolean>
        @Composable get() = derivedStateOf { loadingIds.value.isNotEmpty() }

    fun addLoading(id: String) {
        loadingIds.value = loadingIds.value + id
    }

    fun removeLoading(id: String) {
        loadingIds.value = loadingIds.value - id
    }
}
