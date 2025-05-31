package shampoo.luxury.leviathan.wrap.data.currency

import kotlinx.coroutines.Dispatchers.IO
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import shampoo.luxury.leviathan.global.Values.user

suspend fun addToBalance(amount: Number) =
    newSuspendedTransaction(IO) {
        addToBalance(amount.toDouble())
    }

@Suppress("UnusedReceiverParameter")
suspend fun Transaction.addToBalance(amount: Number) {
    val currentMoney = getMoney()
    Currency.update({ Currency.user eq user }) {
        it[Currency.amount] = (currentMoney + amount.toDouble().toBigDecimal()).coerceAtLeast(0.00.toBigDecimal())
    }
}

suspend fun getMoney() =
    newSuspendedTransaction(IO) {
        Currency
            .selectAll()
            .where { Currency.user eq user }
            .singleOrNull()
            ?.get(Currency.amount)
            ?: 0.00.toBigDecimal()
    }
