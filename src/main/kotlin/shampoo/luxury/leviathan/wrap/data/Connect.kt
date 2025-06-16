package shampoo.luxury.leviathan.wrap.data

import kotlinx.coroutines.Dispatchers.IO
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import shampoo.luxury.leviathan.wrap.data.currency.Currency
import shampoo.luxury.leviathan.wrap.data.pets.Pets
import shampoo.luxury.leviathan.wrap.data.settings.Settings
import shampoo.luxury.leviathan.wrap.data.tasks.Tasks
import shampoo.luxury.leviathan.wrap.data.users.Users

fun connectToDatabase() {
    Database.connect(
        "jdbc:mysql://27rQFmgU7yAiwXz.root:pETtqfozY7dTG6nq@gateway01.us-west-2.prod.aws.tidbcloud.com:4000/leviathan?sslMode=VERIFY_IDENTITY&useInformationSchema=false",
        "com.mysql.cj.jdbc.Driver",
    )
}

suspend fun initializeDatabase() {
    connectToDatabase()
    newSuspendedTransaction(IO) {
        SchemaUtils.create(Users, Tasks, Settings, Pets, Currency)
    }
}
