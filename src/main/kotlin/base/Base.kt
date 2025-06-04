package base

object IpPort {

    const val IP_ADDRESS = "192.168.5.132"
    const val PORT = 8080
    const val UPLOADS_FOLDER = "/uploads"

    const val BASE_IMAGE_URL = "http://${IP_ADDRESS}:${PORT}${UPLOADS_FOLDER}/"


}

object APiName {

    const val LOGIN = "/login"
    const val REGISTER = "/register"
    const val USER = "/users"

    const val MY_SQL_LOGIN = "mySql/login"
    const val MY_SQL_REGISTER = "mySql/register"
    const val MY_SQL_USERS = "mySql/users"


}