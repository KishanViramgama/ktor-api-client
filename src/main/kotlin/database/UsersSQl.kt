package database

import org.jetbrains.exposed.sql.Table

object UsersSQl : Table("users") {
    val id = varchar("id", 50)
    val name = varchar("name", 255)
    val email = varchar("email", 255)
    val password = varchar("password", 255)
    val image = varchar("image", 255)
}