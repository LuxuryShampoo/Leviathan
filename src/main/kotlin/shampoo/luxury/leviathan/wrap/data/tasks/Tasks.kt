package shampoo.luxury.leviathan.wrap.data.tasks

import org.jetbrains.exposed.sql.Table

object Tasks : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 255)
    val description = text("description").nullable()
    val isCompleted = bool("is_completed").default(false)
    override val primaryKey = PrimaryKey(id)
}
