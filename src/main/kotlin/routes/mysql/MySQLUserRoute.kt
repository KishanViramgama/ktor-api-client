package routes.mysql

import base.APiName.MY_SQL_USERS
import base.BaseResponse
import base.IpPort.BASE_IMAGE_URL
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.accept
import io.ktor.server.routing.contentType
import io.ktor.server.routing.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import response.alluser.UserItem
import database.UsersSQl
import response.alluser.AllUserResponse

fun Route.mySQLUserRoute() {
    get(MY_SQL_USERS) {

        contentType(
            ContentType.Application.Json, build = {})

        accept(
            ContentType.Application.Json, build = {})  // Ensure response is JSON

        try {
            val users = withContext(Dispatchers.IO) {
                transaction {
                    UsersSQl.selectAll().map {
                        UserItem(
                            id = it[UsersSQl.id],
                            name = it[UsersSQl.name],
                            email = it[UsersSQl.email],
                            password = it[UsersSQl.password].hashCode().toString(),
                            image = BASE_IMAGE_URL + it[UsersSQl.image]
                        )
                    }
                }
            }

            call.respond(
                HttpStatusCode.OK, AllUserResponse(
                    status = true, message = "Users fetched successfully", data = users
                )
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                BaseResponse(false, e.message ?: "Error fetching users")
            )
        }
    }
}