package validation

object ValidationMessages {
    const val INVALID_JSON = "Invalid JSON format."
    const val NAME_EMPTY = "Name cannot be empty."
    const val EMAIL_EMPTY = "Email cannot be empty."
    const val PASSWORD_EMPTY = "Password cannot be empty."
    const val EMAIL_INVALID = "Invalid email format."
    const val PASSWORD_SHORT = "Password must be at least 8 characters long."
    const val EMAIL_EXISTS = "Email already exists."
    const val USER_NOT_FOUND = "User not found."
    const val INCORRECT_PASSWORD = "Incorrect password."
    const val REGISTRATION_SUCCESS = "User registered successfully."
    const val REGISTRATION_FAILED = "User registration failed."
    const val LOGIN_SUCCESS = "Login successful."
    const val INTERNAL_SERVER_ERROR = "An internal error occurred."
    const val EMAIL_AND_PASSWORD_EMPTY = "Email and password cannot be empty."
    const val INVALID_API_KEY = "Invalid api key."
}