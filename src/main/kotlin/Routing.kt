import base.IpPort.IP_ADDRESS
import base.IpPort.PORT
import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.files
import io.ktor.server.http.content.static
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import routes.firebase.firestoreLoginRoute
import routes.firebase.firestoreRegisterRoute
import routes.firebase.firestoreUserRoute
import routes.initRoutes
import routes.mysql.mySQLRegisterRoute
import routes.mysql.mySQLUserRoute
import routes.mysql.mySqlLoginRoute

fun Application.configureRouting() {

    val firestore: Firestore by lazy {
        FirestoreClient.getFirestore()
    }

    embeddedServer(Netty, port = PORT, host = IP_ADDRESS) { // Replace with your IP

        routing {

            // Serve static files from "uploads" folder
            static("/uploads") {
                files("uploads") // This is the folder name in your project root
            }

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            initRoutes()
            firestoreLoginRoute(firestore)
            firestoreRegisterRoute(firestore)
            firestoreUserRoute(firestore)
            mySqlLoginRoute()
            mySQLRegisterRoute()
            mySQLUserRoute()

        }

    }.start(wait = true)

}
