package routes.firebase

import base.APiName.REGISTER
import base.BaseResponse
import base.IpPort.BASE_IMAGE_URL
import com.google.cloud.firestore.Firestore
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.request.contentType
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import response.alluser.UserItem
import response.register.RegisterResponse
import validation.ValidationMessages
import validation.ValidationUtils
import java.io.File

fun Route.firestoreRegisterRoute(firestore: Firestore) {
    val logger = LoggerFactory.getLogger("firestoreRegisterRoute")
    post(REGISTER) {

        logger.info("Accessed: Register")
        logger.info("Content-Type: ${call.request.contentType()}")

        val apiKey = call.request.headers["X-API-KEY"]
        if (apiKey != "ktor-sample") {
            call.respond(
                HttpStatusCode.Unauthorized,
                BaseResponse(false, ValidationMessages.INVALID_API_KEY)
            )
            return@post
        }

        var name = ""
        var email = ""
        var password = ""
        var imageName = ""

        val multipart = call.receiveMultipart()
        multipart.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    when (part.name) {
                        "name" -> name = part.value
                        "email" -> email = part.value
                        "password" -> password = part.value
                    }
                }

                is PartData.FileItem -> {
                    imageName = part.originalFileName ?: "profile.jpg"
                    val uploadDir = File("uploads")
                    if (!uploadDir.exists()) uploadDir.mkdirs()

                    val imageFile = File(uploadDir, imageName)
                    withContext(Dispatchers.IO) {
                        part.streamProvider().use { input ->
                            imageFile.outputStream().buffered().use { output ->
                                input.copyTo(output)
                            }
                        }
                    }
                }

                else -> Unit
            }
            part.dispose()
        }

        // Validate fields
        val (isValid, errorMessage) = ValidationUtils.isValidRegistration(name, email, password)
        if (!isValid) {
            call.respond(
                HttpStatusCode.BadRequest,
                BaseResponse(false, errorMessage ?: "Invalid request.")
            )
            return@post
        }

        try {
            withContext(Dispatchers.IO) {
                // Check if user already exists
                val existingUser =
                    firestore.collection("users").whereEqualTo("email", email).get().get()
                if (!existingUser.isEmpty) {
                    call.respond(
                        HttpStatusCode.Conflict,
                        BaseResponse(false, ValidationMessages.EMAIL_EXISTS)
                    )
                    return@withContext
                }

                // Save user to Firestore
                val userItem = UserItem(
                    id = "",
                    name = name,
                    email = email,
                    password = password,
                    image = imageName
                )
                val docRef = firestore.collection("users").add(userItem)
                val id = docRef.get().id
                val createdUser = userItem.copy(id = id).copy(image = BASE_IMAGE_URL + imageName)

                call.respond(
                    HttpStatusCode.Created, RegisterResponse(
                        status = true,
                        message = ValidationMessages.REGISTRATION_SUCCESS,
                        userItem = createdUser
                    )
                )
            }
        } catch (e: Exception) {
            logger.error("Registration error: ${e.message}", e)
            call.respond(
                HttpStatusCode.InternalServerError,
                BaseResponse(false, e.message ?: "Server error")
            )
        }
    }
}