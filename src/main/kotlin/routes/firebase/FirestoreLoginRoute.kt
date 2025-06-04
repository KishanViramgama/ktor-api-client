package routes.firebase

import base.APiName.LOGIN
import base.BaseResponse
import base.IpPort.BASE_IMAGE_URL
import com.google.cloud.firestore.Firestore
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
import response.alluser.UserItem
import response.login.LoginRequest
import response.login.LoginResponse
import validation.ValidationMessages
import validation.ValidationUtils

fun Route.firestoreLoginRoute(firestore: Firestore) {
    post(LOGIN) {

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
                    BaseResponse(status = false, message = ValidationMessages.INVALID_JSON)
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


            val usersCollection = firestore.collection("users")

            try {
                val querySnapshot = withContext(Dispatchers.IO) {
                    usersCollection.whereEqualTo("email", loginRequest.email).get().get()
                }

                if (querySnapshot.isEmpty) {
                    call.respond(
                        HttpStatusCode.NotFound,
                        BaseResponse(false, ValidationMessages.USER_NOT_FOUND)
                    )
                    return@post
                }

                val userDoc = querySnapshot.documents.first()
                val storedPassword = userDoc.getString("password") ?: ""

                if (storedPassword == loginRequest.password) {
                    call.respond(
                        HttpStatusCode.OK, LoginResponse(
                            true, ValidationMessages.LOGIN_SUCCESS, UserItem(
                                userDoc.id,
                                userDoc.getString("name").toString(),
                                loginRequest.email,
                                loginRequest.password.hashCode().toString(),
                                BASE_IMAGE_URL + userDoc.getString("image").toString(),
                            )
                        )
                    )
                } else {
                    call.respond(
                        HttpStatusCode.Unauthorized,
                        BaseResponse(false, ValidationMessages.INCORRECT_PASSWORD)
                    )
                }

            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ValidationMessages.INTERNAL_SERVER_ERROR
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