package shampoo.luxury.leviathan.global

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * An object that manages the global balance state using a `StateFlow`.
 */
object BalanceState {
    private val _balance = MutableStateFlow<BigDecimal>(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))

    /**
     * A public read-only `StateFlow` that exposes the current balance.
     */
    val balance: StateFlow<BigDecimal> = _balance.asStateFlow()

    /**
     * Updates the current balance with a new value.
     *
     * @param newBalance The new balance value to set.
     */
    fun updateBalance(newBalance: BigDecimal) {
        _balance.value = newBalance.setScale(2, RoundingMode.HALF_UP)
    }
}
