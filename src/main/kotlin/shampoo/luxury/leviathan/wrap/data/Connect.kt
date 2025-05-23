package shampoo.luxury.leviathan.wrap.data

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import shampoo.luxury.leviathan.screens.Settings
import shampoo.luxury.leviathan.wrap.data.settings.Settings
import shampoo.luxury.leviathan.wrap.data.tasks.Tasks
import shampoo.luxury.leviathan.wrap.data.users.Users

fun connectToDatabase() {
    Database.connect(
        "jdbc:mysql://27rQFmgU7yAiwXz.root:pETtqfozY7dTG6nq@gateway01.us-west-2.prod.aws.tidbcloud.com:4000/leviathan?sslMode=VERIFY_IDENTITY",
        "com.mysql.cj.jdbc.Driver",
    )
}

fun initializeDatabase() {
    connectToDatabase()
    transaction {
        SchemaUtils.create(Users, Tasks, Settings)
    }
}
