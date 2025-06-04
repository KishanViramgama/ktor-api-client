package routes.mysql

import base.APiName.MY_SQL_LOGIN
import base.BaseResponse
import base.IpPort.BASE_IMAGE_URL
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.accept
import io.ktor.server.routing.contentType
import io.ktor.server.routing.post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import response.alluser.UserItem
import database.UsersSQl
import response.login.LoginRequest
import response.login.LoginResponse
import validation.ValidationMessages
import validation.ValidationUtils

fun Route.mySqlLoginRoute() {
    post(MY_SQL_LOGIN) {

        val apiKey = call.request.headers["X-API-KEY"]
        if (apiKey == "ktor-sample") {

            contentType(
                ContentType.Application.Json, build = {})

            accept(
                ContentType.Application.Json, build = {})  // Ensure response is JSON

            val loginRequest = try {
                call.receive<LoginRequest>()
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    BaseResponse(false, ValidationMessages.INVALID_JSON)
                )
                return@post
            }

            val (isValid, errorMessage) = ValidationUtils.isValidLogin(
                loginRequest.email, loginRequest.password
            )
            if (!isValid) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    BaseResponse(false, errorMessage ?: "Invalid request.")
                )
                return@post
            }

            try {
                val user = withContext(Dispatchers.IO) {
                    transaction {
                        UsersSQl.select { UsersSQl.email eq loginRequest.email }
                            .firstOrNull()
                    }
                }

                if (user == null) {
                    call.respond(
                        HttpStatusCode.NotFound,
                        BaseResponse(false, ValidationMessages.USER_NOT_FOUND)
                    )
                } else {
                    val storedPassword = user[UsersSQl.password]
                    if (storedPassword == loginRequest.password) {
                        call.respond(
                            HttpStatusCode.OK, LoginResponse(
                                status = true,
                                message = ValidationMessages.LOGIN_SUCCESS,
                                data = UserItem(
                                    id = user[UsersSQl.id],
                                    name = user[UsersSQl.name],
                                    email = user[UsersSQl.email],
                                    password = loginRequest.password.hashCode().toString(),
                                    image = BASE_IMAGE_URL + user[UsersSQl.image]
                                )
                            )
                        )
                    } else {
                        call.respond(
                            HttpStatusCode.Unauthorized,
                            BaseResponse(false, ValidationMessages.INCORRECT_PASSWORD)
                        )
                    }
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    BaseResponse(false, ValidationMessages.INTERNAL_SERVER_ERROR)
                )
            }

        } else {
            call.respond(
                HttpStatusCode.Unauthorized, BaseResponse(
                    status = false, message = ValidationMessages.INVALID_API_KEY
                )
            )
        }
    }
}