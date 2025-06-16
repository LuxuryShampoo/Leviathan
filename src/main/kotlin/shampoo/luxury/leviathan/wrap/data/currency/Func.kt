package shampoo.luxury.leviathan.wrap.data.currency

import kotlinx.coroutines.Dispatchers.IO
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import shampoo.luxury.leviathan.global.BalanceState
import shampoo.luxury.leviathan.global.Values.user

/**
 * Adds the specified amount to the user's balance.
 *
 * This function performs a database transaction to update the user's balance.
 * If the amount is zero, no update is performed. The balance is ensured to
 * never go below zero. After updating, the new balance is emitted via the
 * `moneySignal`.
 *
 * @param amount The amount to add to the balance. Can be positive or negative.
 */
suspend fun addToBalance(amount: Number) =
    newSuspendedTransaction(IO) {
        if (amount.toDouble() != 0.0) {
            val currentMoney = getMoney()
            Currency.update({ Currency.user eq user }) {
                it[Currency.amount] = (currentMoney + amount.toDouble().toBigDecimal()).coerceAtLeast(0.00.toBigDecimal())
            }
        }
        BalanceState.updateBalance(getMoney())
    }

suspend fun newAddBalance(
    amount: Number,
    current: Number,
) = newSuspendedTransaction(IO) {
    val newBalance = (current.toDouble() + amount.toDouble()).toBigDecimal().coerceAtLeast(0.00.toBigDecimal())
    BalanceState.updateBalance(newBalance)
    Currency.update({ Currency.user eq user }) {
        it[Currency.amount] = newBalance
    }
}

/**
 * Retrieves the current balance of the user.
 *
 * This function performs a database query to fetch the user's balance.
 * If no balance is found, it defaults to 0.00.
 *
 * @return The current balance as a `BigDecimal`.
 */
suspend fun getMoney() =
    newSuspendedTransaction(IO) {
        Currency
            .selectAll()
            .where { Currency.user eq user }
            .singleOrNull()
            ?.get(Currency.amount)
            ?: 0.00.toBigDecimal()
    }
