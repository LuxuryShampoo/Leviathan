package shampoo.luxury.leviathan.wrap.data.settings

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun getSetting(key: String): String? =
    transaction {
        Settings
            .select(listOf(Settings.key eq key))
            .map { it[Settings.value] }
            .firstOrNull()
    }

fun setSetting(
    key: String,
    value: String,
) = transaction {
    if (Settings.select(Settings.key eq key).empty()) {
        Settings.insert {
            it[Settings.key] = key
            it[Settings.value] = value
            it[Settings.type] = "string"
        }
    } else {
        Settings.update({ Settings.key eq key }) {
            it[Settings.value] = value
        }
    }
}
