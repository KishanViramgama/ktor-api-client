package routes.mysql

import base.APiName.MY_SQL_REGISTER
import base.BaseResponse
import base.IpPort.BASE_IMAGE_URL
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.request.contentType
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.accept
import io.ktor.server.routing.contentType
import io.ktor.server.routing.post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import response.alluser.UserItem
import database.UsersSQl
import response.register.RegisterResponse
import validation.ValidationMessages
import validation.ValidationUtils
import java.io.File
import java.util.UUID

fun Route.mySQLRegisterRoute() {

    val logger = LoggerFactory.getLogger("mySQLRegisterRoute")

    post(MY_SQL_REGISTER) {
        logger.info("Accessed: Register")
        logger.info("Content-Type: ${call.request.contentType()}")

        contentType(ContentType.Application.Json) {}
        accept(ContentType.Application.Json) {}

        var getName = ""
        var getEmail = ""
        var getPassword = ""
        var imageName = ""

        val multipart = call.receiveMultipart()
        multipart.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    when (part.name) {
                        "name" -> getName = part.value
                        "email" -> getEmail = part.value
                        "password" -> getPassword = part.value
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

        val (isValid, errorMessage) = ValidationUtils.isValidRegistration(
            getName, getEmail, getPassword
        )

        if (!isValid) {
            call.respond(
                HttpStatusCode.BadRequest,
                BaseResponse(false, errorMessage ?: "Invalid request.")
            )
            return@post
        }

        try {
            val userId = UUID.randomUUID().toString()
            val userItem = UserItem(
                id = userId,
                name = getName,
                email = getEmail,
                password = getPassword,
                image = imageName
            )

            val result = withContext(Dispatchers.IO) {
                transaction {
                    val emailExists = UsersSQl
                        .select { UsersSQl.email eq getEmail }
                        .count() > 0

                    if (emailExists) {
                        return@transaction "exists"
                    }

                    UsersSQl.insert {
                        it[id] = userItem.id
                        it[email] = userItem.email
                        it[name] = userItem.name
                        it[image] = userItem.image
                        it[password] = userItem.password
                    }

                    return@transaction "success"
                }
            }

            when (result) {
                "exists" -> {
                    call.respond(
                        HttpStatusCode.Conflict,
                        BaseResponse(false, ValidationMessages.EMAIL_EXISTS)
                    )
                }

                "success" -> {

                    userItem.image = "$BASE_IMAGE_URL$imageName"

                    call.respond(
                        HttpStatusCode.Created, RegisterResponse(
                            status = true,
                            message = ValidationMessages.REGISTRATION_SUCCESS,
                            userItem = userItem
                        )
                    )
                }

                else -> {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        BaseResponse(false, ValidationMessages.INTERNAL_SERVER_ERROR)
                    )
                }
            }

        } catch (e: Exception) {
            logger.error("Error during registration", e)
            call.respond(
                HttpStatusCode.InternalServerError,
                BaseResponse(false, e.message ?: ValidationMessages.INTERNAL_SERVER_ERROR)
            )
        }
    }
}
