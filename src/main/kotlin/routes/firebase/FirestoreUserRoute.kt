package routes.firebase

import base.APiName.USER
import base.BaseResponse
import base.IpPort.BASE_IMAGE_URL
import com.google.cloud.firestore.Firestore
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.accept
import io.ktor.server.routing.contentType
import io.ktor.server.routing.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import response.alluser.UserItem
import response.alluser.AllUserResponse
import validation.ValidationMessages

fun Route.firestoreUserRoute(firestore: Firestore) {
    val logger = LoggerFactory.getLogger("firestoreRegisterRoute")
    get(USER) {
        val apiKey = call.request.headers["X-API-KEY"]
        if (apiKey == "ktor-sample") {
            try {

                contentType(
                    ContentType.Application.Json, build = {})

                accept(
                    ContentType.Application.Json, build = {})  // Ensure response is JSON

                withContext(Dispatchers.IO) {
                    val querySnapshot = firestore.collection("users").get()
                    val users = querySnapshot.get().map { document ->
                        UserItem(
                            id = document.id, // Get document ID
                            name = document.getString("name") ?: "", // Get name
                            email = document.getString("email") ?: "", // Get email
                            password = document.getString("password").hashCode().toString(),
                            image = (BASE_IMAGE_URL + document.getString("image"))
                        )
                    }

                    call.respond(
                        HttpStatusCode.OK, AllUserResponse(
                            status = true,
                            message = "Users fetched successfully",
                            users.ifEmpty { emptyList() })
                    )
                }

            } catch (e: Exception) {
                logger.error("Error getting users: ${e.message}", e)
                call.respond(
                    HttpStatusCode.BadRequest, BaseResponse(
                        status = false, message = e.message ?: "Error registering user"
                    )
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