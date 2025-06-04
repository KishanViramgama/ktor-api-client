package validation

object ValidationUtils {

    fun isValidLogin(email: String, password: String): Pair<Boolean, String?> {
        if (email.isEmpty()) return Pair(false, ValidationMessages.EMAIL_EMPTY)
        if (!isValidEmail(email)) return Pair(false, ValidationMessages.EMAIL_INVALID)
        if (password.isEmpty()) return Pair(false, ValidationMessages.PASSWORD_EMPTY)
        if (!isValidPassword(password)) return Pair(false, ValidationMessages.PASSWORD_SHORT)
        return Pair(true, null)
    }

    fun isValidRegistration(name: String, email: String, password: String): Pair<Boolean, String?> {
        if (name.isEmpty()) return Pair(false, ValidationMessages.NAME_EMPTY)
        if (email.isEmpty()) return Pair(false, ValidationMessages.EMAIL_EMPTY)
        if (!isValidEmail(email)) return Pair(false, ValidationMessages.EMAIL_INVALID)
        if (password.isEmpty()) return Pair(false, ValidationMessages.PASSWORD_EMPTY)
        if (!isValidPassword(password)) return Pair(false, ValidationMessages.PASSWORD_SHORT)
        return Pair(true, null)
    }

    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && email.contains("@")
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }


}
