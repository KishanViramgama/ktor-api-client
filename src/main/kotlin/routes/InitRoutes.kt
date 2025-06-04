package routes

import base.BaseResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import validation.ValidationMessages

fun  Route.initRoutes() {
    get("/") {
        val apiKey = call.request.headers["X-API-KEY"]
        if (apiKey == "ktor-sample") {
            call.respond(HttpStatusCode.OK, "✅ Access Granted")
        } else {
            call.respond(
                HttpStatusCode.Unauthorized, BaseResponse(
                    status = false, message = ValidationMessages.INVALID_API_KEY
                )
            )
        }
    }
}