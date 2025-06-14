package shampoo.luxury.leviathan.global

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigDecimal

object BalanceState {
    private val _balance = MutableStateFlow<BigDecimal>(BigDecimal.ZERO)
    val balance: StateFlow<BigDecimal> = _balance.asStateFlow()

    fun updateBalance(newBalance: BigDecimal) {
        _balance.value = newBalance
    }
}
